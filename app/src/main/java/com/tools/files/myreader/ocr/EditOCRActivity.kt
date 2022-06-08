package com.tools.files.myreader.ocr

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.tools.files.myreader.R
import com.tools.files.myreader.ocr.general.ControlUtils.copyToClipboard
import com.tools.files.myreader.ocr.general.ControlUtils.funcCreateFileOCR
import com.tools.files.myreader.ocr.general.ControlUtils.inspectFromBitmap
import com.tools.files.myreader.ocr.general.ControlUtils.shareText
import com.tools.files.myreader.ocr.general.ControlUtils.showKeyboard
import com.tools.files.myreader.ulti.Utils.dateFormatterMili
import java.io.File

class EditOCRActivity : FragmentActivity(), View.OnClickListener {
    private var imgBack: ImageView? = null
    private var tvFinish: TextView? = null
    private var tvSave: TextView? = null
    private var tvIndicator: TextView? = null
    private var viewPager2: ViewPager? = null
    private var lnCopy: LinearLayout? = null
    private var lnEdit: LinearLayout? = null
    private var lnShare: LinearLayout? = null
    private var currentPosition = -1
    private var isGallery: String? = null
    private var editOCRAdapter: EditOCRAdapter? = null
    private var savedImgList: ArrayList<String?>? = null
    private val listPareOCR: ArrayList<String?> = ArrayList<String?>()
    private val newListEditOCR: ArrayList<String?> = ArrayList<String?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_ocr)
        initViews()
        initEvent()
    }

    private fun initViews() {
        instance = this
        imgBack = findViewById(R.id.iv_back)
        tvFinish = findViewById(R.id.tv_finish)
        tvSave = findViewById(R.id.tv_save)
        tvIndicator = findViewById(R.id.tv_indicator)
        viewPager2 = findViewById(R.id.vp_result)
        lnCopy = findViewById(R.id.ln_copy)
        lnEdit = findViewById(R.id.ln_edit)
        lnShare = findViewById(R.id.ln_share)
        savedImgList = ArrayList<String?>()
        savedImgList!!.clear()
        savedImgList = intent.getStringArrayListExtra("list_data")
        isGallery = intent.getStringExtra("isOpenGallery")
        initAdapterOCR(savedImgList, isGallery)
    }

    private fun initAdapterOCR(mListOCR: ArrayList<String?>?, isGallery: String?) {
        tvIndicator!!.text = 1.toString() + File.separator + mListOCR!!.size
        viewPager2!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                tvIndicator!!.text = (position + 1).toString() + File.separator + mListOCR.size
                currentPosition = position
            }

            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
        for (i in savedImgList!!.indices) {
            val bm = BitmapFactory.decodeFile(savedImgList!![i])
            runTextRecognition(bm)
        }
    }

    private fun runTextRecognition(decodeByteArray: Bitmap) {
        listPareOCR.add(inspectFromBitmap(this, decodeByteArray))
        initAdapter(listPareOCR)
    }

    private fun initEvent() {
        imgBack!!.setOnClickListener(this)
        tvFinish!!.setOnClickListener(this)
        tvSave!!.setOnClickListener(this)
        lnCopy!!.setOnClickListener(this)
        lnEdit!!.setOnClickListener(this)
        lnShare!!.setOnClickListener(this)
    }

    private fun initAdapter(listOCR: ArrayList<String?>) {
        editOCRAdapter = EditOCRAdapter(this, listOCR)
        viewPager2!!.currentItem = 0
        viewPager2!!.adapter = editOCRAdapter
    }

    override fun onClick(v: View) {
        if (v === imgBack) {
            finish()
        } else if (v === tvFinish) {
            val fileName = "NewOCR" + dateFormatterMili(System.currentTimeMillis()) + ".txt"
            for (i in listPareOCR.indices) {
                if (editOCRAdapter!!.pathOCR == null) {
                    newListEditOCR.add(listPareOCR[i])
                } else newListEditOCR.add(i, editOCRAdapter!!.pathOCR)
            }
            funcCreateFileOCR(this@EditOCRActivity, fileName, newListEditOCR)
        } else if (v === tvSave) {
        } else if (v === lnCopy) {
            copyToClipboard(this@EditOCRActivity, listPareOCR[viewPager2!!.currentItem])
            editOCRAdapter!!.notifyDataSetChanged()
        } else if (v === lnEdit) {
            if (editOCRAdapter != null) {
                if (editOCRAdapter!!.editText != null) {
                    editOCRAdapter!!.editText.isFocusable = true
                    editOCRAdapter!!.editText.requestFocus()
                    showKeyboard(this)
                    editOCRAdapter!!.notifyDataSetChanged()
                }
            }
        } else if (v === lnShare) {
            shareText(this, listPareOCR[viewPager2!!.currentItem])
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    companion object {
        var instance: EditOCRActivity? = null
            private set
    }
}