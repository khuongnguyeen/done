package com.tools.files.myreader.ocr.gallery;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tools.files.myreader.R;
import com.tools.files.myreader.ocr.model.PictureFacer;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter {

    private static final String TAG = GalleryAdapter.class.getName();
    private ArrayList<PictureFacer> mListImage;
    private ArrayList<String> mListPath;
    private Activity mActivity;
    private onClickItem mOnClickItem;

    public void setOnClickItem(onClickItem mOnClickItem) {
        this.mOnClickItem = mOnClickItem;
    }

    public GalleryAdapter(ArrayList<PictureFacer> mListDocument, Activity activity, ArrayList<String> listPath) {
        this.mListImage = mListDocument;
        this.mActivity = activity;
        this.mListPath = listPath;
    }

    @Override
    public int getItemCount() {
        return mListImage != null ? mListImage.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        ItemHolder(viewHolder, position);
    }

    private void ItemHolder(@NonNull RecyclerView.ViewHolder holder1, final int position) {
        FileHolder holder = (FileHolder) holder1;

        String item = mListImage.get(position).getPicturePath();

        Glide.with(mActivity)
                .load(mListImage.get(position).getPicturePath())
                .error(R.color.gray)
                .into(holder.imgFile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickItem != null)
                    mOnClickItem.onClickItem(position);
            }
        });

        if (mListImage.get(position).isSelected()) {
            holder.imgCheck.setChecked(true);
        } else {;
            holder.imgCheck.setChecked(false);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View cell = inflater.inflate(R.layout.item_image_folder, container, false);
        return new FileHolder(cell);
    }

    static class FileHolder extends RecyclerView.ViewHolder {
        RoundedImageView imgFile;
        CheckBox imgCheck;

        FileHolder(View itemView) {
            super(itemView);

            imgFile = itemView.findViewById(R.id.iv_img);
            imgCheck = itemView.findViewById(R.id.iv_check);
        }
    }

    public interface onClickItem {
        void onClickItem(int position);
    }
}
