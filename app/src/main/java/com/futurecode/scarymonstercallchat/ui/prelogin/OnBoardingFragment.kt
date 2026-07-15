package com.futurecode.scarymonstercallchat.ui.prelogin

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.activity.MyApplication
import com.futurecode.scarymonstercallchat.adapter.OnBoardingAdapter
import com.futurecode.scarymonstercallchat.ads.adpager.AdEnabledPagerMapper
import com.futurecode.scarymonstercallchat.ads.adpager.AdPagerItem
import com.futurecode.scarymonstercallchat.ads.adpager.AdPagerPlacementConfig
import com.futurecode.scarymonstercallchat.ads.adpager.AdPagerTimerController
import com.futurecode.scarymonstercallchat.ads.ads_new.ExistingNativeAdPageLoader
import com.futurecode.scarymonstercallchat.ads.ads_new.NativeAdPagerController
import com.futurecode.scarymonstercallchat.base.BaseFragment
import com.futurecode.scarymonstercallchat.databinding.FragmentOnBoardingBinding
import com.futurecode.scarymonstercallchat.model.OnBoardingModel

class OnBoardingFragment : BaseFragment<FragmentOnBoardingBinding>(FragmentOnBoardingBinding::inflate) {

    private lateinit var onboardingAdapter: OnBoardingAdapter
    private var timerController: AdPagerTimerController? = null

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (::onboardingAdapter.isInitialized) {
                onboardingAdapter.onPageSelected(position)
                updateUIForPosition(position)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        // 1. Structural images list mapped via pure background model specs
        val onboardingList = listOf(
            OnBoardingModel(R.drawable.onboard_one),
            OnBoardingModel(R.drawable.onboard_two),
            OnBoardingModel( R.drawable.onboard_three)
        )



        // 1. Build Ad placement mapping patterns configuration
        var isAdsEnabled = !MyApplication.app.prefManager.adsOff

        val adConfig = AdPagerPlacementConfig(
            adsEnabled = isAdsEnabled,
            timerAdPagerPositions = setOf(1, 3), // Positions where native ads inject automatically
            timerUnlockDurationMs = 3_000L
        )

        val pagerItems = AdEnabledPagerMapper.build(onboardingList, adConfig)
        val nativeAdPagerController = NativeAdPagerController(
            ExistingNativeAdPageLoader(requireActivity())
        )

        val pagerTimerController = AdPagerTimerController()
        timerController = pagerTimerController

        // 3. Setup Compatible Adapter
        onboardingAdapter = OnBoardingAdapter(
            activity = requireActivity(),
            list = pagerItems,
            nativeAdPagerController = nativeAdPagerController,
            timerController = pagerTimerController,
            onAdAdvanceRequested = { position ->
                onboardingAdapter.nextPositionAfter(position)?.let { nextPosition ->
                    bindingOrNull?.viewPager?.setCurrentItem(nextPosition, true)
                }
            },
            onContentContinueRequested = { position ->
                handleContinue(position)
            }
        )

        binding.viewPager.adapter = onboardingAdapter
        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback)

        // 4. Next/Forward Button triggers adapter continue execution flow
        binding.btnNext.setOnClickListener {
            handleContinue(binding.viewPager.currentItem)
        }

        binding.viewPager.post {
            if (::onboardingAdapter.isInitialized) {
                val currentPos = bindingOrNull?.viewPager?.currentItem ?: return@post
                onboardingAdapter.onPageSelected(currentPos)
                updateUIForPosition(currentPos)
            }
        }
    }

    private fun handleContinue(position: Int) {
        if (onboardingAdapter.isLastContentPage(position)) {
            // Flow finished -> Switch to target gender destination layout safely
            if (isAdded) {
                try {
                    findNavController().navigate(R.id.action_onBoardingFragment_to_genderFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            onboardingAdapter.nextPositionAfter(position)?.let { nextPosition ->
                bindingOrNull?.viewPager?.setCurrentItem(nextPosition, true)
            }
        }
    }

    // Dynamic UI states handling to display/hide indicators and Next button on Native Ad Pages
    private fun updateUIForPosition(position: Int) {
        if (!isAdded) return // Fragment check guard

        val item = onboardingAdapter.list.getOrNull(position)
        bindingOrNull?.let { safeBinding ->
            if (item is AdPagerItem.Content) {
                safeBinding.btnNext.visibility = View.VISIBLE
                safeBinding.llIndicators.visibility = View.VISIBLE

                updateIndicators(position)
            } else {
                safeBinding.btnNext.visibility = View.GONE
                safeBinding.llIndicators.visibility = View.GONE
            }
        }
    }

    // ✅ FIXED PILL INDICATOR CODE SYNCHRONIZED WITH AD INTERPOLATIONS
    private fun updateIndicators(position: Int) {
        val density = resources.displayMetrics.density
        val selectedWidth = (24 * density).toInt()
        val unselectedSize = (8 * density).toInt()

        val safeBinding = bindingOrNull ?: return
        val indicators = listOf(safeBinding.indicator1, safeBinding.indicator2, safeBinding.indicator3)

        // Maps absolute ViewPager index to actual localized operational content indices only
        val contentIndices = onboardingAdapter.list.indices.filter { onboardingAdapter.list[it] is AdPagerItem.Content }
        val visualActiveIndex = contentIndices.indexOf(position)

        indicators.forEachIndexed { index, view ->
            val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
            if (index == visualActiveIndex) {
                layoutParams.width = selectedWidth
                layoutParams.height = unselectedSize
                view.setBackgroundResource(R.drawable.bg_next_button)
            } else {
                layoutParams.width = unselectedSize
                layoutParams.height = unselectedSize
                view.setBackgroundResource(R.drawable.bg_indicator_unselected)
            }
            view.layoutParams = layoutParams
        }
    }

    override fun onDestroyView() {
        bindingOrNull?.viewPager?.unregisterOnPageChangeCallback(pageChangeCallback)
        if (::onboardingAdapter.isInitialized) {
            onboardingAdapter.release()
        }
        timerController?.clear()
        timerController = null
        super.onDestroyView()
    }
}
