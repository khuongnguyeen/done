package com.tools.files.myreader.ulti

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.tools.files.myreader.R
import com.shockwave.pdfium.PdfiumCore
import com.tom_roush.pdfbox.pdmodel.common.PDPageLabelRange
import id.zelory.compressor.Compressor
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    const val TAG = "Utils"
    fun isFileNameValid(str: String): Boolean {
        val trim = str.trim { it <= ' ' }
        return !TextUtils.isEmpty(trim) && trim.matches(Regex("[a-zA-Z0-9-_ ]*"))
    }

    fun deleteFiles(str: String?) {
        val file = File(str)
        if (file.exists() && file.isDirectory) {
            val sb = StringBuilder()
            sb.append("find ")
            sb.append(str)
            sb.append(" -xdev -mindepth 1 -delete")
            try {
                Runtime.getRuntime().exec(sb.toString())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun dateFormatterMili(time: Long): String {
        val format = "ddMMMyyyyHHmmss"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(time*1000))
    }

    fun dateFormatter(time: Long): String {
        val format = "dd MMM yyyy HH:mm:ss"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(time))
    }

    @SuppressLint("SimpleDateFormat")
    fun dateFormatter(milliseconds: String): String {
        return SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date(milliseconds.toLong())).toString()
    }

    @Synchronized
    fun loadImage(context: Context, url: String, view: ImageView) {
        Glide.with(context)
            .load(url)
            .centerCrop()
//                .crossFade()
            .into(view)
    }


    fun isThumbnailPresent(context: Context, str: String?): Boolean {
        val name = File(str).name
        val sb = StringBuilder()
        sb.append(context.cacheDir)
        sb.append("/Thumbnails/")
        val sb2 = sb.toString()
        val sb3 = StringBuilder()
        sb3.append(sb2)
        sb3.append(removeExtension(name))
        sb3.append(".jpg")
        return File(sb3.toString()).exists()
    }

    @JvmStatic
    fun removeExtension(str: String): String {
        var str = str
        val lastIndexOf = str.lastIndexOf(System.getProperty("file.separator"))
        if (lastIndexOf != -1) {
            str = str.substring(lastIndexOf + 1)
        }
        val lastIndexOf2 = str.lastIndexOf(".")
        return if (lastIndexOf2 == -1) {
            str
        } else str.substring(0, lastIndexOf2)
    }

    fun generatePDFThumbnail(context: Context, str: String?) {
        val pdfiumCore = PdfiumCore(context)
        val file = File(str)
        val name = file.name
        try {
            val newDocument = pdfiumCore.newDocument(
                context.contentResolver.openFileDescriptor(
                    Uri.fromFile(file), PDPageLabelRange.STYLE_ROMAN_LOWER
                )
            )
            val sb = StringBuilder()
            sb.append(context.cacheDir)
            sb.append("/Thumbnails/")
            val sb2 = sb.toString()
            val file2 = File(sb2)
            if (!file2.exists()) {
                file2.mkdirs()
            }
            val sb3 = StringBuilder()
            sb3.append(sb2)
            sb3.append(removeExtension(name))
            sb3.append(".jpg")
            val sb4 = sb3.toString()
            val str2 = TAG
            val sb5 = StringBuilder()
            sb5.append("Generating thumb img ")
            sb5.append(sb4)
            Log.d(str2, sb5.toString())
            val fileOutputStream = FileOutputStream(sb4)
            pdfiumCore.openPage(newDocument, 0)
            val pageWidthPoint = pdfiumCore.getPageWidthPoint(newDocument, 0) / 2
            val pageHeightPoint = pdfiumCore.getPageHeightPoint(newDocument, 0) / 2
            try {
                val createBitmap =
                    Bitmap.createBitmap(pageWidthPoint, pageHeightPoint, Bitmap.Config.RGB_565)
                pdfiumCore.renderPageBitmap(
                    newDocument,
                    createBitmap,
                    0,
                    0,
                    0,
                    pageWidthPoint,
                    pageHeightPoint,
                    true
                )
                createBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            } catch (e: OutOfMemoryError) {
                Toast.makeText(context, R.string.failed_low_memory, Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
            pdfiumCore.closeDocument(newDocument)
        } catch (e2: Exception) {
            e2.printStackTrace()
        }
    }

    @JvmStatic
    fun getImageUriFromPath(toString: String): Uri {
        return Uri.fromFile(File(toString.replace(".pdf", ".jpg")))
    }

    fun getBitmap(ctx: Context?, uri: Uri?): Bitmap? {
        return Compressor(ctx)
            .setQuality(100)
            .compressToBitmap(File(uri?.path))
    }

}