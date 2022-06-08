package com.tools.files.myreader.ocr;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.tools.files.myreader.ocr.model.ImageFolder;
import com.tools.files.myreader.ocr.model.PictureFacer;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QueryAllStorage {
    public static ArrayList<PictureFacer> getAllImageGallery(Activity activity) {
        ArrayList<PictureFacer> picFolders = new ArrayList<>();

        Uri allImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection_image = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.SIZE};

        Cursor cursorImage = activity.getContentResolver().query(allImagesUri, projection_image, null, null, null);
        try {
            if (cursorImage != null) {
                cursorImage.moveToFirst();
            }
            do {
                PictureFacer pictureFacer = new PictureFacer();
                String name = cursorImage.getString(cursorImage.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String dataPath = cursorImage.getString(cursorImage.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                long sizeImage = cursorImage.getLong(cursorImage.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));

                int dateIndex = cursorImage.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
                Date dateText = new Date(cursorImage.getLong(dateIndex));

                pictureFacer.setPicturePath(dataPath);
                pictureFacer.setPicturName(name);
                pictureFacer.setDateTime(dateText);
                pictureFacer.setPictureSize(convertStringToByte(sizeImage));
                picFolders.add(pictureFacer);
            } while (cursorImage.moveToNext());
            cursorImage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return picFolders;
    }

    public static ArrayList<ImageFolder> queryAllPicture(Activity activity) {
        ArrayList<ImageFolder> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();

        Uri allImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection_image = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.SIZE};

        Cursor cursorImage = activity.getContentResolver().query(allImagesUri, projection_image, null, null, /*MediaStore.Images.Media.DATE_TAKEN + " DESC"*/null);
        try {
            if (cursorImage != null) {
                cursorImage.moveToFirst();
            }
            do {
                ImageFolder imageFolder = new ImageFolder();
                //String name = cursorImage.getString(cursorImage.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folder = cursorImage.getString(cursorImage.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String dataPath = cursorImage.getString(cursorImage.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                long sizeImage = cursorImage.getLong(cursorImage.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));

                int dateIndex = cursorImage.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
                Date dateText = new Date(cursorImage.getLong(dateIndex));
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MM yyyy");
                String dateStr = dateFormat.format(dateText);

                String folderPaths = dataPath.substring(0, dataPath.lastIndexOf(folder + "/"));
                folderPaths = folderPaths + folder + "/";

                if (!picPaths.contains(folderPaths)) {
                    picPaths.add(folderPaths);
                    imageFolder.setPath(folderPaths);
                    imageFolder.setFolderName(folder);
                    imageFolder.setFirstPic(dataPath);
                    imageFolder.addpics();
                    imageFolder.setDate(dateStr);
                    imageFolder.setSize(sizeImage);
                    picFolders.add(imageFolder);
                } else {
                    for (int i = 0; i < picFolders.size(); i++) {
                        if (picFolders.get(i).getPath().equals(folderPaths)) {
                            picFolders.get(i).setFirstPic(dataPath);
                            picFolders.get(i).addpics();
                        }
                    }
                }
            } while (cursorImage.moveToNext());
            cursorImage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return picFolders;
    }

    // Get All Picture By Path
    public static ArrayList<PictureFacer> getAllImagesByFolder(Activity activity, String path) {
        ArrayList<PictureFacer> arrAllImage = new ArrayList<>();
        Uri allImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_TAKEN
        };
        Cursor cursorImage = activity.getContentResolver().query(allImagesUri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%" + path + "%"}, null);
        try {
            cursorImage.moveToFirst();
            do {
                PictureFacer pictureFacer = new PictureFacer();
                pictureFacer.setPicturName(cursorImage.getString(cursorImage.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));
                pictureFacer.setPicturePath(cursorImage.getString(cursorImage.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                pictureFacer.setPictureSize(convertStringToByte(cursorImage.getLong(cursorImage.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))));

                int dateIndex = cursorImage.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN); // Date
                Date dateText = new Date(cursorImage.getLong(dateIndex));
                pictureFacer.setDateTime(dateText);
                pictureFacer.setType("Image");
                arrAllImage.add(pictureFacer);

            } while (cursorImage.moveToNext());
            cursorImage.close();
            ArrayList<PictureFacer> reSelection = new ArrayList<>();
            for (int i = arrAllImage.size() - 1; i > -1; i--) {
                reSelection.add(arrAllImage.get(i));
            }
            arrAllImage = reSelection;
        } catch (Exception e) {
            Log.e("QueryAllStorage", "getAllImagesByFolder: " + e.getMessage());
            e.printStackTrace();
        }
        return arrAllImage;
    }

    public static ArrayList<File> queryAllOCR(Activity activity) {
        try {
            ArrayList<File> mFileArrayList = new ArrayList<>();
            String path;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + "documents";

            } else {
                path = activity.getExternalFilesDir(null).getPath() + File.separator + "documents";
            }

            File directory = new File(path);
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                mFileArrayList.add(files[i]);
            }
            return mFileArrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertStringToByte(long size_file) {
        DecimalFormat df = new DecimalFormat("0.00");
        String ret_str = "";
        if (size_file < 1024) {
            return size_file + " B";
        }
        if (size_file < 1048576) {
            if (size_file == 1024) {
                return "1 KB";
            }
            return df.format((float) (((double) size_file) / 1024.0d)) + " KB";
        } else if (size_file < 1073741824) {
            if (size_file == 1048576) {
                return "1 MB";
            }
            return df.format((float) (((double) size_file) / 1048576.0d)) + " MB";
        } else if (size_file == 1073741824) {
            return "1 GB";
        } else {
            return df.format((float) (((double) size_file) / 1.073741824E9d)) + " GB";
        }
    }
}
