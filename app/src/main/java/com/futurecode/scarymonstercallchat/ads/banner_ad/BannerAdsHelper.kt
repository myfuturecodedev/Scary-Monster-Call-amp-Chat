package com.futurecode.scarymonstercallchat.ads.banner_ad

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.activity.MyApplication
import com.futurecode.scarymonstercallchat.utils.PrefManager
import com.futurecode.scarymonstercallchat.utils.Utils
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError

class BannerAdsHelper(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var myPreferenceHelper: PrefManager = MyApplication.app.prefManager
    private var admobAdView: com.google.android.gms.ads.AdView? = null

    init {
        // Ensure the parent LinearLayout perfectly wraps the ad
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        showAds(context)
    }

    fun reload() {
        showAds(context)
    }

    fun showAds(context: Context) {
        if (!myPreferenceHelper.adsOff) {
            showAdmobBanner(context)
        } else {
            removeAllViews()
        }
    }

    private fun showCustomAd(context: Context) {
        removeAllViews()
        orientation = VERTICAL

        val imageView = ImageView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            setImageResource(R.drawable.banner)

            // FIX 1: Make the ImageView perfectly wrap the image dimensions without extra space
            adjustViewBounds = true

            // FIX 2: Fit center ensures the whole image shows without stretching the container
            scaleType = ImageView.ScaleType.FIT_CENTER

            // FIX 3: Removed setPadding(5,5,5,5) because that was literally adding bottom margin/padding!
        }

        addView(imageView)

        imageView.setOnClickListener {
            val activity = context as? Activity
            activity?.let { act ->
                Utils.openCustomChrome(act, Utils.getRandomUrls(act))
            }
        }
    }

    private fun showAdmobBanner(context: Context) {
        admobAdView?.destroy()

        admobAdView = com.google.android.gms.ads.AdView(context)

        removeAllViews()

        admobAdView?.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT // Keeps the AdView tight to the ad size
        )

        addView(admobAdView)

        val displayMetrics = resources.displayMetrics
        val adWidthPixels = displayMetrics.widthPixels.toFloat()
        val density = displayMetrics.density
        val adWidth = (adWidthPixels / density).toInt()

        val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
            context,
            adWidth
        )

        admobAdView?.setAdSize(adSize)
        admobAdView?.adUnitId = myPreferenceHelper.admobBanner

        admobAdView?.adListener = object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, "onAdFailedToLoad: ADMOB ${adError.message}")
                showCustomAd(context)
            }
        }

        val adRequest = AdRequest.Builder().build()
        admobAdView?.loadAd(adRequest)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        admobAdView?.destroy()
    }

    companion object {
        private const val TAG = "BannerAdManager"
    }
}