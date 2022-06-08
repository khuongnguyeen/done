package com.tools.files.myreader.ocr

import android.content.Context
import android.graphics.Bitmap
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.isseiaoki.simplecropview.CropImageView

class PageOcrAdapter(
    fragmentActivity: FragmentActivity,
    private val mListFragments: ArrayList<PreviewFragment>
) : FragmentStateAdapter(fragmentActivity) {
    private var mBitmap: Bitmap? = null
    var flItem: FrameLayout? = null
    private var data: ArrayList<String>? = null
    private val mContext: Context? = null
    private var onCLickItem1: onCLickItem? = null
    fun setOnCLickItem(onCLickItem: onCLickItem?) {
        this.onCLickItem1 = onCLickItem
    }

    fun addFragment(fragment: PreviewFragment) {
        mListFragments.add(fragment)
    }

    fun updateBitmap(bitmap: Bitmap?) {
        mBitmap = bitmap
    }

    fun updateImage(listUpdate: ArrayList<String>?) {
        data = listUpdate
        notifyDataSetChanged()
    }

    override fun createFragment(position: Int): PreviewFragment {
        return mListFragments[position]
    }

    fun getChildAt(i: Int): PreviewFragment {
        return mListFragments[i]
    }

    override fun getItemCount(): Int {
        return mListFragments.size
    }

    interface onCLickItem {
        fun onCLick(view: CropImageView?, position: Int)
    }
}