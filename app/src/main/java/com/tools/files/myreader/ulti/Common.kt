package com.tools.files.myreader.ulti

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.*
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.all.me.io.helpers.utils.FileUtility
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.tools.files.myreader.BuildConfig
import com.tools.files.myreader.R
import com.tools.files.myreader.base.App.Companion.getAnalytics
import java.io.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow


object Common {


    fun shareFile(context: Context, uri: Uri) {
        val item = File(uri.path)
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            val sb = StringBuffer()
            sb.append("application/")
            //            sb.append(item.extension);

            //            shareIntent.type = "application/pdf"
            shareIntent.type = sb.toString()
            val fileUri: Uri
            fileUri = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
                File(item.path)
            )
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
            context.startActivity(Intent.createChooser(shareIntent, "Share " + item.name))
        } catch (unused: java.lang.Exception) {
            Toast.makeText(context, "Don't have any application to share", Toast.LENGTH_LONG)
                .show()
        }
    }


    fun toGrayscale(srcImage: Bitmap): Bitmap {
        val bmpGrayscale: Bitmap =
            Bitmap.createBitmap(srcImage.width, srcImage.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmpGrayscale)
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        paint.colorFilter = ColorMatrixColorFilter(cm)
        canvas.drawBitmap(srcImage, 0f, 0f, paint)
        return bmpGrayscale
    }


    fun createContrast(src: Bitmap, value: Double): Bitmap? {
        val width = src.width
        val height = src.height
        val bmOut = Bitmap.createBitmap(width, height, src.config)
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int
        val contrast = ((100 + value) / 100).pow(2.0)
        for (x in 0 until width) {
            for (y in 0 until height) {
                pixel = src.getPixel(x, y)
                A = Color.alpha(pixel)
                R = Color.red(pixel)
                R = (((R / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
                if (R < 0) {
                    R = 0
                } else if (R > 255) {
                    R = 255
                }
                G = Color.red(pixel)
                G = (((G / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
                if (G < 0) {
                    G = 0
                } else if (G > 255) {
                    G = 255
                }
                B = Color.red(pixel)
                B = (((B / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
                if (B < 0) {
                    B = 0
                } else if (B > 255) {
                    B = 255
                }
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        return bmOut
    }

    fun saveData(context: Context, KEY: String, boolean: Boolean) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY, boolean)
        editor.apply()
    }

    fun loadData(context: Context, KEY: String): Boolean {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY, false)
    }

    fun pushEventAnalytics(nameEvent: String?) {
        val params = Bundle()
        getAnalytics().logEvent(nameEvent!!, params)
    }



    fun sortFile(
        context: Context,
        mDataList: MutableList<Any>,
        one: Int,
        two: Int,
        three: Int,
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?
    ) {
        if (mDataList.size != 0) {
            val dumList = mutableListOf<File>()
            for (i in mDataList){
                if (i is File) dumList.add(i)
            }
            dumList.sortWith { row1, row2 ->
                if (row1.name!!.toLowerCase(Locale.ROOT) < row2.name!!.toLowerCase(Locale.ROOT)
                ) {
                    return@sortWith one
                }
                if (row1.name!!.equals(
                        row2.name!!,
                        ignoreCase = true
                    )
                ) return@sortWith two
                three
            }
            val k = dumList.size
            for (i in 0 until k){
                if (mDataList[i] is File) {
                    mDataList[i] = dumList[0]
                    dumList.removeAt(0)
                }
            }

            adapter!!.notifyDataSetChanged()
        } else {
            showDialogSweet(context, SweetAlertDialog.WARNING_TYPE, "No File!")
        }
    }

    fun showDialogSweet(context: Context, type: Int, string: String) {
        SweetAlertDialog(context, type).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                confirmButtonBackgroundColor = context.getColor(R.color.color_button)
            }
            contentText = string
            show()
        }
    }

    fun callOfficeActivity(context: Context, path: String) {
        val mFile = File(path)
        FileUtility.openFile(context as Activity, mFile, 0)
    }

    fun convertUnits(nb: Double?): String {
        val numberFormat = DecimalFormat("#.0")
        val numb = nb!! / 1024.0
        return if (numb >= 1000.0) {

            numberFormat.format(numb / 1024.0) + " Mb"
        } else {
            numberFormat.format(numb) + " Kb"
        }
    }

    fun getFileMedia(
        context: Context,
        mDataList: ArrayList<Any>,
        fileType: String
    ): ArrayList<Any> {
        val columns = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            MediaStore.Files.FileColumns.DATA
        )
        var select = ""
        select = when (fileType) {
            "word" -> {
                "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.doc'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.docx'" + ")"
            }
            "xls" -> {
                "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.xls'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.xlsx'" + ")"
            }
            "txt" -> {
                "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.txt'" + ")"
            }
            "ppt" -> {
                "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.ppt'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.pptx'" + ")"
            }
            "pdf" -> {
                "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.pdf'" + ")"
            }
            "zip/rar" -> {
                "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.zip'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.rar'" + ")"
            }
            "all" -> {
                "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.zip'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.rar'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.doc'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.docx'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.xls'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.xlsx'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.ppt'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.pptx'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.txt'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.pdf'" + ")"
            }
            else -> {
                "(" + MediaStore.Files.FileColumns.DATA + ")"
            }
        }
        val contentResolver = Objects.requireNonNull(context).contentResolver
        var cursor = contentResolver?.query(MediaStore.Files.getContentUri("external"), columns, select, null, null)
        val columnIndexOrThrowData =
            cursor!!.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val path = cursor.getString(columnIndexOrThrowData)
                mDataList.add(File(path))
            }
            cursor.close()
            cursor = null
        }
        return mDataList
    }

    //delete file
    fun scanDeleteFile(context: Context, path2: String) {
        try {
            MediaScannerConnection.scanFile(
                context, arrayOf(path2),
                null
            ) { path, uri ->
                val file = File(path)
                file.delete()
                if (file.exists()) {
                    file.canonicalFile.delete()
                    if (file.exists()) {
                        context.deleteFile(file.name)
                    }
                }
                context.contentResolver.delete(uri, null, null)
                // TODO:   App.getDB().dataFavouriteDao().deleteFavourite(path)
//                App.getDB().dataFileDao().deleteFile(path)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun formatDate(date: Date?): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        return formatter.format(date)
    }

    fun capitalizeString(string: String): String {
        val chars = string.toLowerCase(Locale.ROOT).toCharArray()
        var found = false
        for (i in chars.indices) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i])
                found = true
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') {
                found = false
            }
        }
        return String(chars)
    }

    fun checkPermission(context: Context): Boolean {
        if (ContextCompat.checkSelfPermission(
                context,
                "android.permission.WRITE_EXTERNAL_STORAGE"
            ) == 0
            && ContextCompat.checkSelfPermission(
                context,
                "android.permission.READ_EXTERNAL_STORAGE"
            ) == 0
        )
            return true
        return false
    }

    @JvmStatic
    fun getUriFromFile(context: Context?, str: String?): Uri {
        return if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(
                context!!,
                "io.documentreader.filereader.pdfreader.provider",
                File(str)
            )
        } else Uri.fromFile(File(str))
    }

    @JvmStatic
    fun isConnected(context: Context): Boolean {
        @SuppressLint("WrongConstant") val systemService = context.getSystemService("connectivity")
        Objects.requireNonNull(
            systemService,
            "null cannot be cast to non-null type android.net.ConnectivityManager"
        )
        val activeNetworkInfo = (systemService as ConnectivityManager).activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable && activeNetworkInfo.isConnected
    }

    private const val EOF = -1
    private const val DEFAULT_BUFFER_SIZE = 1024 * 4

    @JvmStatic
    fun fromUri(context: Context, uri: Uri?): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri!!)
            val fileName = getFileName(context, uri)
            val splitName = splitFileName(fileName)
            var tempFile = File.createTempFile(splitName[0], splitName[1])
            tempFile = rename(tempFile, fileName)
            tempFile.deleteOnExit()
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(tempFile)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            if (inputStream != null) {
                copy(inputStream, out)
                inputStream.close()
            }
            out?.close()
            tempFile
        } catch (ex: Exception) {
            null
        }
    }

    private fun splitFileName(fileName: String?): Array<String?> {
        var name = fileName
        var extension: String? = ""
        val i = fileName!!.lastIndexOf(".")
        if (i != -1) {
            name = fileName.substring(0, i)
            extension = fileName.substring(i)
        }
        return arrayOf(name, extension)
    }

    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf(File.separator)
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    private fun rename(file: File, newName: String?): File {
        val newFile = File(file.parent, newName)
        if (newFile != file) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old $newName file")
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to $newName")
            }
        }
        return newFile
    }

    @Throws(IOException::class)
    private fun copy(input: InputStream, output: OutputStream?): Long {
        var count: Long = 0
        var n: Int
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (EOF != input.read(buffer).also { n = it }) {
            output!!.write(buffer, 0, n)
            count += n.toLong()
        }
        return count
    }



}