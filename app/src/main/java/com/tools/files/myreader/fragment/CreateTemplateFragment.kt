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

class CreateTemplateFragment : BaseFragment(), View.OnClickListener {
    private var rcvFragmentTemplateList: RecyclerView? = null
    private var mCreateFileAdapter: CreateFileAdapter? = null
    var mFileArrayList: ArrayList<FileAssetModel>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_template_create, container, false)
    }

    override fun initData() {
        mFileArrayList = ArrayList()
        mFileArrayList!!.add(
            FileAssetModel(
                "template-docx-a.docx",
                "template-docx-a.png",
                "Resume.docx"
            )
        )
        mFileArrayList!!.add(
            FileAssetModel(
                "template-docx-b.docx",
                "template-docx-b.png",
                "Report II.docx"
            )
        )
        mFileArrayList!!.add(
            FileAssetModel(
                "template-pptx-a.pptx",
                "template-pptx-a.png",
                "Dark II.pptx"
            )
        )
        mFileArrayList!!.add(
            FileAssetModel(
                "template-pptx-b.pptx",
                "template-pptx-b.png",
                "Light II.pptx"
            )
        )
        mFileArrayList!!.add(
            FileAssetModel(
                "template-xlsx-a.xlsx",
                "template-xlsx-a.png",
                "Expenses II.xlsx"
            )
        )
        mFileArrayList!!.add(
            FileAssetModel(
                "template-xlsx-b.xlsx",
                "template-xlsx-b.png",
                "Invoice.xlsx"
            )
        )
        mFileArrayList!!.add(
            FileAssetModel(
                "document-letter.docx",
                "document-letter.png",
                "Letter.docx"
            )
        )
        mFileArrayList!!.add(
            FileAssetModel(
                "document-report.docx",
                "document-report.png",
                "Report I.docx"
            )
        )
        mFileArrayList!!.add(
            FileAssetModel(
                "presentation-dark-amber.pptx",
                "presentation-dark-amber.png",
                "Dark I.pptx"
            )
        )
        mFileArrayList!!.add(
            FileAssetModel(
                "presentation-light-bubbles.pptx",
                "presentation-light-bubbles.png",
                "Light I.pptx"
            )
        )
        mFileArrayList!!.add(
            FileAssetModel(
                "spreadsheet-chart-data.xlsx",
                "spreadsheet-chart-data.png",
                "Chart.xlsx"
            )
        )
        mFileArrayList!!.add(
            FileAssetModel(
                "spreadsheet-expense-budget.xlsx",
                "spreadsheet-expense-budget.png",
                "Expenses I.xlsx"
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
        rcvFragmentTemplateList!!.adapter = mCreateFileAdapter
    }

    override fun initView(view: View) {
        rcvFragmentTemplateList = view.findViewById(R.id.rcv_fragment_template__list)
        val gridLayoutManager = GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false)
        rcvFragmentTemplateList!!.layoutManager = gridLayoutManager // set LayoutManager to RecyclerView
    }
    override fun onClick(view: View) {}


    companion object {
        private val TAG = CreateTemplateFragment::class.java.simpleName
    }
}