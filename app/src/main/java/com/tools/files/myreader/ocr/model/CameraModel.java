package com.tools.files.myreader.ocr.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class CameraModel implements Serializable {
    private Bitmap mBitmap;
    private String pathImage;

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }
}
