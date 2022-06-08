package com.tools.files.myreader.ocr.gallery;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tools.files.myreader.R;
import com.tools.files.myreader.ocr.model.ImageFolder;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter {

    private ArrayList<ImageFolder> imageFolders;
    private Activity mActivity;
    private onClickItem mOnClickItem;

    public void setOnClickItem(onClickItem mOnClickItem) {
        this.mOnClickItem = mOnClickItem;
    }

    public FolderAdapter(ArrayList<ImageFolder> mListDocument, Activity activity) {
        this.imageFolders = mListDocument;
        this.mActivity = activity;
    }

    @Override
    public int getItemCount() {
        return imageFolders != null ? imageFolders.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        ItemHolder(viewHolder, position);
    }

    private void ItemHolder(@NonNull RecyclerView.ViewHolder holder1, final int position) {
        FileHolder holder = (FileHolder) holder1;

        Glide.with(mActivity)
                .load(imageFolders.get(position).getFirstPic())
                .error(R.color.gray)
                .into(holder.imgFile);

        holder.tvFolder.setText(imageFolders.get(position).getFolderName());
        holder.tvSizeFolder.setText(String.valueOf(imageFolders.get(position).getNumberOfPics()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickItem != null)
                    mOnClickItem.onClickItem(position);
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View cell = inflater.inflate(R.layout.item_folder_image, container, false);
        return new FileHolder(cell);
    }

    static class FileHolder extends RecyclerView.ViewHolder {
        RoundedImageView imgFile;
        TextView tvFolder;
        TextView tvSizeFolder;

        FileHolder(View itemView) {
            super(itemView);

            imgFile = itemView.findViewById(R.id.folderPic);
            tvFolder = itemView.findViewById(R.id.folderName);
            tvSizeFolder = itemView.findViewById(R.id.tv_size_folder);
        }
    }

    public interface onClickItem {
        void onClickItem(int position);
    }
}
