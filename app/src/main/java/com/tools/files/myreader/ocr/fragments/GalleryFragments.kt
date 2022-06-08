package com.tools.files.myreader.ocr.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tools.files.myreader.R
import com.tools.files.myreader.ocr.QueryAllStorage
import com.tools.files.myreader.ocr.gallery.GalleryActivity
import com.tools.files.myreader.ocr.gallery.GalleryActivity.Companion.countImage
import com.tools.files.myreader.ocr.gallery.GalleryAdapter
import com.tools.files.myreader.ocr.model.PictureFacer

class GalleryFragments : Fragment(), GalleryAdapter.onClickItem {
    private var rvListGallery: RecyclerView? = null
    private var imageFolders: ArrayList<PictureFacer>? = null
    private var galleryAdapter: GalleryAdapter? = null
    private val selectedImageList = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery_image, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        rvListGallery = view.findViewById(R.id.rv_image)
        selectedImageList.clear()
        imageFolders = QueryAllStorage.getAllImageGallery(activity)
        galleryAdapter = GalleryAdapter(imageFolders, activity, GalleryActivity.savedImgList)
        galleryAdapter!!.setOnClickItem(this)
        rvListGallery!!.layoutManager = GridLayoutManager(activity, 4)
        rvListGallery!!.adapter = galleryAdapter
        galleryAdapter!!.notifyDataSetChanged()
    }

    override fun onClickItem(position: Int) {
        if (!imageFolders!![position].isSelected) {
            selectImage(position)
        } else {
            unSelectImage(position)
        }
        countImage(GalleryActivity.savedImgList!!)
    }

    fun selectImage(position: Int) {
        if (!selectedImageList.contains(imageFolders!![position].picturePath)) {
            imageFolders!![position].isSelected = true
            selectedImageList.add(imageFolders!![position].picturePath)
            GalleryActivity.savedImgList!!.add(imageFolders!![position].picturePath)
            galleryAdapter!!.notifyDataSetChanged()
        }
    }

    fun unSelectImage(position: Int) {
        for (i in selectedImageList.indices) {
            if (imageFolders!![position].picturePath != null) {
                if (selectedImageList[i] == imageFolders!![position].picturePath) {
                    imageFolders!![position].isSelected = false
                    selectedImageList.removeAt(i)
                    GalleryActivity.savedImgList!!.remove(imageFolders!![position].picturePath)
                    galleryAdapter!!.notifyDataSetChanged()
                }
            }
        }
    }
}