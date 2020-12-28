package com.rohitthebest.qrcode_generator_and_scanner

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.rohitthebest.qrcode_generator_and_scanner.databinding.ActivityGeneratorActivtyBinding

class GeneratorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGeneratorActivtyBinding
    private var bitMatrix: BitMatrix? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGeneratorActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.generateQrCodeButton.setOnClickListener {

            generateQRCode()
        }

        binding.fullNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                generateQRCode()
            }

            override fun afterTextChanged(s: Editable?) {


            }


        })

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

                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bmp.setPixel(
                            x,
                            y,
                            if (bitMatrix!!.get(x, y)) Color.BLACK else Color.WHITE
                        )
                    }
                }

                binding.qrCodeImageView.setImageBitmap(bmp)

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