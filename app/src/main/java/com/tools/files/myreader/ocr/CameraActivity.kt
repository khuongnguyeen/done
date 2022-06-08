package com.tools.files.myreader.ocr

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tools.files.myreader.ocr.fragments.TabOCRFragment
import com.tools.files.myreader.ocr.fragments.TabScannerFragment
import com.tools.files.myreader.ocr.gallery.GalleryActivity
import com.tools.files.myreader.ocr.general.ControlUtils.saveImage
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraLogger
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Flash
import com.tools.files.myreader.R
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.ByteArrayOutputStream

class CameraActivity : FragmentActivity(), View.OnClickListener {
    private var imgBackExit: ImageView? = null
    private var imgFlash: ImageView? = null
    private var imgListImage: ImageView? = null
    private var imgTakeCamera: ImageView? = null
    private var imgSaveFile: ImageView? = null
    private var cameraView: CameraView? = null
    private var galleryPagerAdapter: GalleryPagerAdapter? = null
    private var mFragmentArrayList: ArrayList<Fragment>? = null
    private var tabOCRFragment: TabOCRFragment? = null
    private var tabScannerFragment: TabScannerFragment? = null
    private var tvNext: TextView? = null
    private var tvCount: TextView? = null
    private var imgCount = 0
    private var isClickFlash = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        initViews()
        initEvent()
    }

    private fun initViews() {
        imgBackExit = findViewById(R.id.iv_back)
        imgFlash = findViewById(R.id.iv_flash)
        imgListImage = findViewById(R.id.iv_img_list)
        imgTakeCamera = findViewById(R.id.iv_take_pic)
        imgSaveFile = findViewById(R.id.iv_saved_file)
        tvNext = findViewById(R.id.tv_next)
        tvCount = findViewById(R.id.tv_img_count)
        cameraView = findViewById(R.id.camera)
        tabOCRFragment = TabOCRFragment()
        tabScannerFragment = TabScannerFragment()
        mFragmentArrayList = ArrayList()
        mFragmentArrayList!!.add(tabOCRFragment!!)
        mFragmentArrayList!!.add(tabScannerFragment!!)
        galleryPagerAdapter = GalleryPagerAdapter(supportFragmentManager, mFragmentArrayList)
//        viewPager2!!.adapter = galleryPagerAdapter

//        viewPager2!!.currentItem = 0
//        viewPager2!!.offscreenPageLimit = 2
        listBitmapOCR.clear()
        allImageSelect!!.clear()
        cameraView!!.setLifecycleOwner(this)
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE)
        cameraView!!.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                imgCount += 1
                val decodeByteArray =
                    BitmapFactory.decodeByteArray(result.data, 0, result.data.size)
                imgListImage!!.setImageBitmap(decodeByteArray)
                imgListImage!!.isEnabled = false
                listBitmapOCR.add(decodeByteArray)
                tvCount!!.visibility = View.VISIBLE
                tvCount!!.text = imgCount.toString()
                allImageSelect!!.add(saveImage(this@CameraActivity, result.data))
            }
        })
    }

    fun BitMapToString(bitmap: Bitmap): String {
        val byteArray = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray)
        val b = byteArray.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun initEvent() {
        imgBackExit!!.setOnClickListener(this)
        imgFlash!!.setOnClickListener(this)
        imgListImage!!.setOnClickListener(this)
        imgTakeCamera!!.setOnClickListener(this)
        imgSaveFile!!.setOnClickListener(this)
        tvNext!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v === imgBackExit) {
            finish()
        } else if (v === imgFlash) {
            if (cameraView != null) {
                if (isClickFlash) {
                    cameraView!!.flash = Flash.TORCH
                    imgFlash!!.setImageResource(R.drawable.ic_flash_off)
                } else {
                    cameraView!!.flash = Flash.OFF
                    imgFlash!!.setImageResource(R.drawable.ic_flash_on)
                }
                isClickFlash = !isClickFlash
            }
        } else if (v === imgListImage) {
            val intent = Intent(this, GalleryActivity::class.java)
            val checkedId = radio_group.checkedRadioButtonId
            if (checkedId == R.id.btn_ocr)  {
                intent.putExtra("type", "OCR")
            } else {
                intent.putExtra("type", "Scanner")
            }
            startActivity(intent)
            finish()
        } else if (v === imgTakeCamera) {
            cameraView!!.takePicture()
        } else if (v === imgSaveFile) {
            val intent = Intent(this, ListOCRActivity::class.java)
            startActivity(intent)
            finish()
        } else if (v === tvNext) {
            if (allImageSelect != null && allImageSelect!!.size > 0) {

                val checkedId = radio_group.checkedRadioButtonId
                if (checkedId == R.id.btn_ocr) {
                    val intent = Intent(this, EditScannerActivity::class.java)
                    intent.putExtra("type", "OCR")
                    intent.putExtra("isOpenGallery", "false")
                    intent.putStringArrayListExtra("list_data", allImageSelect)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, EditScannerActivity::class.java)
                    intent.putExtra("type", "Scanner")
                    intent.putExtra("isOpenGallery", "false")
                    intent.putStringArrayListExtra("list_data", allImageSelect)
                    startActivity(intent)
                }
                finish()
            } else {
                Toast.makeText(this, "Please choose a Photo!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        var allImageSelect: ArrayList<String?>? = ArrayList<String?>()
        var listBitmapOCR: ArrayList<Bitmap?> = ArrayList<Bitmap?>()
    }
}