package com.futurecode.scarymonstercallchat.ui.prelogin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.ads.AdInterface
import com.futurecode.scarymonstercallchat.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.scarymonstercallchat.ads.native_ad.NativeAdsHelper
import com.futurecode.scarymonstercallchat.base.BaseFragment
import com.futurecode.scarymonstercallchat.databinding.FragmentGenderBinding

class GenderFragment : BaseFragment<FragmentGenderBinding>(FragmentGenderBinding::inflate) {

    private lateinit var nativeAdsHelper: NativeAdsHelper
    private lateinit var fullScreenAdsHelper: FullScreenAdsHelper
    private var isMaleSelected: Boolean? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nativeAdsHelper = NativeAdsHelper(requireActivity())
        fullScreenAdsHelper = FullScreenAdsHelper(requireActivity())

        setupUI()
        loadNativeAds()
    }

    private fun setupUI() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnMale.setOnClickListener {
            selectGender(true)
        }

        binding.btnFemale.setOnClickListener {
            selectGender(false)
        }

        binding.btnNext.setOnClickListener {
            if (isMaleSelected != null) {
                fullScreenAdsHelper.showInterstitialAds(true, object : AdInterface {
                    override fun finished() {
                        if (isMaleSelected == true) {
                            findNavController().navigate(R.id.action_genderFragment_to_menAgeFragment)
                        } else {
                            findNavController().navigate(R.id.action_genderFragment_to_femaleAgeFragment)
                        }
                    }
                })
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_select_gender),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Initial state
        updateSelectionUI()
    }

    private fun selectGender(isMale: Boolean) {
        isMaleSelected = isMale
        updateSelectionUI()
    }
    fun loadNativeAds() {
        bindingOrNull?.let { safeBinding ->
            nativeAdsHelper.showNativeAd(
                nativeBannerAdView = safeBinding.nativeAds3.frame,
                mainLayout = safeBinding.nativeAds3.mainLayout,
                placeholder = safeBinding.nativeAds3.placeholder
            )
        }
    }

    private fun updateSelectionUI() {
        if (isMaleSelected == true) {
            binding.ivMaleIndicator.setImageResource(R.drawable.bg_gender_selected)
            binding.ivFemaleIndicator.setImageResource(R.drawable.bg_neon_circle)
        } else if (isMaleSelected == false) {
            binding.ivMaleIndicator.setImageResource(R.drawable.bg_neon_circle)
            binding.ivFemaleIndicator.setImageResource(R.drawable.bg_gender_selected)
        } else {
            binding.ivMaleIndicator.setImageResource(R.drawable.bg_neon_circle)
            binding.ivFemaleIndicator.setImageResource(R.drawable.bg_neon_circle)
        }
    }
}