package com.tools.files.myreader.interfaces;

import java.io.File;

public interface ItemFileClickListener {
    void onItemClick(File file);
    void onAddToBookmark(File file);
    void onShareFile(File file);
    void onRemoveBookmark(File file);
    void onCreateShortCut(File file);
    void onRenameClick(File file);
    void onDeleteClick(File file);
}
