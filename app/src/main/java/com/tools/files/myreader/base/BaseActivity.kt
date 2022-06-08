package com.tools.files.myreader.base

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.all.me.io.helpers.utils.StorageUtils
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.artifex.sonui.SplashActivity
import com.tools.files.myreader.BuildConfig
import com.tools.files.myreader.R
import com.tools.files.myreader.adapter.RecyclerViewAdapter
import io.me.ndk.adsconfig.LovinInterstitialAds
import io.me.ndk.adsconfig.callbacks.LovinInterstitialOnCallBack
import com.tools.files.myreader.interfaces.ItemFileClickListener
import com.tools.files.myreader.ulti.Action
import com.tools.files.myreader.ulti.Common
import java.io.File
import java.lang.Exception
import java.util.*

abstract class BaseActivity : AppCompatActivity(), ItemFileClickListener {

    var mIoIOnTemClickListener: ItemFileClickListener? = null
    var adapter: RecyclerViewAdapter? = null
    lateinit var lovinInterstitialAds2: LovinInterstitialAds

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIoIOnTemClickListener = this
        lovinInterstitialAds2 = LovinInterstitialAds(this)

        // load interstitial ads
        lovinInterstitialAds2.loadShowAndLoadInterstitialAd(getString(R.string.applovin_interstitial_main_ids),
            true,
            false,object: LovinInterstitialOnCallBack {
                override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                }

                override fun onAdLoaded(maxAd: MaxAd?) {
                }

                override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                }

                override fun onAdDisplayed(maxAd: MaxAd?) {
                }

                override fun onAdClicked(maxAd: MaxAd?) {
                }

