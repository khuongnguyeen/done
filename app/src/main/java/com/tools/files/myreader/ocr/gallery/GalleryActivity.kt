package com.tools.files.myreader.ocr.gallery

import android.content.Intent
import android.os.Bundle
import android.supportv1.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.tools.files.myreader.R
import com.tools.files.myreader.ocr.EditScannerActivity
import com.tools.files.myreader.ocr.GalleryPagerAdapter
import com.tools.files.myreader.ocr.fragments.FolderFragments
import com.tools.files.myreader.ocr.fragments.GalleryFragments

class GalleryActivity : FragmentActivity(), View.OnClickListener {
    private var imgBack: ImageView? = null
    private var vpListGallery: ViewPager? = null
    private var galleryPagerAdapter: GalleryPagerAdapter? = null
    private var mFragmentArrayList: ArrayList<Fragment>? = null
    private var galleryFragments: GalleryFragments? = null
    private var folderFragments: FolderFragments? = null
    private var cvImage: CardView? = null
    private var cvAlbum: CardView? = null
    private var tvImage: TextView? = null
    private var tvAlbum: TextView? = null
    private var type: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_image)
        initViews()
        initEvent()
        initAdapter()
    }

    private fun initViews() {
        type = intent.getStringExtra("type")
        vpListGallery = findViewById(R.id.vp_image)
        tvConfirm = findViewById(R.id.tv_confirm)
        imgBack = findViewById(R.id.iv_back)
        cvImage = findViewById(R.id.cv_image)
        cvAlbum = findViewById(R.id.cv_album)
        tvImage = findViewById(R.id.tv_image)
        tvAlbum = findViewById(R.id.tv_album)
    }

    private fun initEvent() {
        imgBack!!.setOnClickListener(this)
        tvConfirm!!.setOnClickListener(this)
        cvImage!!.setOnClickListener(this)
        cvAlbum!!.setOnClickListener(this)
    }

    private fun initAdapter() {
        galleryFragments = GalleryFragments()
        folderFragments = FolderFragments()
        mFragmentArrayList = ArrayList()
        mFragmentArrayList!!.add(galleryFragments!!)
        mFragmentArrayList!!.add(folderFragments!!)
        savedImgList!!.clear()
        galleryPagerAdapter = GalleryPagerAdapter(supportFragmentManager, mFragmentArrayList)
        vpListGallery!!.adapter = galleryPagerAdapter
        vpListGallery!!.offscreenPageLimit = 2
        vpListGallery!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                funcScroll(position, false)
            }

            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
        tvConfirm!!.text = resources.getString(R.string.confirm_x)
        funcCountSelect()
    }

    private fun funcScroll(position: Int, isScroll: Boolean) {
        when (position) {
            0 -> {
                if (isScroll) vpListGallery!!.currentItem = 0
                cvImage!!.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
                tvImage!!.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                cvAlbum!!.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
                tvAlbum!!.setTextColor(ContextCompat.getColor(this, R.color.colorGray))
            }
            1 -> {
                if (isScroll) vpListGallery!!.currentItem = 1
                cvImage!!.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
                tvImage!!.setTextColor(ContextCompat.getColor(this, R.color.colorGray))
                cvAlbum!!.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
                tvAlbum!!.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            }
        }
    }

    override fun onClick(v: View) {
        if (v === imgBack) {
            funcBackScreen()
        } else if (v === tvConfirm) {
            if (type!!.contains("OCR")) {
                val intent = Intent(this, EditScannerActivity::class.java)
                intent.putExtra("type", "OCR")
                intent.putExtra("isOpenGallery", "true")
                intent.putStringArrayListExtra("list_data", savedImgList)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, EditScannerActivity::class.java)
                intent.putExtra("type", "Scanner")
                intent.putExtra("isOpenGallery", "true")
                intent.putStringArrayListExtra("list_data", savedImgList)
                startActivity(intent)
                finish()
            }
        } else if (v === cvImage) {
            funcScroll(0, true)
        } else if (v === cvAlbum) {
            funcScroll(1, true)
        }
    }

    override fun onBackPressed() {
        funcBackScreen()
    }

    private fun funcBackScreen() {
        if (vpListGallery!!.currentItem == 0) {
            finish()
        } else {
            if (folderFragments != null && folderFragments!!.isVisible) {
                if (!folderFragments!!.onBackFolder()) {
                    finish()
                }
            } else {
                finish()
            }
        }
    }

    companion object {
        private var tvConfirm: TextView? = null
        @JvmField
        var savedImgList: ArrayList<String?>? = ArrayList<String?>()
        private fun funcCountSelect() {
            if (savedImgList != null) {
                if (savedImgList!!.size > 0) {
                    tvConfirm!!.isEnabled = true
                    tvConfirm!!.setBackgroundResource(R.drawable.bg_enable)
                } else {
                    tvConfirm!!.isEnabled = false
                    tvConfirm!!.setBackgroundResource(R.drawable.bg_disable)
                }
            } else {
                tvConfirm!!.isEnabled = false
                tvConfirm!!.setBackgroundResource(R.drawable.bg_disable)
            }
        }

        @JvmStatic
        fun countImage(listImage: ArrayList<String?>) {
            tvConfirm!!.text = "Confirm " + listImage.size
            funcCountSelect()
        }
    }
}