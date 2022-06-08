package com.tools.files.myreader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tools.files.myreader.R
import java.io.File

class ViewImageAdapter(
    var mContext: Context,
    var mList: ArrayList<File>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val view: View = LayoutInflater.from(mContext)
            .inflate(R.layout.item_image, parent, false)
        viewHolder = ItemViewHolder(view)
        return viewHolder
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder
        val item = mList[position]
        itemViewHolder.imgName.text = item.name
        Glide.with(mContext).load(item.path).error(R.drawable.banner).into(itemViewHolder.img)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var img: ImageView
        internal var imgName: TextView
        init {
            img = itemView.findViewById(R.id.iv_item)
            imgName = itemView.findViewById(R.id.img_name)
        }

    }

}