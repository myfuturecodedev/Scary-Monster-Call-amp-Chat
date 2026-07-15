package com.futurecode.scarymonstercallchat.ads.ads_new

import com.futurecode.scarymonstercallchat.databinding.ItemOnboardingNativeAdBinding


class NativeAdPagerController(
    private val nativeAdPageLoader: NativeAdPageLoader
) {

    fun bind(pageKey: String, binding: ItemOnboardingNativeAdBinding) {
        if (binding.root.tag == pageKey) return
        binding.root.tag = pageKey
        nativeAdPageLoader.load(binding)
    }
}