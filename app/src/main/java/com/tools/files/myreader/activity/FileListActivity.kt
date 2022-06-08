package com.tools.files.myreader.activity

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog.*
import com.all.me.io.helpers.utils.FileUtility
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.tools.files.myreader.R
import com.tools.files.myreader.adapter.RecyclerViewAdapter
import com.tools.files.myreader.adsconfig.LovinBannerAds
import com.tools.files.myreader.adsconfig.LovinInterstitialAds
import com.tools.files.myreader.adsconfig.callbacks.LovinInterstitialOnCallBack
import com.tools.files.myreader.adsconfig.callbacks.LovinNativeCallBack
import com.tools.files.myreader.base.App
import com.tools.files.myreader.base.BaseActivity
import com.tools.files.myreader.ulti.Common.capitalizeString
import com.tools.files.myreader.ulti.Common.showDialogSweet
import com.tools.files.myreader.ulti.Common.sortFile
import kotlinx.android.synthetic.main.fragment_file_list.*
import java.io.File
import java.util.*

class FileListActivity : BaseActivity() {

    private var fileType: String? = null
    private var mFoodList = mutableListOf<Any>()
    private var mSearchList = mutableListOf<Any>()
    lateinit var lovinBannerAds: LovinBannerAds

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_file_list)
        initView()
        val editTextSearch = findViewById<EditText>(R.id.edt_activity_main__search2)
        editTextSearch.addTextChangedListener(textWatcher)

        lovinBannerAds = LovinBannerAds(this)

        // load small native

        lovinBannerAds.loadNative(native_ads_container_layout,
            native_ad_container_2,
            native_loading_layout_2,
            getString(R.string.applovin_small_native_ids),
            true,
            false,
            object : LovinNativeCallBack {
                override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
                    Toast.makeText(this@FileListActivity,"AdLoaded",Toast.LENGTH_SHORT).show()
                }

                override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
                    Toast.makeText(this@FileListActivity,"AdLoadFailed",Toast.LENGTH_SHORT).show()
                }

                override fun onNativeAdClicked(ad: MaxAd) {
                    Toast.makeText(this@FileListActivity,"AdClicked",Toast.LENGTH_SHORT).show()
                }


            })



    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun afterTextChanged(s: Editable) {
            search(s.toString().lowercase(Locale.ROOT))
        }
    }

    private fun search(searchTerm: String) {
        mSearchList.clear()
        for (item in mFoodList) {
            if (item is File) if (item.name.lowercase(Locale.ROOT).contains(searchTerm)) {
                mSearchList.add(item)
            }
        }
        adapter!!.notifyDataSetChanged()
    }

    private fun initView() {
        fileType = intent.getStringExtra("type")
        when (fileType) {
            "all" -> titlt_bar.text = getString(R.string.all_file)
            "xls" -> titlt_bar.text = getString(R.string.ex)
            "ppt" -> titlt_bar.text = getString(R.string.ppint)
            "txt" -> titlt_bar.text = getString(R.string.text1)
            "pdf" -> titlt_bar.text = getString(R.string.pd)
            "Favourite" -> titlt_bar.text = getString(R.string.favou)
            else -> titlt_bar.text = capitalizeString(fileType!!)
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sort_name_a_z -> {
                    sortFile(this@FileListActivity, mSearchList, -1, 0, 1, adapter)
                    true
                }
                R.id.sort_name_z_a -> {
                    sortFile(this@FileListActivity, mSearchList, 1, 0, -1, adapter)
                    true
                }
                R.id.sort_size -> {
                    if (mSearchList.size != 0) {
                        val dumList = mutableListOf<File>()
                        for (i in mSearchList) {
                            if (i is File) dumList.add(i)
                        }
                        dumList.sortWith { row1, row2 ->
                            if (row1.length() < row2.length()) {
                                return@sortWith -1
                            }
                            if (row1.length() == row2.length()) return@sortWith 0
                            1
                        }
                        val k = dumList.size
                        for (i in 0 until k) {
                            if (mSearchList[i] is File) {
                                mSearchList[i] = dumList[0]
                                dumList.removeAt(0)
                            }
                        }
                        adapter!!.notifyDataSetChanged()
                    } else {
                        showDialogSweet(this@FileListActivity, WARNING_TYPE, "No File!")
                    }
                    true
                }
                R.id.sort_date -> {
                    if (mSearchList.size != 0) {
                        val dumList = mutableListOf<File>()
                        for (i in mSearchList) {
                            if (i is File) dumList.add(i)
                        }
                        dumList.sortWith { row1, row2 ->
                            if (row1.lastModified()!! < row2.lastModified()!!) {
                                return@sortWith 1
                            }
                            if (row1.lastModified()!! == row2.lastModified()!!) return@sortWith 0
                            -1
                        }
                        val k = dumList.size
                        for (i in 0 until k) {
                            if (mSearchList[i] is File) {
                                mSearchList[i] = dumList[0]
                                dumList.removeAt(0)
                            }
                        }
                        adapter!!.notifyDataSetChanged()
                    } else {
                        showDialogSweet(this@FileListActivity, WARNING_TYPE, "No File!")
                    }
                    true
                }
                else -> false
            }
        }
        recyclerview.layoutManager = LinearLayoutManager(this)
        loadFileCoroutine(fileType!!)
    }


    private fun loadFileCoroutine(type: String) {
        when (type) {
            "all" -> {
                mFoodList.addAll(FileUtility.mAllFileOffice)
                mSearchList.addAll(FileUtility.mAllFileOffice)
            }
            "pdf" -> {
                mFoodList.addAll(FileUtility.mPdfFiles)
                mSearchList.addAll(FileUtility.mPdfFiles)
            }
            "word" -> {
                mFoodList.addAll(FileUtility.mWordFiles)
                mSearchList.addAll(FileUtility.mWordFiles)
            }
            "xls" -> {
                mFoodList.addAll(FileUtility.mExcelFiles)
                mSearchList.addAll(FileUtility.mExcelFiles)
            }
            "ppt" -> {
                mFoodList.addAll(FileUtility.mPowerPointFiles)
                mSearchList.addAll(FileUtility.mPowerPointFiles)
            }
            "txt" -> {
                mFoodList.addAll(FileUtility.mListTxtFile)
                mSearchList.addAll(FileUtility.mListTxtFile)
            }

        }
        progressBar!!.visibility = View.GONE
        if (mSearchList.size == 0 || mSearchList.isEmpty()) {
            tv_text!!.text = "No files!"
            tv_text!!.visibility = View.VISIBLE
        } else {
            tv_text!!.visibility = View.GONE
            val dumList = mutableListOf<File>()
            for (i in mSearchList) {
                if (i is File) dumList.add(i)
            }
            dumList.sortWith { row1, row2 ->
                if (row1.lastModified() < row2.lastModified()) {
                    return@sortWith 1
                }
                if (row1.lastModified() == row2.lastModified()) return@sortWith 0
                -1
            }
            val k = dumList.size
            for (i in 0 until k) {
                if (mSearchList[i] is File) {
                    mSearchList[i] = dumList[0]
                    dumList.removeAt(0)
                }
            }
            adapter = RecyclerViewAdapter(mSearchList, this@FileListActivity, this@FileListActivity)
            recyclerview.adapter = adapter
        }
    }
}