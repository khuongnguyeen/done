package com.tools.files.myreader.model;

import android.graphics.Bitmap;

public class FileAssetModel {
    public String mPath;
    public String mIcon;
    public String mType;
    public String mPath2;
    public Bitmap mBitmap;

    public FileAssetModel(String mPath, String mIcon, String mType) {
        this.mPath = mPath;
        this.mIcon = mIcon;
        this.mType = mType;
        mBitmap = null;
    }

    public FileAssetModel(String mPath, String mIcon, String mType, String mPath2) {
        this.mPath = mPath;
        this.mIcon = mIcon;
        this.mType = mType;
        this.mPath2 = mPath2;
        mBitmap = null;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public String getmIcon() {
        return mIcon;
    }

    public void setmIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getmPath2() {
        return mPath2;
    }

    public void setmPath2(String mPath2) {
        this.mPath2 = mPath2;
    }
}
