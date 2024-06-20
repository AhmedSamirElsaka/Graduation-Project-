package com.example.graduationproject.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.ActivityMainBinding
import com.example.graduationproject.ui.util.saveTo


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.onlinePdf.setOnClickListener {

            startActivity(
                Intent(baseContext , PdfFromInternerActivity::class.java)
            )
//
//            binding.pdfView.statusListener = object : PdfRendererView.StatusCallBack {
//                override fun onPdfLoadStart() {
//                    Log.i("statusCallBack","onPdfLoadStart")
//                }
//                override fun onPdfLoadProgress(
//                    progress: Int,
//                    downloadedBytes: Long,
//                    totalBytes: Long?
//                ) {
//                    //Download is in progress
//                }
//
//                override fun onPdfLoadSuccess(absolutePath: String) {
//                    Log.i("statusCallBack","onPdfLoadSuccess")
////                    binding.pdfView.recyclerView.scrollToPosition(1)
//                    binding.pdfView.post {
//                        binding.pdfView.recyclerView.scrollToPosition(
//                            1
//                        )
//                    }
//
//                }
//
//                override fun onError(error: Throwable) {
//                    Log.i("statusCallBack","onError")
//                }
//
//                override fun onPageChanged(currentPage: Int, totalPage: Int) {
//                    //Page change. Not require
//                }
//            }
//
//            launchPdfFromUrl(large_pdf)


        }

        binding.pickPdfButton.setOnClickListener {
            launchFilePicker()
        }

        binding.fromAssets.setOnClickListener {
            launchPdfFromAssets("quote.pdf")
        }


    }

    private fun launchPdfFromUrl(url: String) {

        startActivity(
            PdfViewerActivity.launchPdfFromUrl(
                context = this,
                pdfUrl = url,
                pdfTitle = "PDF Title",
                saveTo = saveTo.ASK_EVERYTIME,
                enableDownload = true
            )
        )
    }

    private val filePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedFileUri = result.data?.data
            selectedFileUri?.let { uri ->
                launchPdfFromUri(uri)
            }
        }
    }

    fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.run { getColumnIndex(OpenableColumns.DISPLAY_NAME) })
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result.substring(0, result.length - 4);
    }

//    private fun setPdfTitleTvText(fileName:String):String{
//        var pdfFileName = fileName.substring(0, fileName.length - 4);
//        return pdfFileName
//    }
    private fun launchFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        filePicker.launch(intent)
    }

    private fun launchPdfFromUri(uri: Uri) {
        var pdfTitle = getFileName(uri!!)

        startActivity(
            PdfViewerActivity.launchPdfFromPath(
                context = this, path = uri.toString(),
                pdfTitle = pdfTitle, saveTo = saveTo.ASK_EVERYTIME,  fromAssets = false)
        )
    }

    private fun launchPdfFromAssets(uri: String) {
//        var pdfTitle = getFileName(Uri(uri!!))
        startActivity(
            PdfViewerActivity.launchPdfFromPath(
                context = this, path = uri,
                pdfTitle = "Test Pdf From Assets", saveTo = saveTo.ASK_EVERYTIME,  fromAssets = true)
        )
    }


}