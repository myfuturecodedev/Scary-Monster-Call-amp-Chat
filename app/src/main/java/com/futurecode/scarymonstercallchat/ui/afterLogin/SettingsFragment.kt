package com.futurecode.scarymonstercallchat.ui.afterLogin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.scarymonstercallchat.ads.native_ad.NativeAdsHelper
import com.futurecode.scarymonstercallchat.base.BaseFragment
import com.futurecode.scarymonstercallchat.databinding.FragmentSettingsBinding
import com.futurecode.scarymonstercallchat.utils.Utils.setAdClickListener
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private lateinit var nativeAdsHelper: NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        nativeAdsHelper= NativeAdsHelper(requireActivity())
        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())

        binding.customToolbar.txtToolbarTitle.text = resources.getString(R.string.settings)
        binding.customToolbar.btnSettings.visibility= View.GONE

        // 3. Call setupUI

        setupUI()
        loadNativeAds()
    }

    private fun setupUI() {
        binding.customToolbar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // --- Toggle Group ---

        binding.itemSound.apply {
            tvLabel.text = getString(R.string.sound)
            ivIcon.setImageResource(R.drawable.ic_sound)

            // Set initial state from PrefManager (removed hardcoded 'true')
            switchAction.isChecked = prefManager.isSoundEnabled
            Log.d("TAG", "setupUIisSoundEnabled:${prefManager.isSoundEnabled} ")

            // Save new state when changed
            switchAction.setOnCheckedChangeListener { _, isChecked ->
                prefManager.isSoundEnabled = isChecked
            }
        }

        binding.itemVibration.apply {
            tvLabel.text = getString(R.string.vibration)
            ivIcon.setImageResource(R.drawable.ic_vibration)

            switchAction.isChecked = prefManager.isVibrationEnabled
            Log.d("TAG", "setupUIisVibrationEnabled:${prefManager.isVibrationEnabled} ")

            switchAction.setOnCheckedChangeListener { _, isChecked ->
                prefManager.isVibrationEnabled = isChecked
            }
        }

        binding.itemFlash.apply {
            tvLabel.text = getString(R.string.flash)
            ivIcon.setImageResource(R.drawable.ic_flash)

            switchAction.isChecked = prefManager.isFlashEnabled
            Log.d("TAG", "setupUIisFlashEnabled:${prefManager.isFlashEnabled} ")


            switchAction.setOnCheckedChangeListener { _, isChecked ->
                prefManager.isFlashEnabled = isChecked
            }
        }

        // --- Navigation Group ---

        binding.itemLanguage.apply {
            tvLabel.text = getString(R.string.language)
            ivIcon.setImageResource(R.drawable.ic_language)
            tvValue.visibility = View.VISIBLE
            tvValue.text = getString(R.string.default_language)
            root.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
                findNavController().navigate(R.id.action_settingsFragment_to_languageFragment2)
            }
        }

        binding.itemRateUs.apply {
            tvLabel.text = getString(R.string.rate_us)
            ivIcon.setImageResource(R.drawable.ic_rate_us)
            root.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
                val uri = Uri.parse("market://details?id=${requireContext().packageName}")
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                try {
                    startActivity(goToMarket)
                } catch (e: Exception) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${requireContext().packageName}")))
                }
            }
        }

        binding.itemTerms.apply {
            tvLabel.text = getString(R.string.terms_of_service)
            ivIcon.setImageResource(R.drawable.ic_terms)
            root.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/monsterchatcallapp" ?: "")) // Replace with actual URL
                startActivity(browserIntent)
            }
        }

        binding.itemPrivacy.apply {
            tvLabel.text = getString(R.string.privacy_policy)
            ivIcon.setImageResource(R.drawable.ic_privacy)
            root.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(prefManager.privacyPolicy ?: "")) // Replace with actual URL
                startActivity(browserIntent)
            }
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
}