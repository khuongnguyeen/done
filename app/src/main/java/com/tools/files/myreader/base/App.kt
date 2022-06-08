package com.tools.files.myreader.base

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.kochava.base.Tracker
import com.onesignal.*
import com.tools.files.myreader.R
import com.tools.files.myreader.ulti.Common.loadData

import com.artifex.sonui.MainApp

class App : MainApp(){

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)

        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        context = applicationContext
        Tracker.configure(
            Tracker.Configuration(applicationContext)
                .setAppGuid("koall-document-reader-mx6qytki")
                .setLogLevel(Tracker.LOG_LEVEL_NONE)
        )
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        checkOne = loadData(applicationContext, "checkOne")

        val handler: OSInAppMessageLifecycleHandler = object : OSInAppMessageLifecycleHandler() {
            override fun onWillDisplayInAppMessage(message: OSInAppMessage) {
                OneSignal.onesignalLog(
                    OneSignal.LOG_LEVEL.VERBOSE,
                    "MainApplication onWillDisplayInAppMessage"
                )
            }

            override fun onDidDisplayInAppMessage(message: OSInAppMessage) {
                OneSignal.onesignalLog(
                    OneSignal.LOG_LEVEL.VERBOSE,
                    "MainApplication onDidDisplayInAppMessage"
                )
            }

            override fun onWillDismissInAppMessage(message: OSInAppMessage) {
                OneSignal.onesignalLog(
                    OneSignal.LOG_LEVEL.VERBOSE,
                    "MainApplication onWillDismissInAppMessage"
                )
            }

            override fun onDidDismissInAppMessage(message: OSInAppMessage) {
                OneSignal.onesignalLog(
                    OneSignal.LOG_LEVEL.VERBOSE,
                    "MainApplication onDidDismissInAppMessage"
                )
            }
        }

        OneSignal.setInAppMessageLifecycleHandler(handler)

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        OneSignal.setAppId(applicationContext.getString(R.string.ONESIGNAL_APP_ID))
        OneSignal.initWithContext(this)

        OneSignal.setNotificationOpenedHandler { result: OSNotificationOpenedResult ->
            OneSignal.onesignalLog(
                OneSignal.LOG_LEVEL.VERBOSE,
                "OSNotificationOpenedResult result: $result"
            )
        }

        OneSignal.setNotificationWillShowInForegroundHandler { notificationReceivedEvent: OSNotificationReceivedEvent ->
            OneSignal.onesignalLog(
                OneSignal.LOG_LEVEL.VERBOSE, "NotificationWillShowInForegroundHandler fired!" +
                        " with notification event: " + notificationReceivedEvent.toString()
            )
            val notification = notificationReceivedEvent.notification
            notification.additionalData
            notificationReceivedEvent.complete(notification)
        }

        OneSignal.unsubscribeWhenNotificationsAreDisabled(true)
        OneSignal.setLocationShared(false)

        val oneSignalUserID = OneSignal.getDeviceState()!!.userId
        Log.e("oneSignalUserID","oneSignalUserID--------------- $oneSignalUserID")
    }

    companion object {
        var checkOne = false
        var count = 0
        val isLoadedFile = MutableLiveData<Int>()
        val isSizeFavourite = MutableLiveData<Int>()

        private lateinit var mFirebaseAnalytics: FirebaseAnalytics
        fun getAnalytics() = mFirebaseAnalytics
        lateinit var remoteConfig: FirebaseRemoteConfig
        var TAG = "admob_ad"
        private var context: Context? = null
    }


}