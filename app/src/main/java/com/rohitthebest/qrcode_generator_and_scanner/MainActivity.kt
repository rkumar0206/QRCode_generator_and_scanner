package com.rohitthebest.qrcode_generator_and_scanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rohitthebest.qrcode_generator_and_scanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scanBtn.setOnClickListener(this)
        binding.generateBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            binding.scanBtn.id -> {

                val intent = Intent(applicationContext, ScannerActivity::class.java)
                startActivity(intent)
            }

            binding.generateBtn.id -> {

                val intent = Intent(applicationContext, GeneratorActivity::class.java)
                startActivity(intent)
            }
        }

    }
}