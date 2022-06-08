package com.tools.files.myreader.ocr;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class GalleryPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragmentArrayList;

    public GalleryPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentArrayList) {
        super(fm);
        this.mFragmentArrayList = fragmentArrayList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentArrayList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Image";
        } else
            return "Album";
    }
}
