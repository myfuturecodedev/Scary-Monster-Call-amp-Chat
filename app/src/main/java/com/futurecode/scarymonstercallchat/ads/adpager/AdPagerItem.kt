package com.futurecode.scarymonstercallchat.ads.adpager

sealed class AdPagerItem<out T> {
    data class Content<T>(
        val contentIndex: Int,
        val data: T
    ) : AdPagerItem<T>()

    data class NativeAd(
        val pageKey: String,
        val adType: AdPagerAdType,
        val unlockDurationMs: Long
    ) : AdPagerItem<Nothing>()
}