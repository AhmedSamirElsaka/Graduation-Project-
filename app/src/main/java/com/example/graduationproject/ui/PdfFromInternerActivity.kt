package com.example.graduationproject.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.ActivityPdfFromInternerBinding

class PdfFromInternerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPdfFromInternerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfFromInternerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.showOnlinePdf.setOnClickListener {
            var pdfLink = binding.pdfLinkEt.text
            if(!pdfLink.isNullOrEmpty()){

            }else {
                Toast.makeText(applicationContext, "Please Enter Valid Link", Toast.LENGTH_SHORT).show()
            }
        }
    }
}