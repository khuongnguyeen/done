package com.tools.files.myreader.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.all.me.io.helpers.utils.FileUtility.openFile
import com.artifex.solib.a
import com.artifex.sonui.editor.Utilities
import com.tools.files.myreader.R
import com.tools.files.myreader.adapter.CreateFileAdapter
import com.tools.files.myreader.base.BaseFragment
import com.tools.files.myreader.model.FileAssetModel
import java.io.File
import java.util.ArrayList

class CreateNewFileFragment : BaseFragment(), View.OnClickListener {
    private var rcvFragmentNewFileList: RecyclerView? = null
    private var mCreateFileAdapter: CreateFileAdapter? = null
    var mFileArrayList: ArrayList<FileAssetModel>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_file_create, container, false)
    }

    override fun initData() {
        val gridLayoutManager = GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false)
        rcvFragmentNewFileList!!.layoutManager =
            gridLayoutManager // set LayoutManager to RecyclerView
    }

    override fun initView(view: View) {
        rcvFragmentNewFileList = view.findViewById(R.id.rcv_fragment_new_file__list)
        mFileArrayList = ArrayList()
        mFileArrayList!!.add(
            FileAssetModel(
                "spreadsheet-blank.xlsx",
                "xlsx-2007.png",
                "Spreadsheet.xlsx"
            )
        )
        mFileArrayList!!.add(
            FileAssetModel(
                "spreadsheet-office2003.xls",
                "xls-1997.png",
                "2003 spreadsheet.xls",
                "Spreadsheet.xlsx"
            )
        )
        mFileArrayList!!.add(
            FileAssetModel(
                "document-blank.docx",
                "docx-2007.png",
                "Document.docx"
            )
        )
        mFileArrayList!!.add(
            FileAssetModel(
                "document-office2003.doc",
                "doc-1997.png",
                "2003 document.doc",
                "Document.docx"
            )
        )
        mFileArrayList!!.add(
            FileAssetModel(
                "presentation-blank.pptx",
                "pptx-2007.png",
                "Presentation.pptx"
            )
        )
        mFileArrayList!!.add(
            FileAssetModel(
                "presentation-office2003.ppt",
                "ppt-1997.png",
                "2003 presentation.ppt",
                "Presentation.pptx"
            )
        )
        mCreateFileAdapter =
            CreateFileAdapter(mFileArrayList, mContext,
                CreateFileAdapter.onItemClickAssetListener { fileAssetModel ->
                    Utilities.hideKeyboard(mContext)
                    val b2 = a.b(mContext, fileAssetModel.getmPath())
                    if (b2 != null) {
                        val file = File(b2)
                        openFile((mContext as Activity?)!!, file, 1)
                        return@onItemClickAssetListener
                    }
//                    ToastUtil.showToast(mContext, "File error" + fileAssetModel.getmPath())
                })
        rcvFragmentNewFileList!!.setAdapter(mCreateFileAdapter)
    }

    override fun onClick(view: View) {}

    companion object {
        private val TAG = CreateNewFileFragment::class.java.simpleName
    }
}