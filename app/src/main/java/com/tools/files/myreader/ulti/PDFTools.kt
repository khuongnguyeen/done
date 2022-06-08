package com.tools.files.myreader.ulti


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.exifinterface.media.ExifInterface
import com.airbnb.lottie.LottieAnimationView
import com.all.me.io.helpers.utils.FileUtility
import com.tools.files.myreader.R
import com.tools.files.myreader.model.ImagePage
import com.shockwave.pdfium.PdfDocument
import com.shockwave.pdfium.PdfiumCore
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.common.PDPageLabelRange
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle
import com.tom_roush.pdfbox.pdmodel.graphics.image.JPEGFactory
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader
import com.tools.files.myreader.activity.*
import com.tools.files.myreader.base.App
import java.io.File
import java.io.FileOutputStream
import java.util.*

@Suppress("DEPRECATION")
class PDFTools {
    val TAG = "PDFTools"
    private var btnOpenFile: ImageView? = null
    private var share_file: ImageView? = null
    private var preview: ImageView? = null
    private var cardView5: CardView? = null
    lateinit var homeProgressView: ImageView
    lateinit var imageBg: ImageView
    lateinit var currentAction: TextView
    lateinit var mProgressView: ConstraintLayout
    lateinit var cl_ads: ConstraintLayout
    lateinit var percent: TextView
    lateinit var progressBar: ProgressBar
    lateinit var animationView: LottieAnimationView


    @SuppressLint("StaticFieldLeak")
    inner class PdfToImage(context: Context, aPath: String, constraintLayout: ConstraintLayout) :
        AsyncTask<Void, Int, Void>() {
        lateinit var allPdf: String
        var mContext: Context
        var numberPage: Int = 0
        var path: String
        lateinit var pdfiumCore: PdfiumCore
        lateinit var pdfDocument: PdfDocument

        init {
            mContext = context
            path = aPath
            mProgressView = constraintLayout
            initProgressView()

        }

        override fun doInBackground(vararg p0: Void?): Void? {
            var voidR: Void?
            var i: Int
            var str: String
            var i2: Int
            val name = File(path).name
            val arrayList = ArrayList<String>()
            val arrayList2 = ArrayList<String>()
            val sbd = StringBuilder()
            sbd.append(Environment.getExternalStorageDirectory().toString())
            sbd.append("/Pictures/PDFToImage/")
            sbd.append(Utils.removeExtension(name))
            sbd.append("/")
            allPdf = sbd.toString()
            val file = File(allPdf)
            if (!file.exists()) {
                file.mkdirs()
            }
            pdfiumCore = PdfiumCore(mContext)
            try {
                pdfDocument = pdfiumCore.newDocument(
                    mContext.contentResolver.openFileDescriptor(
                        Uri.fromFile(File(path)), PDPageLabelRange.STYLE_ROMAN_LOWER
                    )
                )
                numberPage = this.pdfiumCore.getPageCount(this.pdfDocument)
                progressBar.max = numberPage
                var i3 = 0
                while (i3 < numberPage && !isCancelled) {
                    val sb2 = StringBuilder()
                    sb2.append(allPdf)
                    sb2.append(Utils.removeExtension(name))
                    sb2.append(" page ")
                    val i4 = i3 + 1
                    sb2.append(i4)
                    sb2.append(".jpg")
                    val ss2 = sb2.toString()
                    val fileOutputStream = FileOutputStream(ss2)

                    val sb3 = StringBuilder()
                    sb3.append(R.string.page_to_image)
                    sb3.append(sb3)
                    Log.d(TAG, sb3.toString())
                    pdfiumCore.openPage(pdfDocument, i3)
                    val pageWidthPoint = pdfiumCore.getPageWidthPoint(pdfDocument, i3) * 2
                    val pageHeightPoint =
                        pdfiumCore.getPageHeightPoint(pdfDocument, i3) * 2
                    try {
                        val createBitmap = Bitmap.createBitmap(
                            pageWidthPoint,
                            pageHeightPoint,
                            Bitmap.Config.ARGB_8888
                        )
                        i2 = 1
                        str = ss2
                        i = i4
                        try {
                            pdfiumCore.renderPageBitmap(
                                pdfDocument,
                                createBitmap,
                                i3,
                                0,
                                0,
                                pageWidthPoint,
                                pageHeightPoint,
                                true
                            )
                            createBitmap.compress(Bitmap.CompressFormat.JPEG, 60, fileOutputStream)
                        } catch (e: OutOfMemoryError) {
                            e.printStackTrace()
                        }

                    } catch (e2: OutOfMemoryError) {
                        e2.printStackTrace()
                        str = ss2
                        i = i4
                        i2 = 1
                        arrayList.add(str)
                        arrayList2.add("image/jpg")
                        val numArr = arrayOfNulls<Int>(i2)
                        numArr[0] = Integer.valueOf(i)
                        publishProgress(*numArr)
                    }
                    arrayList.add(str)
                    arrayList2.add("image/jpg")
                    val numArr2 = arrayOfNulls<Int>(i2)
                    numArr2[0] = Integer.valueOf(i)
                    publishProgress(*numArr2)
                    i3 = i
                }
                pdfiumCore.closeDocument(pdfDocument)
                return try {
                    MediaScannerConnection.scanFile(
                        mContext,
                        arrayList.toTypedArray(),
                        arrayList2.toTypedArray(),
                        null
                    )
                    null
                } catch (unused: Exception) {
                    voidR = null
                    voidR
                }
            } catch (unused2: Exception) {
                voidR = null
                return voidR
            }
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            updateProgressPercent(values[0]!!, numberPage)
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            currentAction.text = ""
            processingFinished(mContext, "")
            openImageDirectory(mContext, mContext.getString(R.string.open_directory), this.allPdf)
            share_file?.visibility = View.GONE


        }
    }


