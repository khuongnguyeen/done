package com.tools.files.myreader.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.all.me.io.helpers.utils.StorageUtils
import com.google.android.gms.ads.nativead.NativeAd
import com.tools.files.myreader.R
import com.tools.files.myreader.adapter.RecyclerViewAdapter
import com.tools.files.myreader.base.App.Companion.isSizeFavourite
import com.tools.files.myreader.interfaces.ItemFileClickListener
import com.tools.files.myreader.ulti.Common
import kotlinx.android.synthetic.main.fragment_file_list.*
import java.io.File
import java.util.*

class ListFavouriteFragment : Fragment() {

    var unifiedNativeAd: NativeAd? = null
    private var adapter: RecyclerViewAdapter? = null
    val fileInfoBeans = mutableListOf<Any>()
    var rc: RecyclerView? = null
    var constraintLayout: ConstraintLayout? = null
    private var mIoIOnTemClickListener: ItemFileClickListener? = null

    companion object {
        fun newInstance(): ListFavouriteFragment {
            return ListFavouriteFragment()
        }
    }

    val checkNative = MutableLiveData<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_file_favourite, container, false)
        mIoIOnTemClickListener = context as ItemFileClickListener
        rc = view.findViewById(R.id.recyclerview)
        constraintLayout = view.findViewById(R.id.constraintLayout)
        constraintLayout?.visibility = View.GONE
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        val titltBar = view.findViewById<TextView>(R.id.titlt_bar)
        titltBar.text = "Favourite"
        rc?.layoutManager = LinearLayoutManager(requireContext())

        val da = StorageUtils(requireContext()).getBookmark(requireContext())
        if (da != null && da.size > 0)
//            for (i in (da.size - 1) downTo 0) {
            fileInfoBeans.addAll(da)
//            }
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.GONE
        val tvText = view.findViewById<TextView>(R.id.tv_text)
        if (fileInfoBeans.size == 0 || fileInfoBeans.isEmpty()) {
            tvText.text = "No Favourite, click button heart to try again!"
            tvText.visibility = View.VISIBLE
        } else {
            tvText.visibility = View.GONE
            adapter = RecyclerViewAdapter(fileInfoBeans, requireContext(), object :
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
            isSizeFavourite.observe(viewLifecycleOwner) {
                if (adapter != null) adapter!!.notifyDataSetChanged()
            }

            rc?.adapter = adapter
        }
        toolbar.setNavigationOnClickListener { requireActivity().finish() }
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sort_name_a_z -> {
                    if (fileInfoBeans.size != 0) {
                        val dumList = mutableListOf<File>()
                        for (i in fileInfoBeans) {
                            if (i is File) dumList.add(i)
                        }
                        dumList.sortWith { row1, row2 ->
                            if (row1.name.lowercase(Locale.getDefault()) < row2.name.lowercase(
                                    Locale.getDefault()
                                )
                            ) {
                                return@sortWith -1
                            }
                            if (row1.name.equals(
                                    row2.name,
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
                            context,
                            SweetAlertDialog.WARNING_TYPE
                        )

                            .setConfirmButtonBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
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
                            if (row1.name.lowercase(Locale.getDefault()) < row2.name.lowercase(
                                    Locale.getDefault()
                                )
                            ) {
                                return@sortWith 1
                            }
                            if (row1.name.equals(
                                    row2.name,
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
                            context,
                            SweetAlertDialog.WARNING_TYPE
                        )

                            .setConfirmButtonBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
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
                            if (row1.length() < row2.length()) {
                                return@sortWith -1
                            }
                            if (row1.length() == row2.length()) return@sortWith 0
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
                        SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setConfirmButtonBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
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
                        SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setConfirmButtonBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
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
        return view
    }
}

