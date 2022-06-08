package com.tools.files.myreader.ocr

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.supportv1.v4.content.ContextCompat
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.isseiaoki.simplecropview.CropImageView
import com.tools.files.myreader.R
import com.tools.files.myreader.ocr.popups.SignatureDialog
import com.tools.files.myreader.ocr.popups.SignatureDialog.onDrawListener
import com.tools.files.myreader.ocr.view.StickerView
import java.io.ByteArrayOutputStream

class EditScannerActivity() : FragmentActivity(), View.OnClickListener {
    private var tvNumberPage: TextView? = null
    private var tvConfirm: TextView? = null
    private var vpListOCR: ViewPager2? = null
    private var imgCancel: ImageView? = null
    private var imgDone: ImageView? = null
    private var imgBackScreen: ImageView? = null
    private var pbLoading: LinearLayout? = null
    private var lnRotate: LinearLayout? = null
    private var lnFlip: LinearLayout? = null
    private var lnCrop: LinearLayout? = null
    private var lnSign: LinearLayout? = null
    private var lnFilter: LinearLayout? = null
    private var lnDelete: LinearLayout? = null
    private var lnBottomBar: LinearLayout? = null
    private var lnBackFilter: LinearLayout? = null
    private var lnListFilter: LinearLayout? = null
    private var rvListFilter: RecyclerView? = null
    private var filterAdapter: FilterAdapter? = null
    private var isSignature = false
    private var isFilter = false
    private var isClickCrop = false
    private var isFlip = true
    private var isRotate = true
    private var mPosition = 0
    var path: String? = null
    private var mType: String? = null
    private var isGallery: String? = null
    private var pageOcrAdapter: PageOcrAdapter? = null
    private var fragments: ArrayList<PreviewFragment>? = null
    private val listFilter: ArrayList<Float?> = ArrayList<Float?>()
    private val mListColor: ArrayList<Int?> = ArrayList<Int?>()
    private var currentChildImage: PreviewFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_scanner)
        instance = this
        initViews()
        initEvent()
    }

    private fun initViews() {
        imgBackScreen = findViewById(R.id.iv_back)
        imgCancel = findViewById(R.id.iv_cancel)
        imgDone = findViewById(R.id.iv_done)
        tvNumberPage = findViewById(R.id.tv_index)
        tvConfirm = findViewById(R.id.iv_confirm)
        vpListOCR = findViewById(R.id.vp_img)
        pbLoading = findViewById(R.id.loading_view)
        lnRotate = findViewById(R.id.ln_rotate)
        lnFlip = findViewById(R.id.ln_flip)
        lnCrop = findViewById(R.id.ln_crop)
        lnSign = findViewById(R.id.ln_sign)
        lnFilter = findViewById(R.id.ln_filter)
        lnDelete = findViewById(R.id.ln_delete)
        lnBottomBar = findViewById(R.id.ln_toolbar)
        lnBackFilter = findViewById(R.id.ln_back_filter)
        rvListFilter = findViewById(R.id.rv_filter)
        lnListFilter = findViewById(R.id.ln_filter_child)
        mType = intent.getStringExtra("type")
        isGallery = intent.getStringExtra("isOpenGallery")
        savedImgList = intent.getStringArrayListExtra("list_data")
        if (mType!!.contains("OCR")) {
            lnSign!!.visibility = View.GONE
            lnFilter!!.visibility = View.GONE
        } else {
            lnSign!!.visibility = View.VISIBLE
            lnFilter!!.visibility = View.VISIBLE
        }
        tvNumberPage!!.text = "1/" + savedImgList!!.size
        initAdapter()
    }

    override fun onResume() {
        super.onResume()
        pbLoading!!.visibility = View.GONE
        initListFilter()
    }

    private fun initListFilter() {
        listFilter.add(0.8f)
        listFilter.add(1.0f)
        listFilter.add(1.2f)
        listFilter.add(1.4f)
        listFilter.add(1.6f)
        listFilter.add(1.8f)
        listFilter.add(2.0f)
        listFilter.add(2.2f)
        listFilter.add(2.4f)
        listFilter.add(2.6f)
        listFilter.add(2.8f)
        listFilter.add(3.0f)
        listFilter.add(3.2f)
        listFilter.add(3.4f)
        mListColor.add(ContextCompat.getColor(this, R.color.color_filter1))
        mListColor.add(ContextCompat.getColor(this, R.color.color_filter2))
        mListColor.add(ContextCompat.getColor(this, R.color.color_filter3))
        mListColor.add(ContextCompat.getColor(this, R.color.color_filter4))
        mListColor.add(ContextCompat.getColor(this, R.color.color_filter5))
        mListColor.add(ContextCompat.getColor(this, R.color.color_filter6))
        mListColor.add(ContextCompat.getColor(this, R.color.color_filter7))
        mListColor.add(ContextCompat.getColor(this, R.color.color_filter8))
        mListColor.add(ContextCompat.getColor(this, R.color.color_filter9))
        mListColor.add(ContextCompat.getColor(this, R.color.color_filter10))
        mListColor.add(ContextCompat.getColor(this, R.color.color_filter11))
        mListColor.add(ContextCompat.getColor(this, R.color.color_filter12))
        mListColor.add(ContextCompat.getColor(this, R.color.color_filter13))
        mListColor.add(ContextCompat.getColor(this, R.color.color_filter14))
        val bm = BitmapFactory.decodeFile(
            savedImgList!![vpListOCR!!.currentItem]
        )
        filterAdapter = FilterAdapter(listFilter, this, bm, mListColor)
        filterAdapter!!.setOnClickItem { position ->
            if (currentChildImage!!.cropImageView != null) {
                val bm = BitmapFactory.decodeFile(
                    savedImgList!![vpListOCR!!.currentItem]
                )
                startFilter(currentChildImage!!.cropImageView!!, bm, listFilter[position] as Float)
            }
        }
        rvListFilter!!.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rvListFilter!!.adapter = filterAdapter
        filterAdapter!!.notifyDataSetChanged()
    }

    fun startFilter(cropImageView: CropImageView, bitmap: Bitmap?, f: Float) {
        val modifiedBitmap = if (bitmap == null) null else BitmapExtKt.`adjustedContrast$default`(
            bitmap,
            f,
            0.0f,
            0,
            6,
            null
        )
        cropImageView.imageBitmap = modifiedBitmap
    }

    private fun initAdapter() {
        fragments = ArrayList()
        fragments!!.clear()
        for (i in savedImgList!!.indices) {
            currentChildImage = PreviewFragment()
            //Bitmap bm = BitmapFactory.decodeFile(savedImgList.get(i));
            currentChildImage!!.setBitmapCrop(savedImgList!![i])
            fragments!!.add(i, currentChildImage!!)
        }
        pageOcrAdapter = PageOcrAdapter(this@EditScannerActivity, fragments!!)
        vpListOCR!!.adapter = pageOcrAdapter
        vpListOCR!!.currentItem = 0
        vpListOCR!!.setPageTransformer(ZoomOutPageTransformer())
        vpListOCR!!.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                mPosition = position
                tvNumberPage!!.text = (position + 1).toString() + "/" + savedImgList!!.size
                currentChildImage = pageOcrAdapter!!.getChildAt(position)
            }
        })
        pageOcrAdapter!!.notifyDataSetChanged()
    }

    private fun initEvent() {
        tvConfirm!!.setOnClickListener(this)
        lnRotate!!.setOnClickListener(this)
        lnFlip!!.setOnClickListener(this)
        lnCrop!!.setOnClickListener(this)
        lnSign!!.setOnClickListener(this)
        lnFilter!!.setOnClickListener(this)
        lnDelete!!.setOnClickListener(this)
        imgDone!!.setOnClickListener(this)
        imgBackScreen!!.setOnClickListener(this)
        lnBackFilter!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v === tvConfirm) {
            pbLoading!!.visibility = View.VISIBLE
            if (mType!!.contains("OCR")) {
                val intent = Intent(this, EditOCRActivity::class.java)
                intent.putExtra("isOpenGallery", isGallery)
                intent.putStringArrayListExtra("list_data", savedImgList)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, PreviewPDFActivity::class.java)
                intent.putStringArrayListExtra("List_image_scanner", savedImgList)
                startActivity(intent)
                finish()
            }
        } else if (v === lnRotate) {
            val bm = BitmapFactory.decodeFile(
                savedImgList!![vpListOCR!!.currentItem]
            )
            if (isRotate) {
                currentChildImage!!.funcRotate(bm, 90f)
            } else {
                currentChildImage!!.funcRotate(bm, 0f)
            }
            isRotate = !isRotate
        } else if (v === lnFlip) {
            val bm = BitmapFactory.decodeFile(
                savedImgList!![vpListOCR!!.currentItem]
            )
            if (isFlip) {
                currentChildImage!!.funcFlip(bm, 180f)
            } else {
                currentChildImage!!.funcFlip(bm, 0f)
            }
            isFlip = !isFlip
        } else if (v === lnCrop) {
            funcEditBottom()
            if (!isClickCrop) {
                currentChildImage!!.startCrop()
                isClickCrop = true
            }
        } else if (v === lnSign) {
            val signatureDialog = SignatureDialog(this)
            signatureDialog.setOnExportListener(object : onDrawListener {
                override fun onDraw(bitmap: Bitmap) {
                    isSignature = true
                    funcEditBottom()
                    currentChildImage!!.funcSignature(bitmap)
                }
            })
            signatureDialog.show()
        } else if (v === lnFilter) {
            funcFilter()
        } else if (v === lnDelete) {
            try {
                if (savedImgList != null) {
                    savedImgList!!.removeAt(vpListOCR!!.currentItem)
                    initAdapter()
                    if (savedImgList!!.size == 0) {
                        finish()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "click_Delete_Exception: " + e.message)
                e.printStackTrace()
            }
        } else if (v === imgBackScreen) {
            funcBackOCR()
        } else if (v === lnBackFilter) {
            funcBackOCR()
        } else if (v === imgCancel) {
            if (currentChildImage != null) {
                currentChildImage!!.cancelCrop()
                funcSelectCrop()
                isClickCrop = false
            }
        } else if (v === imgDone) {
            funcSelectCrop()
            if (isSignature) {
                if (currentChildImage != null && currentChildImage!!.flItem != null) {
                    savedImgList!![mPosition] = ""
                    confirmSignature(currentChildImage!!.flItem!!)
                }
            } else if (isClickCrop) {
                currentChildImage!!.stopCrop()
                pageOcrAdapter!!.notifyDataSetChanged()
                isClickCrop = false
            }
        }
    }

    private fun funcSelectCrop() {
        vpListOCR!!.isUserInputEnabled = true
        imgCancel!!.visibility = View.GONE
        imgDone!!.visibility = View.GONE
        lnListFilter!!.visibility = View.GONE
        tvConfirm!!.visibility = View.VISIBLE
        lnBottomBar!!.visibility = View.VISIBLE
    }

    private fun funcEditBottom() {
        vpListOCR!!.isUserInputEnabled = false
        imgCancel!!.visibility = View.VISIBLE
        imgDone!!.visibility = View.VISIBLE
        tvConfirm!!.visibility = View.GONE
    }

    fun confirmSignature(flPreview: FrameLayout) {
        val childCount = flPreview.childCount
        if (childCount > 0) {
            var i = 0
            while (true) {
                val i2 = i + 1
                val childAt = flPreview.getChildAt(i)
                if (childAt is StickerView) {
                    childAt.setInEdit(false)
                }
                if (i2 < childCount) {
                    i = i2
                } else {
                    return
                }
            }
        }
    }

    private fun funcFilter() {
        vpListOCR!!.isUserInputEnabled = false
        isFilter = true
        val bm = BitmapFactory.decodeFile(savedImgList!![vpListOCR!!.currentItem])
        filterAdapter!!.refreshImage(bm)
        imgCancel!!.visibility = View.VISIBLE
        imgDone!!.visibility = View.VISIBLE
        lnListFilter!!.visibility = View.VISIBLE
        tvConfirm!!.visibility = View.GONE
        lnBottomBar!!.visibility = View.GONE
    }

    override fun onBackPressed() {
        funcBackOCR()
    }

    private fun funcBackOCR() {
        if (lnBottomBar!!.visibility == View.GONE) {
            imgCancel!!.visibility = View.GONE
            imgDone!!.visibility = View.GONE
            lnListFilter!!.visibility = View.GONE
            tvConfirm!!.visibility = View.VISIBLE
            lnBottomBar!!.visibility = View.VISIBLE
            vpListOCR!!.isUserInputEnabled = true
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    companion object {
        private val TAG = EditScannerActivity::class.java.name
        var instance: EditScannerActivity? = null
            private set
        var savedImgList: ArrayList<String?>? = ArrayList()
        fun convert(bitmap: Bitmap): String {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        }
    }
}