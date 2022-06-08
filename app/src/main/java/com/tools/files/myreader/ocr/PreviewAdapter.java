package com.tools.files.myreader.ocr;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tools.files.myreader.R;

import java.util.ArrayList;

public class PreviewAdapter extends RecyclerView.Adapter {

    private ArrayList<String> mListDocument;
    private Activity mActivity;
    public int mPosition = 1;

    public PreviewAdapter(ArrayList<String> mListDocument, Activity activity) {
        this.mListDocument = mListDocument;
        this.mActivity = activity;
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
        mPosition = position;
        FileHolder holder = (FileHolder) holder1;

        Glide.with(mActivity)
                .load(mListDocument.get(position))
                .error(R.color.gray)
                .into(holder.imgFile);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View cell = inflater.inflate(R.layout.item_scanner_preview, container, false);
        return new FileHolder(cell);
    }

    static class FileHolder extends RecyclerView.ViewHolder {
        ImageView imgFile;

        FileHolder(View itemView) {
            super(itemView);

            imgFile = itemView.findViewById(R.id.iv_item_preview);
        }
    }
}
