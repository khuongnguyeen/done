package com.tools.files.myreader.ocr.general

import android.app.Activity
import android.content.*
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.tools.files.myreader.ocr.EditOCRActivity
import com.tools.files.myreader.ocr.popups.ExportDialog
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import com.isseiaoki.simplecropview.util.Logger
import com.tools.files.myreader.R
import com.tools.files.myreader.activity.MainActivity
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.internal.Intrinsics

object ControlUtils {
    private const val SubscriptionUtil = "SubscriptionUtil"
    private const val CONSTANT_REMOVE_ADS = "CONSTANT_REMOVE_ADS"
    fun isBuyRMAds(mContext: Context): Int {
        val preferences = mContext.getSharedPreferences(SubscriptionUtil, Context.MODE_PRIVATE)
        return preferences.getInt(CONSTANT_REMOVE_ADS, -1)
    }

    fun setBuyRMAds(mContext: Context, isPurchase: Int) {
        val preferences = mContext.getSharedPreferences(SubscriptionUtil, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt(CONSTANT_REMOVE_ADS, isPurchase)
        editor.apply()
    }

    @JvmStatic
    fun saveImage(activity: Activity, bArr: ByteArray?): String {
        val baseImageFolder = getBaseImageFolder(activity)
        val file = File(baseImageFolder, System.currentTimeMillis().toString() + ".jpg")
        return try {
            val fileOutputStream = FileOutputStream(file.path)
            fileOutputStream.write(bArr)
            fileOutputStream.close()
            file.path
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    fun convert(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    fun getBaseImageFolder(context: Context): String {
        val sb = StringBuilder()
        var str: String? = null
        val externalFilesDir = context.getExternalFilesDir(null)
        if (externalFilesDir != null) {
            str = externalFilesDir.path
        }
        sb.append(str)
        sb.append(File.separator)
        sb.append("images")
        val file = File(sb.toString())
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.path
    }

    @JvmStatic
    fun showKeyboard(activity: Activity) {
        val inputMethodManager =
            (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun closeKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }

    @JvmStatic
    fun shareText(context: Context, text: String?) {
        try {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
            sharingIntent.putExtra(Intent.EXTRA_TEXT, text)
            context.startActivity(Intent.createChooser(sharingIntent, "Share to"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun copyToClipboard(context: Activity, text: String?) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
    }

    @JvmStatic
    fun funcCreateFileOCR(context: Activity, fileName: String?, listPareOCR: ArrayList<String?>) {
        val stringBuilder = StringBuilder()
        val exportDialog = ExportDialog(context, fileName)
        exportDialog.setOnExportListener {
            for (i in listPareOCR.indices) {
                stringBuilder.append(listPareOCR[i])
            }
            if (exportDialog.edtNameFile != null) {
                generateNoteOnSD(
                    context,
                    exportDialog.edtNameFile.text.toString(),
                    stringBuilder.toString()
                )
            } else {
                generateNoteOnSD(context, fileName, stringBuilder.toString())
            }
        }
        exportDialog.show()
    }

    fun generateNoteOnSD(context: Activity, sFileName: String?, sBody: String?) {
        try {
            val root: File
            root = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                    "documents"
                )
            } else {
                val path = context.getExternalFilesDir(null)!!.path + File.separator + "documents"
                File(path)
            }
            if (!root.exists()) {
                root.mkdirs()
            }
            val fileWrite = File(root, sFileName)
            val writer = FileWriter(fileWrite)
            writer.append(sBody)
            writer.flush()
            writer.close()
            if (EditOCRActivity.instance != null) {
                EditOCRActivity.instance!!.sendBroadcast(Intent("CREATE_TXT"))
                Toast.makeText(EditOCRActivity.instance, "Saved", Toast.LENGTH_SHORT).show()
                val intent = Intent(EditOCRActivity.instance, MainActivity::class.java)
                intent.putExtra("CREATE_TXT", "true")
                EditOCRActivity.instance!!.startActivity(intent)
                EditOCRActivity.instance!!.finish()
            }
        } catch (e: IOException) {
            Toast.makeText(context, "Create File Error!", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun inspectFromBitmap(context: Context, bitmap: Bitmap?): String {
        Intrinsics.checkNotNullParameter(context, "<this>")
        val build = TextRecognizer.Builder(context).build()
        if (bitmap == null) {
            val string = context.getString(R.string.cannot_reg_text)
            Intrinsics.checkNotNullExpressionValue(string, "getString(R.string.cannot_reg_text)")
            return string
        }
        return try {
            if (!build.isOperational) {
                return "Text recognizer could not be set up on your device"
            }
            val detect = build.detect(Frame.Builder().setBitmap(bitmap).build())
            val arrayList: MutableList<TextBlock> = ArrayList()
            var i = 0
            val size = detect.size()
            if (size > 0) {
                while (true) {
                    val i2 = i + 1
                    val valueAt = detect.valueAt(i)
                    Intrinsics.checkNotNullExpressionValue(valueAt, "textBlock")
                    arrayList.add(valueAt)
                    if (i2 >= size) {
                        break
                    }
                    i = i2
                }
            }
            //Collections.sort(arrayList);
            val sb = StringBuilder()
            for (value in arrayList) {
                sb.append(value.value)
                sb.append("\n")
            }
            build.release()
            val sb2 = sb.toString()
            Intrinsics.checkNotNullExpressionValue(sb2, "detectedText.toString()")
            sb2
        } catch (e: Exception) {
            build.release()
            e.printStackTrace()
            val string2 = context.getString(R.string.cannot_reg_text)
            Intrinsics.checkNotNullExpressionValue(string2, "getString(R.string.cannot_reg_text)")
            string2
        }
    }

    fun createNewUri(context: Context, format: CompressFormat?): Uri? {
        val currentTimeMillis = System.currentTimeMillis()
        val today = Date(currentTimeMillis)
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
        val title = dateFormat.format(today)
        val dirPath = dirPath
        val fileName = "scv" + title + "." + getMimeType(format)
        val path = "$dirPath/$fileName"
        val file = File(path)
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, title)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/" + getMimeType(format))
        values.put(MediaStore.Images.Media.DATA, path)
        val time = currentTimeMillis / 1000
        values.put(MediaStore.MediaColumns.DATE_ADDED, time)
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, time)
        if (file.exists()) {
            values.put(MediaStore.Images.Media.SIZE, file.length())
        }
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        Logger.i("SaveUri = $uri")
        return uri
    }

    val dirPath: String
        get() {
            var dirPath = ""
            var imageDir: File? = null
            val extStorageDir = Environment.getExternalStorageDirectory()
            if (extStorageDir.canWrite()) {
                imageDir = File(extStorageDir.path + "/Offices")
            }
            if (imageDir != null) {
                if (!imageDir.exists()) {
                    imageDir.mkdirs()
                }
                if (imageDir.canWrite()) {
                    dirPath = imageDir.path
                }
            }
            return dirPath
        }

    fun getMimeType(format: CompressFormat?): String {
        when (format) {
            CompressFormat.JPEG -> return "jpeg"
            CompressFormat.PNG -> return "png"
        }
        return "png"
    }
}