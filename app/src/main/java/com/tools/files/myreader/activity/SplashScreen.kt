package com.tools.files.myreader.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.all.me.io.helpers.utils.FileUtility
import com.all.me.io.helpers.utils.StorageUtils
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.tools.files.myreader.R
import io.me.ndk.adsconfig.LovinCoverAds
import io.me.ndk.adsconfig.LovinInterstitialAds
import io.me.ndk.adsconfig.callbacks.LovinInterstitialOnCallBack
import com.tools.files.myreader.base.App.Companion.checkOne
import com.tools.files.myreader.ulti.Action.SHORT_CUT_FILE_NAME
import com.tools.files.myreader.ulti.Action.SHORT_CUT_PAGE_NUM
import com.tools.files.myreader.ulti.SharedPreferencesUtility
import com.tools.files.myreader.ulti.Common.pushEventAnalytics
import io.me.ndk.adsconfig.util.Utils.lovinSplashInterstitial
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.io.File


class SplashScreen : AppCompatActivity() {
    private var mFile: File? = null
    private var mStorageUtils: StorageUtils? = null
    private var pageNum = -1
    var checkShow = false
    var checkLoad = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        if (!checkOne) {
            pushEventAnalytics("tutorial_begin")
        }
        SharedPreferencesUtility.setTimeRateCurrent(this, System.currentTimeMillis())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initAds()
        }
        btn_start.setOnClickListener {
            btn_start.visibility = View.INVISIBLE
            intentMethod()
        }

    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun initAds() {
        lovinSplashInterstitial = LovinInterstitialAds(this)
        lovinSplashInterstitial!!.loadAndShowInterstitialAd(getString(R.string.applovin_interstitial_main_ids),
            isRemoteConfigActive = true,
            isAppPurchased = false, mListener = object : LovinInterstitialOnCallBack {
                override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                    launchMain()
                }

                override fun onAdLoaded(maxAd: MaxAd?) {
                    checkLoad = true
                    if (checkShow) {
                        lovinSplashInterstitial?.showInterstitialAds()
                    }
                    Log.e("", "")
                }

                override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                    Log.e("", "")
                }

                override fun onAdDisplayed(maxAd: MaxAd?) {
                    Log.e("", "")
                }

                override fun onAdClicked(maxAd: MaxAd?) {
                    Log.e("", "")
                }

                override fun onAdHidden(maxAd: MaxAd?) {
                    launchMain()
                }
            })
    }

    private fun intentMethod() {
        checkShow = true
        if (checkLoad)
            lovinSplashInterstitial?.showInterstitialAds()

    }

    override fun onDestroy() {
        super.onDestroy()
        lovinSplashInterstitial?.destroyInterstitialAds()
    }

    private fun launchMain() {
        if (intent != null && intent.getStringExtra(SHORT_CUT_FILE_NAME) != null && intent.getStringExtra(
                SHORT_CUT_FILE_NAME
            ) != ""
        ) {
            try {
                val filepath = intent.getStringExtra(SHORT_CUT_FILE_NAME)
                mFile = File(filepath)
                mStorageUtils = StorageUtils(this)
                pageNum = intent.getIntExtra(SHORT_CUT_PAGE_NUM, -1)
                FileUtility.openFile(this, mFile!!, 0)
                finish()
            } catch (e: Exception) {
                finish()
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    val mIntent = Intent(this, MainActivity::class.java)
                    startActivity(mIntent)
                } else {
                    val mIntent = Intent(this, IntroductionActivity::class.java)
                    startActivity(mIntent)
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                        val mIntent = Intent(this, MainActivity::class.java)
                        startActivity(mIntent)

                    } else {
                        val mIntent = Intent(this, IntroductionActivity::class.java)
                        startActivity(mIntent)
                    }
                }
            }
            finish()
        }
    }
}
