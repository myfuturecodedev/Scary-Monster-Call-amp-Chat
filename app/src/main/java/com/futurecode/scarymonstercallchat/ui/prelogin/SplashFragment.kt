package com.futurecode.scarymonstercallchat.ui.prelogin

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.navigation.fragment.findNavController
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.activity.MainActivity
import com.futurecode.scarymonstercallchat.base.BaseFragment
import com.futurecode.scarymonstercallchat.databinding.FragmentSplashBinding

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
    private var progressAnimator: ObjectAnimator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startLoadingAnimation()
    }

    private fun startLoadingAnimation() {
        // Design shows 100% progress
        val targetProgress = 100

        // Smoothly animate the progress bar
        progressAnimator = ObjectAnimator.ofInt(binding.progressBar, "progress", 0, targetProgress).apply {
            duration = 4000 // 4 seconds for a realistic load
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                val progress = it.animatedValue as Int
                bindingOrNull?.tvProgressPercent?.text = "$progress %"
            }
            start()
        }

        // Delay navigation to allow user to see the splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            if (prefManager.isLanguageSelectedFirstTime){
                (activity as? MainActivity)?.goToMain()
            }else{
                findNavController().navigate(R.id.action_splashFragment_to_languageFragment)
            }

        }, 4000)
    }

    override fun onDestroyView() {
        progressAnimator?.cancel() // Stop the animation
        progressAnimator = null
        super.onDestroyView()
    }
}