package com.tools.files.myreader.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.tools.files.myreader.R
import com.tools.files.myreader.model.ImagePage
import com.tools.files.myreader.ulti.Action
import com.tools.files.myreader.ulti.ConstantSPKey
import com.tools.files.myreader.ulti.PDFTools
import com.tools.files.myreader.ulti.Common
import kotlinx.android.synthetic.main.progress_view.progress_vieww

class ConvertPdf : AppCompatActivity() {
    lateinit var path: String
    lateinit var key: String
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert_pdf_to_image)
        setBackground()
        sharedPreferences =
            getSharedPreferences(ConstantSPKey.ACTIVITY_SETTING_KEY, Context.MODE_PRIVATE)
        getData()
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    val listener = SharedPreferences.OnSharedPreferenceChangeListener { p0, p1 ->
        if (p1.equals(ConstantSPKey.VALUE_COLOR) || p1.equals(ConstantSPKey.NIGHT_MODE)) {
            //  isActivity=true
            //  setBackground()
        }
    }

    fun getData() {
        try {
            path = intent.getStringExtra("path").toString()
        } catch (e: Exception) {
            Log.d("exxxe", "Error " + e.message)
        }
        key = intent.getStringExtra("key").toString()
        when (key) {
            ConstantSPKey.KEY_PDF_TO_IMAGE -> {
                val pdfTools = PDFTools()
                pdfTools.javaClass
                pdfTools.PdfToImage(this, path, progress_vieww).execute()
            }

            Action.IMG_TO_PDF -> {
                val bd = intent.getBundleExtra(Action.IT_BD)
                val list = bd?.getSerializable(Action.BD_LST) as ArrayList<ImagePage>
                val obj = bd.getString(Action.BD_NN)
                val pdfTools = PDFTools()
                pdfTools.javaClass
                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                pdfTools.SaveOrganizedPages(
                    this, list,
                    obj!!,
                    progress_vieww
                ).execute()
            }
        }
    }

    override fun onBackPressed() {}

    private fun setBackground() {
        sharedPreferences =
            getSharedPreferences(ConstantSPKey.ACTIVITY_SETTING_KEY, Context.MODE_PRIVATE)
    }
}




