package com.futurecode.scarymonstercallchat.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.futurecode.scarymonstercallchat.activity.MyApplication
import com.futurecode.scarymonstercallchat.notification.InAppUtils
import com.futurecode.scarymonstercallchat.ui.afterLogin.PrankHomeCallFragment
import com.futurecode.scarymonstercallchat.ui.afterLogin.SelectPrankTypeFragment
import com.futurecode.scarymonstercallchat.ui.prelogin.SplashFragment
import com.futurecode.scarymonstercallchat.utils.PrefManager

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    protected val bindingOrNull: VB?
        get() = _binding

    protected val prefManager: PrefManager by lazy { MyApplication.app.prefManager }

    var distanceUnit = "km"
    var waterUnit = "ml"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Triggers the automated evaluation sequence as soon as the screen renders safely
        checkAndShowInAppBanner()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun showAds(flNative: FrameLayout) {
        if (!isAdded) return
    }

    private fun checkAndShowInAppBanner(){
        activity?.let { currentActivity ->
            val navHostFragment = currentActivity.supportFragmentManager
                .fragments
                .firstOrNull { it is NavHostFragment } as? NavHostFragment

            val currentFragment = navHostFragment
                ?.childFragmentManager
                ?.primaryNavigationFragment

            Log.d(
                "CurrentFragment",
                currentFragment?.javaClass?.simpleName ?: "No Fragment"
            )


            when (currentFragment) {

                is SelectPrankTypeFragment -> {
                    if (MyApplication.app.prefManager.clickCount == 0) {
                        InAppUtils.showInAppBanner(currentActivity, true)
                    }
                }

                !is SplashFragment -> {
                    InAppUtils.showInAppBanner(currentActivity, false)
                }
            }
        }
    }

}