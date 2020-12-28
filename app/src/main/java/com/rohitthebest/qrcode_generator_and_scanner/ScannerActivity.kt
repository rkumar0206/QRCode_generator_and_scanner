package com.rohitthebest.qrcode_generator_and_scanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.rohitthebest.qrcode_generator_and_scanner.databinding.ActivityScannerBinding
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var binding: ActivityScannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setScannerProperties()
    }

    private fun setScannerProperties() {

        binding.scannerView.apply {

            setFormats(listOf(BarcodeFormat.QR_CODE))
            setAutoFocus(true)
            setLaserColor(R.color.teal_200)
            setMaskColor(R.color.purple_200)
        }
    }

    override fun onResume() {
        super.onResume()

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Camera permission not granted", Toast.LENGTH_SHORT).show()

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 12)
            return
        }

        binding.scannerView.startCamera()
        binding.scannerView.setResultHandler(this)
    }

    override fun handleResult(p0: Result?) {

        if (p0 != null) {

            Toast.makeText(this, "$p0", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onPause() {
        super.onPause()

        binding.scannerView.stopCamera()
    }

}