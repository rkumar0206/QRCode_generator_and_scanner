package com.rohitthebest.qrcode_generator_and_scanner

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.rohitthebest.qrcode_generator_and_scanner.databinding.ActivityGeneratorActivtyBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class GeneratorActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityGeneratorActivtyBinding
    private var bitMatrix: BitMatrix? = null
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGeneratorActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()

        binding.fullNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                generateQRCode()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun initListener() {

        binding.generateQrCodeButton.setOnClickListener(this)
        binding.shareBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            binding.generateQrCodeButton.id -> {
                generateQRCode()
            }

            binding.shareBtn.id -> {

                saveFileToCacheDirectory()

            }
        }

    }

    private fun saveFileToCacheDirectory() {

        try {

            val cachePath = File(applicationContext.cacheDir, "images")
            cachePath.mkdirs()
            val fos = FileOutputStream("$cachePath/image.png") //overwrites the image everytime
            bitmap?.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()

            //sharing the image
            shareImage()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun shareImage() {

        val imagePath = File(applicationContext.cacheDir, "images")
        val newFile = File(imagePath, "image.png")
        val contentUri = FileProvider.getUriForFile(
            applicationContext,
            "com.rohitthebest.qrcode_generator_and_scanner.provider",
            newFile
        )

        if (contentUri != null) {

            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, contentResolver.getType(contentUri))
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            startActivity(Intent.createChooser(shareIntent, "Share Via"))

        }
    }


    private fun generateQRCode() {

        if (checkForEditTextNullability()) {

            try {

                val qrCodeWriter = QRCodeWriter()

                bitMatrix = qrCodeWriter.encode(
                    binding.fullNameEditText.text.toString().trim(),
                    BarcodeFormat.QR_CODE,
                    350,
                    350
                )

                val width = bitMatrix!!.width
                val height = bitMatrix!!.height

                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bitmap!!.setPixel(
                            x,
                            y,
                            if (bitMatrix!!.get(x, y)) Color.BLACK else Color.WHITE
                        )
                    }
                }

                binding.qrCodeImageView.setImageBitmap(bitmap)

            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {

            Toast.makeText(this, "please write something!!!", Toast.LENGTH_SHORT).show()
        }

    }


    private fun checkForEditTextNullability(): Boolean {

        return binding.fullNameEditText.text.toString().trim().isNotEmpty()
    }

}