package com.tools.files.myreader.ocr

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.tools.files.myreader.ocr.EditScannerActivity.Companion.instance
import com.tools.files.myreader.ocr.view.StickerView
import com.tools.files.myreader.ocr.view.StickerView.OperationListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.isseiaoki.simplecropview.CropImageView
import com.isseiaoki.simplecropview.callback.CropCallback
import com.tools.files.myreader.R

class PreviewFragment : Fragment() {
    var flItem: FrameLayout? = null
    var cropImageView: CropImageView? = null
    var savedBitmap: Bitmap? = null
        private set
    private var mPathFile: String? = null
    private var origBitmap: Bitmap? = null
    private var actualCrop: RectF? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_image, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        flItem = view.findViewById(R.id.fr_preview_item)
        cropImageView = view.findViewById(R.id.cropImageView)
        cropImageView!!.setCropEnabled(false)
        //cropImageView.setImageBitmap(modifiedBitmap);
        Glide.with(requireActivity())
            .asBitmap()
            .load(mPathFile)
            .error(R.color.gray)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    cropImageView!!.setImageBitmap(resource)
                    savedBitmap = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
        cropImageView!!.setInitialFrameScale(1.0f)
        cropImageView!!.setHandleShadowEnabled(false)
        cropImageView!!.setFrameColor(Color.TRANSPARENT)
        cropImageView!!.setHandleColor(Color.TRANSPARENT)
        actualCrop = cropImageView!!.actualCropRect
    }

    fun setBitmapCrop(pathFile: String?) {
        mPathFile = pathFile
    }

    fun funcRotate(bitmap: Bitmap?, flip: Float) {
        savedBitmap = BitmapExtKt.rotate(bitmap, flip)
        origBitmap = savedBitmap
        if (cropImageView != null) {
            cropImageView!!.imageBitmap = savedBitmap
        }
    }

    fun funcFlip(bitmap: Bitmap?, flip: Float) {
        savedBitmap = BitmapExtKt.rotate(bitmap, flip)
        origBitmap = savedBitmap
        if (cropImageView != null) {
            cropImageView!!.imageBitmap = savedBitmap
        }
    }

    fun startCrop() {
        if (cropImageView != null) {
            cropImageView!!.setCropEnabled(true)
            cropImageView!!.setOverlayColor(Color.parseColor("#60FFFFFF"))
            cropImageView!!.setFrameColor(Color.parseColor("#00FFF0"))
            cropImageView!!.setHandleColor(Color.parseColor("#00FFF0"))
        }
    }

    fun stopCrop() {
        try {
            if (cropImageView != null) {
                cropImageView!!.setCropEnabled(false)
                cropImageView!!.cropAsync(object : CropCallback {
                    override fun onSuccess(cropped: Bitmap) {
                        Log.e(TAG, "onSuccess_stopCrop: $cropped")
                        savedBitmap = cropped
                        cropImageView!!.imageBitmap = cropped
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, "onError_stopCrop: " + e.message)
                        e.printStackTrace()
                    }
                })
                cropImageView!!.setInitialFrameScale(1.0f)
                cropImageView!!.setHandleShadowEnabled(false)
                cropImageView!!.setFrameColor(Color.TRANSPARENT)
                cropImageView!!.setHandleColor(Color.TRANSPARENT)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancelCrop() {
        cropImageView!!.setFrameColor(Color.TRANSPARENT)
        cropImageView!!.setOverlayColor(Color.TRANSPARENT)
        cropImageView!!.setHandleColor(Color.TRANSPARENT)
        cropImageView!!.setCropEnabled(false)
    }

    fun funcSignature(bitmap: Bitmap?) {
        savedBitmap = bitmap
        origBitmap = savedBitmap
        val stickerView = StickerView(instance)
        stickerView.setBitmap(bitmap)
        stickerView.setOperationListener(object : OperationListener {
            override fun onDeleteClick() {
                if (flItem != null) flItem!!.removeView(stickerView)
            }

            override fun onEdit(stickerView: StickerView) {}
            override fun onTop(stickerView: StickerView) {}
        })
        if (flItem != null) flItem!!.addView(stickerView)
    }

    companion object {
        private val TAG = PreviewFragment::class.java.name
    }
}