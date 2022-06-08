package com.tools.files.myreader.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.all.me.io.helpers.utils.StorageUtils
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.tools.files.myreader.R
import com.tools.files.myreader.interfaces.ItemFileClickListener
import com.tools.files.myreader.ulti.FileUtil
import com.tools.files.myreader.ulti.Utils.dateFormatter
import java.io.File
import java.util.*

class RecyclerViewAdapter(
    private val mRecyclerViewItems: MutableList<Any>,
    private val mContext: Context,
    private var mItemClickListener: ItemFileClickListener,
    var boolean: Boolean = false
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var body: View
        var mFileIcon: ImageView
        var mFileName: TextView
        var mFileDate: TextView
        var mFileSize: TextView
        var buttonViewOption: TextView
        var imvFavourite: ImageView

        init {
            body = itemView.findViewById(R.id.body)
            mFileIcon = itemView.findViewById(R.id.iv_file_icon)
            mFileName = itemView.findViewById(R.id.tv_file_name)
            mFileDate = itemView.findViewById(R.id.tv_date)
            mFileSize = itemView.findViewById(R.id.tv_size)
            buttonViewOption = itemView.findViewById(R.id.textViewOptions)
            imvFavourite = itemView.findViewById(R.id.imv_favourite)
        }

    }

    override fun getItemCount(): Int {
        return mRecyclerViewItems.size
    }

    override fun getItemViewType(position: Int): Int {
        val recyclerViewItem = mRecyclerViewItems[position]
        return if (recyclerViewItem is NativeAd) {
            UNIFIED_NATIVE_AD_VIEW_TYPE
        } else MENU_ITEM_VIEW_TYPE
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val menuItemLayoutView: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_file_new, viewGroup, false)
        return ItemViewHolder(menuItemLayoutView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            UNIFIED_NATIVE_AD_VIEW_TYPE -> {
            }
            else -> {
                (holder as ItemViewHolder).body.tag = position
                if (boolean) holder.buttonViewOption.visibility = View.GONE
                else holder.buttonViewOption.visibility = View.VISIBLE
                val bean = mRecyclerViewItems!![position] as File
                holder.mFileDate.text = dateFormatter(bean.lastModified())
                holder.mFileName.text = bean.name
                holder.mFileSize.text = FileUtil.formatFileSize(bean.length()!!)
                holder.mFileIcon.setImageResource(
                    FileUtil.getFileTypeImageId(
                        mContext, bean.name
                    )
                )
                val arr = StorageUtils(mContext).getBookmark(mContext)
                var check = false
                if (arr != null && arr.size > 0) for (i in arr) {
                    if (i.path == bean.path) {
                        check = true
                    }
                }

                if (check) holder.imvFavourite.setImageResource(R.drawable.ic_bookmark_file_selected)
                else holder.imvFavourite.setImageResource(R.drawable.ic_bookmark_file_normal)
                holder.imvFavourite.setOnClickListener {
                    if (check) {
                        mItemClickListener.onRemoveBookmark(bean)
                        holder.imvFavourite.setImageResource(R.drawable.ic_bookmark_file_normal)
                        check = false
                    } else {
                        mItemClickListener.onAddToBookmark(bean)
                        holder.imvFavourite.setImageResource(R.drawable.ic_bookmark_file_selected)
                        check = true
                    }
                }

                holder.itemView.setOnClickListener {
                    mItemClickListener.onItemClick(bean)
                }

                holder.buttonViewOption.setOnClickListener { //creating a popup menu
                    val popup = PopupMenu(mContext, holder.buttonViewOption)
                    //inflating menu from xml resource
                    popup.inflate(R.menu.options_menu)
                    //adding click listener
                    popup.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.item_rename -> {
                                mItemClickListener!!.onRenameClick(bean)
                                true
                            }
                            R.id.item_delete -> {
                                mItemClickListener!!.onDeleteClick(bean)
                                true
                            }
                            else -> false
                        }
                    }
                    popup.show()
                }
            }
        }
    }

    companion object {
        private const val MENU_ITEM_VIEW_TYPE = 0
        private const val UNIFIED_NATIVE_AD_VIEW_TYPE = 1
    }
}