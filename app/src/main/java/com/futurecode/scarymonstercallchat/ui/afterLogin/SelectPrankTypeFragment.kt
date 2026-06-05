package com.futurecode.scarymonstercallchat.ui.afterLogin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.activity.MyApplication
import com.futurecode.scarymonstercallchat.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.scarymonstercallchat.ads.native_ad.NativeAdsHelper
import com.futurecode.scarymonstercallchat.base.BaseFragment
import com.futurecode.scarymonstercallchat.databinding.FragmentSelectPrankTypeBinding
import com.futurecode.scarymonstercallchat.utils.NotificationPermissionHelper
import com.futurecode.scarymonstercallchat.utils.Utils.setAdClickListener

class SelectPrankTypeFragment : BaseFragment<FragmentSelectPrankTypeBinding>(FragmentSelectPrankTypeBinding::inflate) {
    private lateinit var nativeAdsHelper: NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper

    // 1. Initialize the helper here.
    // It must be created at the class level so the activity result launcher registers safely.

    private val notificationPermissionHelper = NotificationPermissionHelper(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nativeAdsHelper= NativeAdsHelper(requireActivity())
        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())

        setupMenuButtons()
        loadNativeAds()
        // 2. Call the permission check whenever the screen opens
        notificationPermissionHelper.checkAndRequestPermission()
        Log.d("TAG", "checkAndShowInAppBanner: ${MyApplication.app.prefManager.clickCount}")

    }

    private fun setupMenuButtons() {
        binding.btnPrankCall.apply {
           // tvActionTitle.text = "Prank Call"
            tvActionTitle.text = getString(R.string.prank_call)
           // tvActionDesc.text = "Summon a scary beast"
            tvActionDesc.text = getString(R.string.summon_a_scary_beast)
            ivActionIcon.setImageResource(R.drawable.ic_call)
            root.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
                navigateToHome("call")
            }
        }

        binding.btnScaryChat.apply {
            tvActionTitle.text = getString(R.string.scary_chat)
            tvActionDesc.text = getString(R.string.text_with_the_unknown)
            ivActionIcon.setImageResource(R.drawable.ic_chat)
            root.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
                navigateToHome("chat")
            }
        }

        binding.btnHorrorSounds.apply {
            tvActionTitle.text = getString(R.string.horror_sounds)
            tvActionDesc.text = getString(R.string.eerie_ambient_noises)
            ivActionIcon.setImageResource(R.drawable.horror_sound)
            root.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
                navigateToHome("sound")
            }
        }

        binding.btnSettings.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            findNavController().navigate(R.id.settingsFragment)
        }
    }

    fun loadNativeAds(){
        nativeAdsHelper = NativeAdsHelper(requireActivity())
        nativeAdsHelper?.showNativeAd(
            nativeBannerAdView = binding.nativeAds3.frame,
            mainLayout = binding.nativeAds3.mainLayout,
            placeholder = binding.nativeAds3.placeholder
        )
    }


    private fun navigateToHome(type: String) {
        val bundle = bundleOf("prankType" to type)
        findNavController().navigate(R.id.action_selectPrankTypeFragment_to_prankHomeCallFragment, bundle)
    }
}