                override fun onAdHidden(maxAd: MaxAd?) {
                }


            })
    }

    override fun onItemClick(file: File?) {
        lovinInterstitialAds2.showInterstitialAds()
        Common.callOfficeActivity(this@BaseActivity, file?.path!!)

    }

    override fun onAddToBookmark(file: File?) {
        StorageUtils(this).addBookmark(this, file)
        if (adapter!=null) adapter!!.notifyDataSetChanged()
    }

    override fun onShareFile(file: File?) {
        try {
            val uri: Uri?
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "file/*"
            uri = try {
                FileProvider.getUriForFile(
                    this@BaseActivity,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file!!
                )
            } catch (e: Exception) {
                Uri.fromFile(file)
            }
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(sharingIntent, "Share Document!"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRemoveBookmark(file: File?) {
        StorageUtils(this).removeBookmark(this, file)
        if (adapter!=null) adapter!!.notifyDataSetChanged()

    }

    override fun onRenameClick(file: File?) {
//
//        val dumData = file!!
//        val dialog = Dialog(this@BaseActivity)
//        with(dialog) {
//            requestWindowFeature(1)
//            setContentView(R.layout.dialog_rename)
//            window!!.setBackgroundDrawable(ColorDrawable(0))
//            setCanceledOnTouchOutside(false)
//            setCancelable(false)
//        }
//        val edt = (dialog.findViewById(R.id.edt_rename) as EditText)
//        with(edt) {
//            imeOptions = EditorInfo.IME_ACTION_DONE
//            setOnEditorActionListener { _, actionId, _ ->
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    clickOk(dumData, edt, position, dialog)
//                    true
//                } else false
//            }
//            hint = dumData.name!!.replace(
//                ".${File(dumData.path!!).extension}",
//                ""
//            )
//        }
//        (dialog.findViewById(R.id.delete_ok) as TextView).setOnClickListener {
//            clickOk(dumData, edt, position, dialog)
//        }
//        (dialog.findViewById(R.id.delete_cancel) as TextView).setOnClickListener {
//            dialog.dismiss()
//        }
//        dialog.show()

    }

    override fun onDeleteClick(file: File?) {}

    override fun onCreateShortCut(file: File?) {
        val mFile = file!!
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {
            val intent = Intent(Intent(this, SplashActivity::class.java))
            intent.action = Intent.ACTION_VIEW
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(Action.SHORT_CUT_FILE_NAME, mFile.absolutePath)
            intent.putExtra(Action.SHORT_CUT_PAGE_NUM, 0)
            if (mFile.isFile) {
                if (mFile.name.endsWith(".pdf")) {
                    val shortcutInfo =
                        ShortcutInfoCompat.Builder(this, UUID.randomUUID().toString())
                            .setIntent(intent) // !!! intent's action must be set on oreo
                            .setShortLabel(mFile.name)
                            .setIcon(IconCompat.createWithResource(this, R.drawable.me_ic_pdf))
                            .build()
                    ShortcutManagerCompat.requestPinShortcut(this, shortcutInfo, null)
                    Toast.makeText(this, "Create shortcut success!", Toast.LENGTH_SHORT).show()
                } else if (mFile.name.endsWith(".xls") || mFile.name.endsWith(".xlsx")) {
                    val shortcutInfo =
                        ShortcutInfoCompat.Builder(this, UUID.randomUUID().toString())
                            .setIntent(intent) // !!! intent's action must be set on oreo
                            .setShortLabel(mFile.name)
                            .setIcon(IconCompat.createWithResource(this, R.drawable.me_ic_xlsx))
                            .build()
                    ShortcutManagerCompat.requestPinShortcut(this, shortcutInfo, null)
                    Toast.makeText(this, "Create shortcut success!", Toast.LENGTH_SHORT).show()
                } else if (mFile.name.endsWith(".ppt") || mFile.name.endsWith(".pptx")) {
                    val shortcutInfo =
                        ShortcutInfoCompat.Builder(this, UUID.randomUUID().toString())
                            .setIntent(intent) // !!! intent's action must be set on oreo
                            .setShortLabel(mFile.name)
                            .setIcon(IconCompat.createWithResource(this, R.drawable.me_ic_ppt))
                            .build()
                    ShortcutManagerCompat.requestPinShortcut(this, shortcutInfo, null)
                    Toast.makeText(this, "Create shortcut success!", Toast.LENGTH_SHORT).show()
                } else if (mFile.name.endsWith(".doc") || mFile.name.endsWith(".docx") || mFile.name.endsWith(
                        ".docb"
                    )
                ) {
                    val shortcutInfo =
                        ShortcutInfoCompat.Builder(this, UUID.randomUUID().toString())
                            .setIntent(intent) // !!! intent's action must be set on oreo
                            .setShortLabel(mFile.name)
                            .setIcon(IconCompat.createWithResource(this, R.drawable.me_ic_doc))
                            .build()
                    ShortcutManagerCompat.requestPinShortcut(this, shortcutInfo, null)
                    Toast.makeText(this, "Create shortcut success!", Toast.LENGTH_SHORT).show()
                } else {
                    val shortcutInfo =
                        ShortcutInfoCompat.Builder(this, UUID.randomUUID().toString())
                            .setIntent(intent) // !!! intent's action must be set on oreo
                            .setShortLabel(mFile.name)
                            .setIcon(IconCompat.createWithResource(this, R.drawable.ic_folder_item))
                            .build()
                    ShortcutManagerCompat.requestPinShortcut(this, shortcutInfo, null)
                }
            }
        }
    }

//    private fun clickOk(dumData: File, edt: EditText, position: Int, dialog: Dialog) {
//        val file = dumData //old name
//        val newName = edt.text.toString().trim()
//        if (newName != "") {
//            var check = false
//
//            for (i in dumList) {
//                val ext: String = file.extension
//                if (i.name == "$newName.$ext") check = true
//            }
//            if (check)
//                showDialogSweet(this@BaseActivity, WARNING_TYPE, "The file name has existed!")
//            else renameFile(this@BaseActivity, file, newName.trim(), dumData)
//        } else {
//            showDialogSweet(this@BaseActivity, WARNING_TYPE, "The file name cannot be blank!")
//        }
//        dialog.dismiss()
//    }


//    private fun renameFile(context: Context, file: File, suffix: String, dataFile: DataFile) {
//        val ext: String = file.extension
//        val onlyPath: String = file.parentFile.absolutePath
//        val newPath = "$onlyPath/$suffix.$ext"
////        getDB().dataFavouriteDao().renamePath("$suffix.$ext", file.path, newPath)
////        getDB().dataFileDao().renamePath("$suffix.$ext", file.path, newPath)
//        val newFile = File(newPath)
//        val rename: Boolean = file.renameTo(newFile)
//        if (rename) {
//            dataFile.name = "$suffix.${file.extension}"
//            dataFile.path = newPath
//            val resolver: ContentResolver =
//                context.applicationContext
//                    .contentResolver
//            resolver.delete(
//                MediaStore.Files.getContentUri("external"),
//                MediaStore.MediaColumns.DATA + "=?",
//                arrayOf(file.absolutePath)
//            )
//            val intent =
//                Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//            intent.data = Uri.fromFile(newFile)
//            context.applicationContext
//                .sendBroadcast(intent)
//            showDialogSweet(this@FileListActivity, SUCCESS_TYPE, "File name changed successfully!")
//        } else {
//            showDialogSweet(this@FileListActivity, ERROR_TYPE, "Change file name fail!")
//        }
//    }



}
