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
import com.tools.files.myreader.ocr.gallery.FolderAdapter
import com.tools.files.myreader.ocr.gallery.GalleryActivity
import com.tools.files.myreader.ocr.gallery.GalleryActivity.Companion.countImage
import com.tools.files.myreader.ocr.gallery.GalleryAdapter
import com.tools.files.myreader.ocr.model.ImageFolder
import com.tools.files.myreader.ocr.model.PictureFacer

class FolderFragments : Fragment(), GalleryAdapter.onClickItem {
    private var imageFolders: ArrayList<ImageFolder>? = null
    private var folderAdapter: FolderAdapter? = null
    private var rvListGallery: RecyclerView? = null
    private var selectedImageList: ArrayList<String>? = null
    private var mListImage: ArrayList<PictureFacer>? = null
    private var galleryAdapter: GalleryAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_folder_image, container, false)
        rvListGallery = view.findViewById(R.id.rv_folder_image)
        initViews()
        return view
    }

    private fun initViews() {
        mListImage = ArrayList()
        selectedImageList = ArrayList()
        selectedImageList!!.clear()
        mListImage!!.clear()
        if (activity != null) {
            imageFolders = QueryAllStorage.queryAllPicture(activity)
            folderAdapter = FolderAdapter(imageFolders, activity)
            folderAdapter!!.setOnClickItem { position ->
                mListImage =
                    QueryAllStorage.getAllImagesByFolder(activity, imageFolders!![position].path)
                galleryAdapter = GalleryAdapter(mListImage, activity, GalleryActivity.savedImgList)
                galleryAdapter!!.setOnClickItem(this@FolderFragments)
                rvListGallery!!.layoutManager = GridLayoutManager(activity, 4)
                rvListGallery!!.adapter = galleryAdapter
                galleryAdapter!!.notifyDataSetChanged()
            }
            rvListGallery!!.layoutManager = GridLayoutManager(activity, 3)
            rvListGallery!!.adapter = folderAdapter
            folderAdapter!!.notifyDataSetChanged()
        }
    }

    fun onBackFolder(): Boolean {
        return if (mListImage != null && mListImage!!.size > 0) {
            initViews()
            true
        } else {
            false
        }
    }

    override fun onClickItem(position: Int) {
        if (!mListImage!![position].isSelected) {
            selectImage(position)
        } else {
            unSelectImage(position)
        }
        countImage(GalleryActivity.savedImgList!!)
    }

    fun selectImage(position: Int) {
        if (!selectedImageList!!.contains(mListImage!![position].picturePath)) {
            mListImage!![position].isSelected = true
            selectedImageList!!.add(mListImage!![position].picturePath)
            GalleryActivity.savedImgList!!.add(mListImage!![position].picturePath)
            galleryAdapter!!.notifyDataSetChanged()
        }
    }

    fun unSelectImage(position: Int) {
        for (i in selectedImageList!!.indices) {
            if (mListImage!![position].picturePath != null) {
                if (selectedImageList!![i] == mListImage!![position].picturePath) {
                    mListImage!![position].isSelected = false
                    selectedImageList!!.removeAt(i)
                    GalleryActivity.savedImgList!!.remove(mListImage!![position].picturePath)
                    galleryAdapter!!.notifyDataSetChanged()
                }
            }
        }
    }
}