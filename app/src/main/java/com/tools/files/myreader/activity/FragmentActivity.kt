package com.tools.files.myreader.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.all.me.io.helpers.utils.StorageUtils
import com.google.android.gms.ads.nativead.NativeAd
import com.tools.files.myreader.R
import com.tools.files.myreader.adapter.RecyclerViewAdapter
import com.tools.files.myreader.base.App
import com.tools.files.myreader.base.BaseActivity
import com.tools.files.myreader.fragment.ListFavouriteFragment
import com.tools.files.myreader.interfaces.ItemFileClickListener
import com.tools.files.myreader.ulti.Common
import kotlinx.android.synthetic.main.fragment_file_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class FragmentActivity : BaseActivity() {

    var unifiedNativeAd: NativeAd? = null
    val fileInfoBeans = mutableListOf<Any>()
    var rc: RecyclerView? = null
    var constraintLayout: ConstraintLayout? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_file_favourite)
        rc = findViewById(R.id.recyclerview)
        constraintLayout = findViewById(R.id.constraintLayout)
        constraintLayout?.visibility = View.GONE
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val titltBar = findViewById<TextView>(R.id.titlt_bar)
        titltBar.text = "Favourite"
        rc?.layoutManager = LinearLayoutManager(this)

        val da = StorageUtils(this).getBookmark(this)
        if (da != null && da.size > 0)
            fileInfoBeans.addAll(da)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.GONE
        val tvText = findViewById<TextView>(R.id.tv_text)
        if (fileInfoBeans.size == 0 || fileInfoBeans.isEmpty()) {
            tvText.text = "No Favourite, click button heart to try again!"
            tvText.visibility = View.VISIBLE
        } else {
            tvText.visibility = View.GONE
            adapter = RecyclerViewAdapter(fileInfoBeans, this, object :
                ItemFileClickListener {
                override fun onItemClick(file: File?) {
                    if (mIoIOnTemClickListener != null) {
                        mIoIOnTemClickListener!!.onItemClick(file)
                    }
                }

                override fun onAddToBookmark(file: File?) {
                    if (mIoIOnTemClickListener != null) {
                        mIoIOnTemClickListener!!.onAddToBookmark(file)
                        adapter!!.notifyDataSetChanged()
                    }
                }

                override fun onShareFile(file: File?) {
                    if (mIoIOnTemClickListener != null) {
                        mIoIOnTemClickListener!!.onShareFile(file)
                    }
                }

                override fun onRemoveBookmark(file: File?) {
                    if (mIoIOnTemClickListener != null) {
                        mIoIOnTemClickListener!!.onRemoveBookmark(file)
                        for (i in fileInfoBeans.size - 1 downTo 0)
                            if (fileInfoBeans[i] is File)
                                if ((fileInfoBeans[i] as File) == file)
                                    fileInfoBeans.removeAt(i)

                        adapter!!.notifyDataSetChanged()
                    }
                }

                override fun onRenameClick(file: File?) {
                    if (mIoIOnTemClickListener != null) {
                        mIoIOnTemClickListener!!.onRenameClick(file)
                        adapter!!.notifyDataSetChanged()
                    }
                }

                override fun onCreateShortCut(file: File?) {
                    if (mIoIOnTemClickListener != null) {
                        mIoIOnTemClickListener!!.onCreateShortCut(file)
                    }
                }

                override fun onDeleteClick(file: File?) {
                    if (mIoIOnTemClickListener != null) {
                        mIoIOnTemClickListener!!.onDeleteClick(file)
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }, true)
            App.isSizeFavourite.observe(this) {
                if (adapter != null) adapter!!.notifyDataSetChanged()
            }

            rc?.adapter = adapter
        }
        toolbar.setNavigationOnClickListener { this.finish() }
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sort_name_a_z -> {
                    if (fileInfoBeans.size != 0) {
                        val dumList = mutableListOf<File>()
                        for (i in fileInfoBeans) {
                            if (i is File) dumList.add(i)
                        }
                        dumList.sortWith { row1, row2 ->
                            if (row1.name!!.lowercase(Locale.getDefault()) < row2.name!!.lowercase(
                                    Locale.getDefault()
                                )
                            ) {
                                return@sortWith -1
                            }
                            if (row1.name!!.equals(
                                    row2.name!!,
                                    ignoreCase = true
                                )
                            ) return@sortWith 0
                            1
                        }
                        val k = dumList.size
                        for (i in 0 until k) {
                            if (fileInfoBeans[i] is File) {
                                fileInfoBeans[i] = dumList[0]
                                dumList.removeAt(0)
                            }
                        }
                        adapter!!.notifyDataSetChanged()
                    } else {
                        SweetAlertDialog(
                            this,
                            SweetAlertDialog.WARNING_TYPE
                        )

                            .setConfirmButtonBackgroundColor(
                                ContextCompat.getColor(
                                    this,
                                    R.color.color_button
                                )
                            )
                            .setContentText("No File!")
                            .show()
                    }
                    true
                }
                R.id.sort_name_z_a -> {
                    if (fileInfoBeans.size != 0) {
                        val dumList = mutableListOf<File>()
                        for (i in fileInfoBeans) {
                            if (i is File) dumList.add(i)
                        }
                        dumList.sortWith { row1, row2 ->
                            if (row1.name!!.lowercase(Locale.getDefault()) < row2.name!!.lowercase(
                                    Locale.getDefault()
                                )
                            ) {
                                return@sortWith 1
                            }
                            if (row1.name!!.equals(
                                    row2.name!!,
                                    ignoreCase = true
                                )
                            ) return@sortWith 0
                            -1
                        }
                        val k = dumList.size
                        for (i in 0 until k) {
                            if (fileInfoBeans[i] is File) {
                                fileInfoBeans[i] = dumList[0]
                                dumList.removeAt(0)
                            }
                        }
                        adapter!!.notifyDataSetChanged()
                    } else {
                        SweetAlertDialog(
                            this,
                            SweetAlertDialog.WARNING_TYPE
                        )

                            .setConfirmButtonBackgroundColor(
                                ContextCompat.getColor(
                                    this,
                                    R.color.color_button
                                )
                            )
                            .setContentText("No File!")
                            .show()
                    }
                    true
                }
                R.id.sort_size -> {
                    if (fileInfoBeans.size != 0) {
                        val dumList = mutableListOf<File>()
                        for (i in fileInfoBeans) {
                            if (i is File) dumList.add(i)
                        }
                        dumList.sortWith { row1, row2 ->
                            if (row1.length()!! < row2.length()!!) {
                                return@sortWith -1
                            }
                            if (row1.length()!! == row2.length()!!) return@sortWith 0
                            1
                        }
                        val k = dumList.size
                        for (i in 0 until k) {
                            if (fileInfoBeans[i] is File) {
                                fileInfoBeans[i] = dumList[0]
                                dumList.removeAt(0)
                            }
                        }
                        adapter!!.notifyDataSetChanged()
                    } else {
                        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setConfirmButtonBackgroundColor(
                                ContextCompat.getColor(
                                    this,
                                    R.color.color_button
                                )
                            )
                            .setContentText("No File!")
                            .show()
                    }
                    true
                }
                R.id.sort_date -> {
                    if (fileInfoBeans.size != 0) {
                        val dumList = mutableListOf<File>()
                        for (i in fileInfoBeans) {
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
                            if (fileInfoBeans[i] is File) {
                                fileInfoBeans[i] = dumList[0]
                                dumList.removeAt(0)
                            }
                        }
                        adapter!!.notifyDataSetChanged()
                    } else {
                        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setConfirmButtonBackgroundColor(
                                ContextCompat.getColor(
                                    this,
                                    R.color.color_button
                                )
                            )
                            .setContentText("No File!")
                            .show()
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getCount()
    }

    private fun getCount() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fileInfoBeans = ArrayList<Any>()
                fileInfoBeans.clear()
                Common.getFileMedia(this@FragmentActivity, fileInfoBeans, "all")
                withContext(Dispatchers.Main) {
                    val dumList = mutableListOf<File>()
                    for (i in fileInfoBeans) {
                        if (i is File) dumList.add(i)
                    }
                    fileInfoBeans.clear()
                    for (i in StorageUtils(this@FragmentActivity).getBookmark(this@FragmentActivity)) {
                        var check = false
                        for (j in dumList) {
                            if (i.path == j.path) {
                                check = true
                            }
                        }
                        if (!check) {
                            fileInfoBeans.add(i)
                        }
                    }
                    App.isSizeFavourite.value = 1
                    if (adapter != null) adapter!!.notifyDataSetChanged()
                }
            } catch (ex: Exception) {
                Log.e("getLeagues", "$ex")
            }
        }
    }




}