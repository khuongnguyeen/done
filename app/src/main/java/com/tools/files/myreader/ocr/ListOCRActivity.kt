package com.tools.files.myreader.ocr

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.all.me.io.helpers.utils.StorageUtils
import com.tools.files.myreader.R
import com.tools.files.myreader.adapter.RecyclerViewAdapter
import com.tools.files.myreader.base.BaseActivity
import com.tools.files.myreader.interfaces.ItemFileClickListener
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import kotlin.collections.ArrayList

class ListOCRActivity : BaseActivity(), View.OnClickListener, ItemFileClickListener {
    private var rcvFragmentFileList: RecyclerView? = null
    private var mFileFragmentAdapter: RecyclerViewAdapter? = null
    private var tvEmpty: TextView? = null
    private var mFileArrayList = ArrayList<Any?>()
    private var imgBack: ImageView? = null
    private var storageUtil: StorageUtils? = null
    private var mDisposable: Disposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_ocr)
        initViews()
        initEvent()
        initAdapter()
    }

    private fun initViews() {
        rcvFragmentFileList = findViewById(R.id.rv_ocr_item)
        imgBack = findViewById(R.id.iv_back)
        tvEmpty = findViewById<TextView>(R.id.tv_empty)
        storageUtil = StorageUtils(this)
    }

    private fun initEvent() {
        imgBack!!.setOnClickListener(this)
    }

    private fun initAdapter() {
        Observable.create(ObservableOnSubscribe<ArrayList<File>> { emitter ->
            if (QueryAllStorage.queryAllOCR(this@ListOCRActivity) != null)
                mFileArrayList.addAll(QueryAllStorage.queryAllOCR(this@ListOCRActivity))
            emitter.onNext(mFileArrayList as ArrayList<File>)
            emitter.onComplete()
        })
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ArrayList<File>> {
                override fun onSubscribe(d: Disposable) {
                    mDisposable = d
                }

                override fun onNext(files: ArrayList<File>) {
                    if (mFileArrayList.size > 0) {
                        tvEmpty?.visibility = View.GONE
                    } else {
                        tvEmpty?.visibility = View.VISIBLE
                    }

                    mFileFragmentAdapter = RecyclerViewAdapter(mFileArrayList as MutableList<Any>, this@ListOCRActivity,this@ListOCRActivity)

                    rcvFragmentFileList!!.layoutManager = LinearLayoutManager(this@ListOCRActivity)
                    rcvFragmentFileList!!.adapter = mFileFragmentAdapter
                    mFileFragmentAdapter!!.notifyDataSetChanged()
                    //mDisposable.dispose();
                }

                override fun onError(e: Throwable) {
                    tvEmpty?.setVisibility(View.VISIBLE)
                    e.printStackTrace()
                }

                override fun onComplete() {
                    // TODO
                }
            })
    }

    override fun onClick(v: View) {
        if (v === imgBack) {
            finish()
        }
    }

}