package com.tools.files.myreader.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.all.me.io.helpers.utils.FileUtility
import com.all.me.io.helpers.utils.StorageUtils
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.google.android.gms.ads.LoadAdError
import com.tools.files.myreader.R
import com.tools.files.myreader.adsconfig.LovinBannerAds
import com.tools.files.myreader.adsconfig.LovinInterstitialAds
import com.tools.files.myreader.adsconfig.callbacks.LovinInterstitialOnCallBack
import com.tools.files.myreader.base.App.Companion.checkOne
import com.tools.files.myreader.ulti.Action.SHORT_CUT_FILE_NAME
import com.tools.files.myreader.ulti.Action.SHORT_CUT_PAGE_NUM
import com.tools.files.myreader.ulti.SharedPreferencesUtility
import com.tools.files.myreader.ulti.Common.pushEventAnalytics
import kotlinx.android.synthetic.main.activity_splash_screen.*
import kotlinx.coroutines.*
import java.io.File


class SplashScreen : AppCompatActivity() {
    private var mFile: File? = null
    private var mStorageUtils: StorageUtils? = null
    private lateinit var lovinSplashInterstitial: LovinInterstitialAds
    private lateinit var lovinBannerAds: LovinBannerAds
    private var pageNum = -1
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
    private fun initAds(){
        lovinSplashInterstitial = LovinInterstitialAds(this)
        lovinBannerAds = LovinBannerAds(this)
        lovinSplashInterstitial.loadAndShowInterstitialAd(getString(R.string.applovin_interstitial_splash_ids),
            isRemoteConfigActive = true,
            isAppPurchased = false, mListener = object: LovinInterstitialOnCallBack {
                override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {}

                override fun onAdLoaded(maxAd: MaxAd?) {}

                override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {}

                override fun onAdDisplayed(maxAd: MaxAd?) {}

                override fun onAdClicked(maxAd: MaxAd?) {}

                override fun onAdHidden(maxAd: MaxAd?) {
                    launchMain()
                }
            })
    }

    private fun intentMethod() {
        lovinSplashInterstitial.showInterstitialAds()
    }

    override fun onDestroy() {
        super.onDestroy()
        lovinSplashInterstitial.destroyInterstitialAds()
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
        }
        else {
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    val mIntent = Intent(this, MainActivity::class.java)
                    startActivity(mIntent)
                }else{
                    val mIntent = Intent(this, IntroductionActivity::class.java)
                    startActivity(mIntent)
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        val mIntent = Intent(this, MainActivity::class.java)
                        startActivity(mIntent)

                    }else{
                        val mIntent = Intent(this, IntroductionActivity::class.java)
                        startActivity(mIntent)
                    }
                }
            }
            finish()
        }
    }
}
