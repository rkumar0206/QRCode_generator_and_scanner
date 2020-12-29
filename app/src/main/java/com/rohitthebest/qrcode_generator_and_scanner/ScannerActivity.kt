package com.rohitthebest.qrcode_generator_and_scanner

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.rohitthebest.qrcode_generator_and_scanner.databinding.ActivityScannerBinding
import me.dm7.barcodescanner.zxing.ZXingScannerView

private const val TAG = "ScannerActivity"

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

            if (p0.text.toString().trim().startsWith("https://") ||
                p0.text.toString().trim().startsWith("http://")
            ) {

                openLinkInBrowser(p0.toString().trim())

            } else {

                AlertDialog.Builder(this)
                    .setTitle("Message")
                    .setMessage(p0.text.toString().trim())
                    .setPositiveButton("OK") { dialog, _ ->

                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
    }

    private fun checkUrl(url: String): String {

        var urll = ""
        try {
            if (url.startsWith("https://") || url.startsWith("http://")) {
                urll = url
            } else if (url.isNotEmpty()) {
                urll = "https://www.google.com/search?q=$url"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return urll

    }

    private fun openLinkInBrowser(url: String?) {

        if (CheckNetworkConnection().isInternetAvailable(this)) {
            url?.let {

                try {
                    Log.d(TAG, "Loading Url in default browser.")
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(checkUrl(it)))
                    this.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
        } else {
            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onPause() {
        super.onPause()

        binding.scannerView.stopCamera()
    }

}