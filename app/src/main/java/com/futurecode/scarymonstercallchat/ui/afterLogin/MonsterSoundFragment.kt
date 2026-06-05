package com.futurecode.scarymonstercallchat.ui.afterLogin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.navigation.fragment.findNavController
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.base.BaseFragment
import com.futurecode.scarymonstercallchat.databinding.FragmentMonsterSoundBinding
import java.util.Locale
import android.media.MediaPlayer
import com.futurecode.scarymonstercallchat.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.scarymonstercallchat.ads.native_ad.NativeAdsHelper
import com.futurecode.scarymonstercallchat.utils.Utils.setAdClickListener


class MonsterSoundFragment : BaseFragment<FragmentMonsterSoundBinding>(FragmentMonsterSoundBinding::inflate) {

    private var monsterName: String? = null
    private var imageRes: Int = R.drawable.app_logo
    private var sound: Int = R.raw.crow

    // 1. Add variables for the sound ID and the MediaPlayer
    private var soundResId: Int = -1
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var nativeAdsHelper: NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nativeAdsHelper= NativeAdsHelper(requireActivity())
        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())

        monsterName = arguments?.getString("monsterName")
        imageRes = arguments?.getInt("imageRes", R.drawable.app_logo) ?: R.drawable.app_logo
       // sound = arguments?.getInt("sound")
        soundResId = arguments?.getInt("sound", -1) ?: -1

        setupUI()
        loadNativeAds()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI() {
        binding.customToolbar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.customToolbar.btnSettings.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            findNavController().navigate(R.id.settingsFragment)
        }

        // Set Monster Data
        binding.customToolbar.txtToolbarTitle.text = "${monsterName?.uppercase(Locale.getDefault())} SOUND"
        binding.tvSoundName.text = "${monsterName} Sound"
        binding.ivMonster.setImageResource(imageRes)

        // 3. Cleaned up Tap and Hold Logic
        binding.root.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // User pressed down
                    startPlayingSound()
                    binding.tvHint.alpha = 0.5f
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // User let go or dragged finger off screen
                    stopPlayingSound()
                    binding.tvHint.alpha = 1.0f
                    true
                }
                else -> false
            }
        }
    }

    // 4. Implement start sound
    private fun startPlayingSound() {
        // Only play if we have a valid sound ID and it's not already playing
        if (soundResId != -1 && mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(requireContext(), soundResId)
            mediaPlayer?.isLooping = true // Optional: Set to true if you want it to loop while holding
            mediaPlayer?.start()
        }
    }

    // 5. Implement stop sound
    private fun stopPlayingSound() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.stop()
            }
            player.release()
        }
        mediaPlayer = null
    }

    fun loadNativeAds(){
        nativeAdsHelper = NativeAdsHelper(requireActivity())
        nativeAdsHelper?.showNativeAd(
            nativeBannerAdView = binding.nativeAds3.frame,
            mainLayout = binding.nativeAds3.mainLayout,
            placeholder = binding.nativeAds3.placeholder
        )
    }

    // 6. VERY IMPORTANT: Stop sound if the user closes the app while holding down!
    override fun onPause() {
        super.onPause()
        stopPlayingSound()
    }
}