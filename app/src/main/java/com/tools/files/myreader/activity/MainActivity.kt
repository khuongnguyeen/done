package com.tools.files.myreader.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.all.me.io.helpers.utils.FileUtility
import com.all.me.io.helpers.utils.StorageUtils
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.suddenh4x.ratingdialog.AppRating
import com.suddenh4x.ratingdialog.preferences.RatingThreshold
import com.tools.files.myreader.BuildConfig
import com.tools.files.myreader.R
import com.tools.files.myreader.adapter.RecyclerViewAdapter
import com.tools.files.myreader.adsconfig.LovinBannerAds
import com.tools.files.myreader.adsconfig.LovinInterstitialAds
import com.tools.files.myreader.adsconfig.callbacks.LovinInterstitialOnCallBack
import com.tools.files.myreader.base.App.Companion.count
import com.tools.files.myreader.base.BaseActivity
import com.tools.files.myreader.customview.OnSingleClickListener
import com.tools.files.myreader.internal.Statics
import com.tools.files.myreader.ocr.CameraActivity
import com.tools.files.myreader.ulti.Action
import com.tools.files.myreader.ulti.Common
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.item_main_2.*
import kotlinx.android.synthetic.main.item_toolmain.*
import java.io.File
import java.util.ArrayList
import kotlin.system.exitProcess

@Suppress("DEPRECATION")
class MainActivity : BaseActivity() {
    val data = mutableListOf<File>()
    private lateinit var lovinBannerAds: LovinBannerAds

    companion object {
        private const val REQUEST_CAMERA_PERMISSIONS = 273
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        lovinInterstitialAds = LovinInterstitialAds(this)
        lovinBannerAds = LovinBannerAds(this)
        // load interstitial ads
        lovinInterstitialAds.loadShowAndLoadInterstitialAd(getString(R.string.applovin_interstitial_main_ids),
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

    private fun initView() {
        queryDataFile()
        initRateApp()
        navigationViewInit()
        setupTools()
    }

    @SuppressLint("SetTextI18n")
    private fun setupTools() {
        txt_remove_ad.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                clickUpgradeNow()
            }
        })

