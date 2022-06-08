package com.tools.files.myreader.ocr;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tools.files.myreader.R;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter {

    private ArrayList<Float> mListDocument;
    private Activity mActivity;
    private onClickItem mOnClickItem;
    private Bitmap mPathImage;
    private ArrayList<Integer> mListColor;

    public void setOnClickItem(onClickItem mOnClickItem) {
        this.mOnClickItem = mOnClickItem;
    }

    public FilterAdapter(ArrayList<Float> mListDocument, Activity activity, Bitmap pathImage, ArrayList<Integer> listColor) {
        this.mListDocument = mListDocument;
        this.mActivity = activity;
        this.mPathImage = pathImage;
        this.mListColor = listColor;
    }

    @Override
    public int getItemCount() {
        return mListDocument != null ? mListDocument.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        ItemHolder(viewHolder, position);
    }

    private void ItemHolder(@NonNull RecyclerView.ViewHolder holder1, final int position) {
        FileHolder holder = (FileHolder) holder1;

        holder.tvValue.setText(String.valueOf(mListDocument.get(position)));
        holder.imgFile.setImageBitmap(mPathImage);

        holder.flTrans.setBackgroundColor(mListColor.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickItem != null)
                    mOnClickItem.onClickItem(position);
            }
        });
    }

    public void refreshImage(Bitmap pathImage) {
        mPathImage = pathImage;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View cell = inflater.inflate(R.layout.item_filter_ocr, container, false);
        return new FileHolder(cell);
    }

    static class FileHolder extends RecyclerView.ViewHolder {
        ImageView imgFile;
        FrameLayout flTrans;
        TextView tvValue;

        FileHolder(View itemView) {
            super(itemView);

            imgFile = itemView.findViewById(R.id.iv_filter);
            flTrans = itemView.findViewById(R.id.fl_item_filter);
            tvValue = itemView.findViewById(R.id.tv_value);
        }
    }

    public interface onClickItem {
        void onClickItem(int position);
    }
}
