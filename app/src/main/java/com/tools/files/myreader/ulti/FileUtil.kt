package com.tools.files.myreader.ulti

import android.content.Context
import com.tools.files.myreader.R
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object FileUtil {
    fun formatFileSize(fileS: Long): String {
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        val wrongSize = "0B"
        if (fileS == 0L) {
            return wrongSize
        }
        fileSizeString = when {
            fileS < 1024 -> {
                df.format(fileS.toDouble()) + "B"
            }
            fileS < 1048576 -> {
                df.format(fileS.toDouble() / 1024) + "KB"
            }
            fileS < 1073741824 -> {
                df.format(fileS.toDouble() / 1048576) + "MB"
            }
            else -> {
                df.format(fileS.toDouble() / 1073741824) + "GB"
            }
        }
        return fileSizeString
    }

    fun getFileLastModifiedTime(f: File): String {
        val cal = Calendar.getInstance()
        val time = f.lastModified()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        cal.timeInMillis = time
        return formatter.format(cal.time)
    }

    fun getFileTypeImageId(mContext: Context?, fileName: String?): Int {
        val id: Int
        id = when {
            checkSuffix(
                fileName,
                arrayOf("doc", "docx")
            ) -> {
                R.drawable.google_docs
            }
            checkSuffix(
                fileName,
                arrayOf("ppt", "pptx")
            ) -> {
                R.drawable.gg_red
            }
            checkSuffix(
                fileName,
                arrayOf("xls", "xlsx")
            ) -> {
                R.drawable.google_sheets
            }
            checkSuffix(
                fileName,
                arrayOf("pdf")
            ) -> {
                R.drawable.google_pdf
            }
            checkSuffix(
                fileName,
                arrayOf("txt")
            ) -> {
                R.drawable.google_df
            }
            checkSuffix(
                fileName,
                arrayOf("zip", "rar")
            ) -> {
                R.mipmap.ic_zip
            }
            else -> {
                R.mipmap.ic_single_file
            }
        }
        return id
    }

    fun checkSuffix(
        fileName: String?,
        fileSuffix: Array<String>
    ): Boolean {
        for (suffix in fileSuffix) {
            if (fileName != null) {
                if (fileName.toLowerCase().endsWith(suffix)) {
                    return true
                }
            }
        }
        return false
    }


    fun getFileInfoFromFile(file: File): File {
        val fileInfo = file
//        val lastDotIndex = file.name.lastIndexOf(".")
//        if (lastDotIndex > 0) {
//            val fileSuffix = file.name.substring(lastDotIndex + 1)
//            //            fileInfo.setSuffix(fileSuffix);
//        }
        return fileInfo
    }


}