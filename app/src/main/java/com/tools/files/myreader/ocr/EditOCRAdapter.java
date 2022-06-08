package com.tools.files.myreader.ocr;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.tools.files.myreader.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EditOCRAdapter extends PagerAdapter {

    private ArrayList<String> mListOCR;
    private Context mContext;
    public EditText editText;
    public String pathOCR;

    public EditOCRAdapter(Context context, ArrayList<String> listOCR) {
        this.mContext = context;
        this.mListOCR = listOCR;
    }

    @Override
    public int getCount() {
        return mListOCR.size();
    }

    @Override
    public @NotNull Object instantiateItem(final @NotNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_edit_ocr_adapter, container, false);
        editText = view.findViewById(R.id.edt_item_ocr);
        editText.setText(mListOCR.get(position));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                pathOCR = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(final @NotNull View container, final int position, final @NotNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
        return view == object;
    }

}
