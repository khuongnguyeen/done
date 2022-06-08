package com.tools.files.myreader.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tools.files.myreader.R;
import com.tools.files.myreader.interfaces.ItemFileClickListener;
import com.tools.files.myreader.model.FileAssetModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CreateFileAdapter extends RecyclerView.Adapter {
    private long timeClick = 0;
    private Context mContext;
    private String extension;
    private ItemFileClickListener mItemFileClickListener;
    private static final int CONTENT = 0;
    private static final int AD = 1;
    private List<FileAssetModel> mListFileUtil = new ArrayList<>();
    private onItemClickAssetListener onItemClickAssetListener;

    public CreateFileAdapter(ArrayList<FileAssetModel> filePaths, Context _context, onItemClickAssetListener mItemFileClickListener) {
        mListFileUtil = filePaths;

        this.mContext = _context;
        this.onItemClickAssetListener = mItemFileClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return CONTENT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CONTENT) {
            View dataLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_file_create, parent, false);
            return new FileViewHolder(dataLayoutView);
        } else {
            View nativeExpressLayoutView = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.card_native_banner_ads,
                    parent, false);
            return new NativeBannerAdViewHolder(nativeExpressLayoutView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        bindDataTypeView(viewHolder, position);
    }

    private void bindDataTypeView(RecyclerView.ViewHolder viewHolder, final int position) {
        final FileViewHolder holder = (FileViewHolder) viewHolder;
        final FileAssetModel fileAssetModel;
        Bitmap bitmap;
        Bitmap bitmap2;

        fileAssetModel = mListFileUtil.get(position);

        // load image
        try {
            // get input stream
            InputStream ims = mContext.getAssets().open(fileAssetModel.getmIcon());
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            holder.imvItemFileFile.setImageDrawable(d);
        } catch (IOException ex) {
            return;
        }

        holder.txvItemItemFileTitle.setText(fileAssetModel.getmType());
    }

    @Override
    public int getItemCount() {

        if (mListFileUtil.size() > 0) {
            return mListFileUtil.size();
        } else {
            return 0;
        }

    }

    public void updateData(Activity activity, ArrayList<FileAssetModel> listFile) {
        mListFileUtil.clear();
        mListFileUtil = listFile;
        Log.e("updateData", "updateData3: " + listFile.size());
        notifyDataSetChanged();
    }

    public class NativeBannerAdViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout rllNativeBanner;

        public NativeBannerAdViewHolder(@NonNull View view) {
            super(view);
            rllNativeBanner = view.findViewById(R.id.ln_native);
        }
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        private ImageView imvItemFileFile;
        private TextView txvItemItemFileTitle;

        FileViewHolder(View itemView) {
            super(itemView);
            imvItemFileFile = itemView.findViewById(R.id.imv_item_file__create_file);
            txvItemItemFileTitle = itemView.findViewById(R.id.txv_item_item_file_create_title);

            itemView.setOnClickListener(view -> {
                int position = getLayoutPosition();
                if (onItemClickAssetListener != null) {
                    onItemClickAssetListener.onClickItemAsset(mListFileUtil.get(position));
                }
            });
        }
    }

    public interface onItemClickAssetListener {
        void onClickItemAsset(FileAssetModel fileAssetModel);
    }

}
