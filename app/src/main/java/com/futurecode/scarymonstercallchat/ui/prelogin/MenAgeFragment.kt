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
import com.futurecode.scarymonstercallchat.databinding.FragmentMenAgeBinding

class MenAgeFragment : BaseFragment<FragmentMenAgeBinding>(FragmentMenAgeBinding::inflate) {

    private lateinit var fullScreenAdsHelper: FullScreenAdsHelper
    private lateinit var nativeAdsHelper: NativeAdsHelper
    private var selectedAgeId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fullScreenAdsHelper = FullScreenAdsHelper(requireActivity())
        nativeAdsHelper= NativeAdsHelper(requireActivity())

        setupUI()
        loadNativeAds()
    }

    private fun setupUI() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnAge1.setOnClickListener { selectAge(1) }
        binding.btnAge2.setOnClickListener { selectAge(2) }
        binding.btnAge3.setOnClickListener { selectAge(3) }

        binding.btnNext.setOnClickListener {
            if (selectedAgeId != null) {
                fullScreenAdsHelper.showInterstitialAds(true, object : AdInterface {
                    override fun finished() {
                        findNavController().navigate(R.id.action_menAgeFragment_to_welcomeFragment)
                    }
                })
            } else {
                Toast.makeText(requireContext(), getString(R.string.please_select_age), Toast.LENGTH_SHORT).show()
            }
        }

        updateSelectionUI()
    }

    private fun selectAge(ageId: Int) {
        selectedAgeId = ageId
        updateSelectionUI()
    }

    private fun updateSelectionUI() {
        binding.ivAge1Indicator.setImageResource(if (selectedAgeId == 1) R.drawable.bg_gender_selected else R.drawable.bg_neon_circle)
        binding.ivAge2Indicator.setImageResource(if (selectedAgeId == 2) R.drawable.bg_gender_selected else R.drawable.bg_neon_circle)
        binding.ivAge3Indicator.setImageResource(if (selectedAgeId == 3) R.drawable.bg_gender_selected else R.drawable.bg_neon_circle)
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
}