        all_file.setOnClickListener(
            object : OnSingleClickListener() {
                override fun onSingleClick(v: View?) {
                    onCheckPremium("all")
                }
            })
        ll_pdf.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                onCheckPremium("pdf")
            }
        })
        ll_word.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                onCheckPremium("word")
            }
        })
        ll_excel.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                onCheckPremium("xls")
            }
        })
        ll_pp.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                onCheckPremium("ppt")
            }
        })
        ll_text.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                onCheckPremium("txt")
            }
        })
        ll_favourite.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                onCheckPremium("Favourite")
            }
        })

        ct_search.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                clickSearch()
            }
        })

        cam_scanner.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                onAddCameraClicked()
            }
        })

        create_office.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                val intent = Intent(this@MainActivity, CreateFileActivity::class.java)
                intent.putExtra("CREATE_FILE", 0)
                startActivity(intent)
            }
        })

        ll_pdf_to_image.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                count++

                startSelectPDFActivity()

            }
        })

        ll_image_to_pdf.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                count++
                startActivity(
                    Intent(
                        applicationContext,
                        GalleryActivity::class.java
                    )
                )

            }
        })
    }

    fun onAddCameraClicked() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            val permissions = arrayOf(Manifest.permission.CAMERA)
            val permissionsToRequest: MutableList<String> = java.util.ArrayList()
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsToRequest.add(permission)
                }
            }
            if (permissionsToRequest.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toTypedArray(),
                    REQUEST_CAMERA_PERMISSIONS
                )
            } else addCamera()
        } else {
            addCamera()
        }
    }

    private fun addCamera() {
        lovinInterstitialAds.showInterstitialAds()
        val intent = Intent(this@MainActivity, CameraActivity::class.java)
        startActivity(intent)
    }

    fun startSelectPDFActivity() {
        startActivity(
            Intent(
                this,
                SelectPDFActivity::class.java
            ).putExtra(Action.ACTION_INTENT, Action.PDF_TO_IMG).putExtra(
                Action.ST_IT,
                Action.ST_ALL
            )
        )
    }

    private fun clickSearch() {
        lovinInterstitialAds.showInterstitialAds()
        Common.pushEventAnalytics("view_search")
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    private fun clickUpgradeNow() {
        Common.pushEventAnalytics("click_upgrade")

    }

    fun restartApp() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        finish()
    }

    private fun onCheckPremium(s: String) {
        openFile(s)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_PERMISSIONS) {
            if (grantResults.size > 0 && permissions[0] == Manifest.permission.CAMERA) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addCamera()
                } else {
                    onAddCameraClicked()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun openFile(type: String) {
        lovinInterstitialAds.showInterstitialAds()
        Common.pushEventAnalytics("view_$type")
        val intent = Intent()
        if (type == "Favourite") {
            intent.setClass(this, FragmentActivity::class.java)
        } else {
            intent.setClass(this, FileListActivity::class.java)
        }
        intent.putExtra("type", type)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        applicationContext.getSharedPreferences("PDFtools", MODE_PRIVATE).edit()
            .putString("sessionName", Statics.randString()).apply()
        txt_remove_ad.visibility = View.INVISIBLE
        rv_recent_main!!.layoutManager = LinearLayoutManager(this)
        val dataCache = mutableListOf<File>()
        if (StorageUtils(this).getRecent(this) != null)
            dataCache.addAll(StorageUtils(this).getRecent(this))

        if (dataCache.size >= 10) {
            data.clear()
            for (i in 0..9) {
                data.add(dataCache[i])
            }
        } else {
            data.clear()
            data.addAll(dataCache)
        }
        if (data.size > 0) {
            rl_nodt_1.visibility = View.GONE
        }
        adapter = RecyclerViewAdapter(data as MutableList<Any>, this, this, true)
        rv_recent_main!!.adapter = adapter
    }

    @SuppressLint("ResourceType")
    private fun initRateApp() {
        AppRating.Builder(this)
            .useGoogleInAppReview()
            .setGoogleInAppReviewCompleteListener { successful ->
//                Toast.makeText(this,"Demo $successful",Toast.LENGTH_SHORT).show()
            }
            .setMinimumLaunchTimes(1)
            .setMinimumDays(1)
            .setMinimumLaunchTimesToShowAgain(2)
            .setMinimumDaysToShowAgain(1)
            .setRatingThreshold(RatingThreshold.FOUR)
            .showIfMeetsConditions()

    }

    private fun navigationViewInit() {
        setSupportActionBar(toolbar_main)
        supportActionBar!!.title = ""
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar_main,
            R.string.nav_open,
            R.string.nav_close
        )
        toggle.drawerArrowDrawable.color = Color.WHITE
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        rll_rate_app.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                rateApp()
            }
        })
        rll_share_app.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                shareApp()
            }
        })
        rl_about.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                startActivity(Intent(this@MainActivity, AboutActivity::class.java))
            }
        })
    }


    private fun shareApp() {
        try {
            val start = System.currentTimeMillis()
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
            val sb = StringBuilder()
            sb.append("\nLet me recommend you this application\n\n")
            sb.append("https://play.google.com/store/apps/details?id=")
            sb.append(BuildConfig.APPLICATION_ID + "\n\n")
            shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString())
            startActivity(Intent.createChooser(shareIntent, "choose one"))
            Log.e("ststst", "${System.currentTimeMillis() - start}")
        } catch (unused: Exception) {
            unused.printStackTrace()
        }
    }

    private fun rateApp() {
        try {
            val uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    "android.intent.action.VIEW",
                    Uri.parse("http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                )
            )
        }
    }

    var i = 0

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)

        if (!drawer_layout.isDrawerOpen(GravityCompat.START)) {
            finishAffinity()
            exitProcess(0)
        } else {
            i += 1
            if (i >= 2) {
                val intent = Intent(Intent.ACTION_MAIN)
                with(intent) {
                    addCategory(Intent.CATEGORY_HOME)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(this)
                }
                finishAffinity()
                exitProcess(0)
            } else {
                Toast.makeText(this, getString(R.string.exit1), Toast.LENGTH_SHORT).show()
                Handler().postDelayed({ i = 0 }, 2000)
            }
        }
    }

    private fun queryDataFile() {
        Observable.create { emitter: ObservableEmitter<ArrayList<File>> ->
            FileUtility.mAllFileOffice.clear()
            FileUtility.mWordFiles.clear()
            FileUtility.mPdfFiles.clear()
            FileUtility.mExcelFiles.clear()
            FileUtility.mPowerPointFiles.clear()
            FileUtility.mListTxtFile.clear()
            emitter.onNext(FileUtility.getAllFile(FileUtility.mDir))
            emitter.onComplete()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ArrayList<File>> {
                override fun onSubscribe(d: Disposable) {}

                override fun onNext(files: ArrayList<File>) {
                    if (files.size > 0) {
                        files.sortWith { row1, row2 ->
                            if (row1.lastModified() < row2.lastModified())
                                return@sortWith 1
                            if (row1.lastModified() == row2.lastModified()) return@sortWith 0
                            -1
                        }
                    }
                }

                override fun onError(e: Throwable) {}

                override fun onComplete() {}
            })
    }
}

