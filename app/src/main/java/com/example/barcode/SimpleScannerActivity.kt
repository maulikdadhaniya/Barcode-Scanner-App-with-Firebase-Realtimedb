package com.example.barcode

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler


class SimpleScannerActivity : Activity(), ResultHandler {
    private var mScannerView: ZXingScannerView? = null
    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        mScannerView = ZXingScannerView(this)
        setContentView(mScannerView)
    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }

    override fun handleResult(rawResult: Result) {
        Log.v("YYYY", rawResult.text)
        Log.v(
            "YYYY",
            rawResult.barcodeFormat.toString()
        )

        saveDataToFirebase(rawResult.text, rawResult.barcodeFormat.toString())

        mScannerView!!.resumeCameraPreview(this)
    }

    private fun saveDataToFirebase(barcodeData: String, barcodeType: String) {
        val ref = FirebaseDatabase.getInstance().getReference("barcode")
        val barcodeId = ref.push().key

        val barcode = DataModel(barcodeId!!, barcodeData, barcodeType)

        ref.child(barcodeId).setValue(barcode).addOnCompleteListener(OnCompleteListener {
            Toast.makeText(this, "Save Data !!", Toast.LENGTH_SHORT).show()

            finish()
        })
    }
}
