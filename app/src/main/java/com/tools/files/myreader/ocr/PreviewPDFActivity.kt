package com.tools.files.myreader.ocr

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tools.files.myreader.ocr.popups.ExportDialog
import com.tejpratapsingh.pdfcreator.utils.PDFUtil
import com.tejpratapsingh.pdfcreator.utils.PDFUtil.PDFUtilListener
import com.tejpratapsingh.pdfcreator.views.basic.PDFImageView
import com.tools.files.myreader.R
import com.tools.files.myreader.activity.MainActivity
import java.io.File

class PreviewPDFActivity : FragmentActivity(), View.OnClickListener {
    private var mListDocument: ArrayList<String>? = ArrayList()
    private val contentViews: MutableList<View> = ArrayList()
    private var rvListPDF: RecyclerView? = null
    private var previewAdapter: PreviewAdapter? = null
    private var tvSave: TextView? = null
    private var tvPagePDF: TextView? = null
    private var tvNameFile: TextView? = null
    private var imgBack: ImageView? = null
    private var lnLoading: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_scanner)
        mListDocument = intent.getStringArrayListExtra("List_image_scanner")
        initViews()
        initEvent()
        initAdapter()
    }

    private fun initViews() {
        rvListPDF = findViewById(R.id.rv_preview_scanner)
        tvSave = findViewById(R.id.tv_save_preview)
        tvNameFile = findViewById(R.id.tv_name_preview)
        imgBack = findViewById(R.id.iv_back)
        tvPagePDF = findViewById(R.id.footer_page_text)
        lnLoading = findViewById(R.id.loading_view)
        tvNameFile!!.text = "preview_" + System.currentTimeMillis() + ".pdf"
    }

    private fun initEvent() {
        tvSave!!.setOnClickListener(this)
        imgBack!!.setOnClickListener(this)
    }

    private fun initAdapter() {
        previewAdapter = PreviewAdapter(mListDocument, this)
        rvListPDF!!.layoutManager = LinearLayoutManager(this)
        rvListPDF!!.adapter = previewAdapter
        previewAdapter!!.notifyDataSetChanged()
        tvPagePDF!!.text = 1.toString() + File.separator + mListDocument!!.size
        rvListPDF!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var lastPos = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val offset = rvListPDF!!.computeHorizontalScrollOffset()
                if (offset % rvListPDF!!.height == 0) {
                    val position =
                        (rvListPDF!!.layoutManager as LinearLayoutManager?)!!.findFirstCompletelyVisibleItemPosition()
                    if (position != -1) {
                        lastPos = position + 1
                    }
                    tvPagePDF!!.text = lastPos.toString() + File.separator + mListDocument!!.size
                }
            }
        })
    }

    override fun onClick(v: View) {
        if (v === tvSave) {
            val exportDialog = ExportDialog(this, tvNameFile!!.text.toString())
            exportDialog.setOnExportListener {
                if (exportDialog.edtNameFile != null) {
                    funcCreateFilePDF(exportDialog.edtNameFile.text.toString())
                } else {
                    funcCreateFilePDF(tvNameFile!!.text.toString())
                }
            }
            exportDialog.show()
        } else if (v === imgBack) {
            finish()
        }
    }

    private fun funcCreateFilePDF(nameFile: String) {
        lnLoading!!.visibility = View.VISIBLE
        val path: String
        path = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                .toString() + "/" + nameFile
        } else {
            getExternalFilesDir(null)!!.path + "/" + nameFile
        }
        for (i in mListDocument!!.indices) {
            val bm = BitmapFactory.decodeFile(mListDocument!![i])
            val pdfImageView = PDFImageView(this@PreviewPDFActivity)
            pdfImageView.setImageBitmap(bm)
            pdfImageView.view.setPadding(20, 12, 20, 12)
            contentViews.add(pdfImageView.view)
        }
        PDFUtil.getInstance().generatePDF(contentViews, path, object : PDFUtilListener {
            override fun pdfGenerationSuccess(savedPDFFile: File) {
                lnLoading!!.visibility = View.GONE
                Toast.makeText(this@PreviewPDFActivity, "Success!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@PreviewPDFActivity, MainActivity::class.java))
                finish()
            }

            override fun pdfGenerationFailure(exception: Exception) {
                lnLoading!!.visibility = View.GONE
                Toast.makeText(this@PreviewPDFActivity, "Create PDF Fail!", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this@PreviewPDFActivity, MainActivity::class.java))
                finish()
            }
        })
    }

    companion object {
        private val TAG = PreviewPDFActivity::class.java.name
    }
}