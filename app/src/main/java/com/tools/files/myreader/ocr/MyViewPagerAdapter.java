package com.tools.files.myreader.ocr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tools.files.myreader.R;

import java.util.ArrayList;

public class MyViewPagerAdapter extends RecyclerView.Adapter<MyViewPagerAdapter.MyHolder> {

    private Context context;
    private ArrayList<String> mListTag;

    public MyViewPagerAdapter(Context context, ArrayList<String> listTag) {
        this.context = context;
        this.mListTag = listTag;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.cell_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.mText.setText(mListTag.get(position));
    }

    @Override
    public int getItemCount() {
        return mListTag.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        public TextView mText;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.text);
        }
    }
}