    //-------------------------------------___________________________------------------------------


    @SuppressLint("StaticFieldLeak")
    inner class SaveOrganizedPages(
        private val mContext: Context,
        private val imagePages: List<ImagePage>,
        private val newFileName: String,
        constraintLayout: ConstraintLayout
    ) : AsyncTask<Void, Int, Void>() {
        private var organizedPages: ArrayList<Int> = ArrayList()
        private var generatedPDFPath: String? = null
        private var numPages = 0
        private var allPdfDocuments = ""

        init {
            mProgressView = constraintLayout
            initProgressView()
        }

        public override fun onPreExecute() {
            super.onPreExecute()
            progressBar.max = this.numPages
            currentAction.setText(R.string.converting)
            mProgressView.visibility = View.VISIBLE
//            Glide.with(mContext).load(imagePages[0]).into(preview!!)
        }

        public override fun doInBackground(vararg voidArr: Void): Void? {
            val z =
                PreferenceManager.getDefaultSharedPreferences(mContext)
                    .getBoolean(ConstantSPKey.GRID_MODE, false)
            try {
                imagePages.forEach {
                    organizedPages.add(it.pageNumber)
                }
                val sb = StringBuilder()
                sb.append(Environment.getExternalStorageDirectory())
                sb.append("/Documents/PDF/ImageToPDF/")
                this.allPdfDocuments = sb.toString()
                val file = File(allPdfDocuments)
                val sbb = StringBuilder()
                sbb.append(allPdfDocuments)
                sbb.append(newFileName)
                sbb.append(".pdf")
                this.generatedPDFPath = sbb.toString()
                if (!file.exists()) file.mkdirs()
                PDFBoxResourceLoader.init(mContext)
                val pDDocument = PDDocument()
                numPages = organizedPages.size
                var i = 0
                while (i < this.numPages && !isCancelled) {
                    val path =
                        imagePages[i].imageUri
                    val rotateBitmap = rotateBitmap(
                        ImageUtils.instant!!.getCompressedBitmap(path),
                        ExifInterface(path!!).getAttributeInt("Orientation", 0)
                    )
                    val width = rotateBitmap!!.width.toFloat()
                    val height = rotateBitmap.height.toFloat()
                    val pDPage = PDPage(PDRectangle(width, height))
                    pDDocument.addPage(pDPage)
                    // val img = PDImageXObject.createFromFile(File(path),pDDocument)
                    val img = JPEGFactory.createFromImage(pDDocument, rotateBitmap)
                    val pDPageContentStream =
                        PDPageContentStream(pDDocument, pDPage)
                    pDPageContentStream.drawImage(img, 0.0f, 0.0f, width, height)
                    pDPageContentStream.close()
                    i++
                    publishProgress(i)
                }
                pDDocument.save(this.generatedPDFPath!!)
                pDDocument.close()
                if (z) {
                    Utils.generatePDFThumbnail(
                        mContext,
                        generatedPDFPath
                    )
                }
                MediaScannerConnection.scanFile(
                    mContext,
                    arrayOf(generatedPDFPath!!),
                    arrayOf("application/pdf"),
                    null
                )
                val str = TAG
                val sb2 = StringBuilder()
                sb2.append("Page order")
                sb2.append(this.organizedPages)
                Log.d(str, sb2.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            updateProgressPercent(values[0], this.numPages)
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            currentAction.text = ""
            processingFinished(mContext, "")
            setupOpenPath(mContext, generatedPDFPath.toString())
            share_file?.setOnClickListener {
                Common.shareFile(mContext, Uri.fromFile(File(generatedPDFPath.toString())))
            }


        }
    }

    fun initProgressView() {
        percent = mProgressView.findViewById(R.id.percent)
        cardView5 = mProgressView.findViewById(R.id.cardView5)
        currentAction = mProgressView.findViewById(R.id.current_action)
        progressBar = mProgressView.findViewById(R.id.progress_bar)
        animationView = mProgressView.findViewById(R.id.ani_view)
        imageBg = mProgressView.findViewById(R.id.cl_bg)
        btnOpenFile = mProgressView.findViewById(R.id.open_file)
        cl_ads = mProgressView.findViewById(R.id.cl_ads)
        share_file = mProgressView.findViewById(R.id.share_file)
        preview = mProgressView.findViewById(R.id.preview)
        homeProgressView = mProgressView.findViewById(R.id.home_progress_view)
    }

    fun processingFinished(context: Context, str: String) {

        percent.visibility = View.GONE
        cl_ads.visibility = View.VISIBLE
        cardView5?.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
        animationView.visibility = View.INVISIBLE
        imageBg.visibility = View.VISIBLE
        homeProgressView.visibility = View.VISIBLE
        btnOpenFile!!.visibility = View.VISIBLE

        if (!TextUtils.isEmpty(str)) {
            this.currentAction.text = "$str compression"
        }

    }

    fun updateProgressPercent(i: Int?, i2: Int) {
        val i3 = (i!!.toFloat() * 100.0f).toInt() / i2
        val ss = "$i3 %"
        percent.text = ss
        progressBar.progress = i
    }

    fun setupOpenPath(context: Context, str2: String) {
        homeProgressView.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    MainActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
        this.btnOpenFile!!.setOnClickListener {
            Log.d("lllll", "click")
            val file = File(str2)
            FileUtility.openFile(context as Activity, file, 0)


        }
    }

    fun openImageDirectory(context: Context, str: String, str2: String) {
        homeProgressView.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    MainActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }

        btnOpenFile!!.setOnClickListener {
            val intent = Intent(context, ViewImageActivity::class.java)
            intent.putExtra(Action.ACTION_INTENT, Action.PDF_TO_IMG)
                .putExtra(Action.IMG_FOLDER, str2)
            context.startActivity(intent)

        }
    }

    fun rotateBitmap(bitmap: Bitmap, i: Int): Bitmap? {
        val matrix = Matrix()
        when (i) {
            1 -> return bitmap
            2 -> matrix.setScale(-1.0f, 1.0f)
            3 -> matrix.setRotate(180.0f)
            4 -> {
                matrix.setRotate(180.0f)
                matrix.postScale(-1.0f, 1.0f)
            }
            5 -> {
                matrix.setRotate(90.0f)
                matrix.postScale(-1.0f, 1.0f)
            }
            6 -> matrix.setRotate(90.0f)
            7 -> {
                matrix.setRotate(-90.0f)
                matrix.postScale(-1.0f, 1.0f)
            }
            8 -> matrix.setRotate(-90.0f)
            else -> return bitmap
        }
        return try {
            val createBitmap =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()
            createBitmap
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            null
        }

    }
}
