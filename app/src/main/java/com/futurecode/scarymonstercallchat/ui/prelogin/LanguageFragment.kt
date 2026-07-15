package com.futurecode.scarymonstercallchat.ui.prelogin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.activity.MyApplication
import com.futurecode.scarymonstercallchat.adapter.LanguageAdapter
import com.futurecode.scarymonstercallchat.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.scarymonstercallchat.ads.native_ad.NativeAdsHelper
import com.futurecode.scarymonstercallchat.base.BaseFragment
import com.futurecode.scarymonstercallchat.databinding.FragmentLanguageBinding
import com.futurecode.scarymonstercallchat.model.LanguageModel

class LanguageFragment : BaseFragment<FragmentLanguageBinding>(FragmentLanguageBinding::inflate) {

    private val languages = mutableListOf<LanguageModel>()
    private lateinit var languageAdapter: LanguageAdapter
    private lateinit var nativeAdsHelper: NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper
    private var from = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        from = arguments?.getString("from") ?: ""

        binding.customToolbar.txtToolbarTitle.text = getString(R.string.language)
        nativeAdsHelper = NativeAdsHelper(requireActivity())
        fullScreenAdsHelper = FullScreenAdsHelper(requireActivity())

        binding.customToolbar.btnSettings.visibility = View.GONE

        populateLanguages()

        Log.d("TAG", "onViewCreated: $from")

        val languagesWithAds = mutableListOf<Any>()
        languages.forEachIndexed { index, languageModel ->
            languagesWithAds.add(languageModel)
            if (index == 2) {
                languagesWithAds.add("AD_UNIT")
            }
        }

        languageAdapter = LanguageAdapter(requireActivity(), languagesWithAds) { selectedLanguage ->
            applySelectedLanguage(selectedLanguage)
            navigateAfterLanguageSelection()
        }

        setupRecyclerView()
        setupClickListeners()
    }

    private fun applySelectedLanguage(selectedLanguage: LanguageModel) {
        MyApplication.applyLanguage(selectedLanguage.code)
    }

    private fun navigateAfterLanguageSelection() {
        if (from == SOURCE_AUTH) {
            findNavController().navigate(R.id.action_languageFragment_to_onBoardingFragment)
        } else {
            findNavController().navigateUp()
        }
    }

    private fun populateLanguages() {
        val currentLang = prefManager.selectedLanguage

        languages.clear()
        // The 4th parameter (currentLang == "...") determines the `isSelected` boolean
        languages.add(LanguageModel(getString(R.string.english), "en", R.drawable.eng, currentLang == "en"))
        languages.add(LanguageModel(getString(R.string.spanish), "es", R.drawable.spanish_flag, currentLang == "es"))
        languages.add(LanguageModel(getString(R.string.french), "fr", R.drawable.french_flag, currentLang == "fr"))
        languages.add(LanguageModel(getString(R.string.portuguese), "pt", R.drawable.portuguese_glag, currentLang == "pt"))
        languages.add(LanguageModel(getString(R.string.german), "de", R.drawable.german_flag, currentLang == "de"))
        languages.add(LanguageModel(getString(R.string.turkish), "tr", R.drawable.turkish_flag, currentLang == "tr"))
        languages.add(LanguageModel(getString(R.string.korean), "ko", R.drawable.korean_flag, currentLang == "ko"))
        languages.add(LanguageModel(getString(R.string.japanese), "ja", R.drawable.japanese_flag, currentLang == "ja"))
    }

    private fun setupRecyclerView() {
        binding.rvLanguages.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = languageAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupClickListeners() {
        binding.customToolbar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private companion object {
        const val SOURCE_AUTH = "auth"
    }
}
