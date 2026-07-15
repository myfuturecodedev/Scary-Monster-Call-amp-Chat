package com.futurecode.scarymonstercallchat.activity

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.facebook.ads.AudienceNetworkAds
import com.futurecode.scarymonstercallchat.ads.app_open_ad.AppOpenHelperNew
import com.futurecode.scarymonstercallchat.utils.NetworkMonitor
import com.futurecode.scarymonstercallchat.utils.PrefManager

import com.google.android.gms.ads.MobileAds
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import java.util.Locale

class MyApplication : Application() {

    lateinit var prefManager: PrefManager
    // Use nullable to avoid lateinit exceptions if ads are off
    var appOpenHelper: AppOpenHelperNew? = null
    private lateinit var networkMonitor: NetworkMonitor
    private var currentActivity: Activity? = null
    private lateinit var analytics: FirebaseAnalytics


    override fun onCreate() {
        super.onCreate()
        app = this
        prefManager = PrefManager.get(this)
        analytics = Firebase.analytics
        applyLanguage(prefManager.selectedLanguage, persistSelection = false)

        Log.d("TAG", "prefManagerOffffff: ${prefManager.adsOff}")

        // 1. Setup Activity Tracker FIRST
        // This is required because NetworkMonitor needs getCurrentActivity() to show the dialog
        setupActivityTracker()

        // 2. Initialize and Start Network Monitoring
        networkMonitor = NetworkMonitor(this)
        networkMonitor.startMonitoring()

        // 3. General Initializations
       // JsonReadUtils.fetchJsonData(this)
        initializeADS()


    }

    private fun setupActivityTracker() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityStarted(activity: Activity) {
                currentActivity = activity
            }

            override fun onActivityStopped(activity: Activity) {
                if (currentActivity == activity) {
                    currentActivity = null
                }
            }

            override fun onActivityCreated(p0: Activity, p1: Bundle?) {}
            override fun onActivityResumed(p0: Activity) {}
            override fun onActivityPaused(p0: Activity) {}
            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
            override fun onActivityDestroyed(p0: Activity) {}
        })
    }

    fun getCurrentActivity(): Activity? = currentActivity

    private fun initializeADS() {
        AudienceNetworkAds.initialize(this)
        MobileAds.initialize(this) {

        }

        // Setup Helper only if ads are enabled
        if (!prefManager.adsOff) {
            appOpenHelper = AppOpenHelperNew(this)
//            appOpenHelper?.fetchAd()
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        if (::networkMonitor.isInitialized) {
            networkMonitor.stopMonitoring()
        }
    }

    companion object {
        private const val DEFAULT_LANGUAGE = "en"
        lateinit var app: MyApplication

        fun applyLanguage(languageCode: String, persistSelection: Boolean = true) {
            val normalizedLanguageCode = languageCode.ifBlank { DEFAULT_LANGUAGE }

            if (persistSelection) {
                app.prefManager.selectedLanguage = normalizedLanguageCode
                app.prefManager.isLanguageSelectedFirstTime = true
            }

            Locale.setDefault(Locale.forLanguageTag(normalizedLanguageCode))

            val appLocales = LocaleListCompat.forLanguageTags(normalizedLanguageCode)
            if (AppCompatDelegate.getApplicationLocales().toLanguageTags() != appLocales.toLanguageTags()) {
                AppCompatDelegate.setApplicationLocales(appLocales)
            }
        }
    }
}
