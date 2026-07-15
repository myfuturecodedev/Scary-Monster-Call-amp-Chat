package com.futurecode.scarymonstercallchat.ui.afterLogin

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.scarymonstercallchat.ads.native_ad.NativeAdsHelper
import com.futurecode.scarymonstercallchat.ads.reward.RewardAdsHelper
import com.futurecode.scarymonstercallchat.base.BaseFragment
import com.futurecode.scarymonstercallchat.databinding.FragmentMonsterSoundBinding
import com.futurecode.scarymonstercallchat.utils.Utils.setAdClickListener
import com.futurecode.scarymonstercallchat.utils.Utils.showRewardAdDialog
import java.util.Locale


//class MonsterSoundFragment : BaseFragment<FragmentMonsterSoundBinding>(FragmentMonsterSoundBinding::inflate) {
//
//    private var monsterName: String? = null
//    private var imageRes: Int = R.drawable.app_logo
//    private var sound: Int = R.raw.crow
//
//    // 1. Add variables for the sound ID and the MediaPlayer
//    private var soundResId: Int = -1
//    private var mediaPlayer: MediaPlayer? = null
//    private lateinit var nativeAdsHelper: NativeAdsHelper
//    lateinit var fullScreenAdsHelper: FullScreenAdsHelper
//
//    private var rewardsHelper: RewardAdsHelper? = null
//
//    // Tracking states for Reward execution flow
//    private var isRewardEarned = false
//    private var isAdFailedToLoadOrShow = false
//    private var scanJob: Job? = null
//
//    private var isFlashOn = false
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        nativeAdsHelper= NativeAdsHelper(requireActivity())
//        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())
//
//        monsterName = arguments?.getString("monsterName")
//        imageRes = arguments?.getInt("imageRes", R.drawable.app_logo) ?: R.drawable.app_logo
//       // sound = arguments?.getInt("sound")
//        soundResId = arguments?.getInt("sound", -1) ?: -1
//
//        setupUI()
//        loadNativeAds()
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    private fun setupUI() {
//        binding.customToolbar.btnBack.setOnClickListener {
//            findNavController().navigateUp()
//        }
//
//        binding.customToolbar.btnSettings.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
//            findNavController().navigate(R.id.settingsFragment)
//        }
//
//        // Set Monster Data
//        binding.customToolbar.txtToolbarTitle.text = "${monsterName?.uppercase(Locale.getDefault())} SOUND"
//        binding.tvSoundName.text = "${monsterName} Sound"
//        binding.ivMonster.setImageResource(imageRes)
//
//        // 3. Cleaned up Tap and Hold Logic
//        binding.root.setOnTouchListener { _, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    // User pressed down
//                    //startPlayingSound()
//                    showPremiumUnlockDialog()
//                    binding.tvHint.alpha = 0.5f
//                    true
//                }
//                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//                    // User let go or dragged finger off screen
//                    //stopPlayingSound()
//                    showPremiumUnlockDialog()
//                    binding.tvHint.alpha = 1.0f
//                    true
//                }
//                else -> false
//            }
//        }
//    }
//
//    // 4. Implement start sound
//    private fun startPlayingSound() {
//        // Only play if we have a valid sound ID and it's not already playing
//        if (soundResId != -1 && mediaPlayer == null) {
//            mediaPlayer = MediaPlayer.create(requireContext(), soundResId)
//            mediaPlayer?.isLooping = true // Optional: Set to true if you want it to loop while holding
//            mediaPlayer?.start()
//        }
//    }
//
//    // 5. Implement stop sound
//    private fun stopPlayingSound() {
//        mediaPlayer?.let { player ->
//            if (player.isPlaying) {
//                player.stop()
//            }
//            player.release()
//        }
//        mediaPlayer = null
//    }
//
//    fun loadNativeAds(){
//        nativeAdsHelper = NativeAdsHelper(requireActivity())
//        nativeAdsHelper?.showNativeAd(
//            nativeBannerAdView = binding.nativeAds3.frame,
//            mainLayout = binding.nativeAds3.mainLayout,
//            placeholder = binding.nativeAds3.placeholder
//        )
//    }
//
//
//    private fun showPremiumUnlockDialog() {
//        val currentContext = kotlin.context ?: return
//
//        val dialogView = layoutInflater.inflate(R.layout.dialog_premium_ad, null)
//
//        val alertDialog = AlertDialog.Builder(currentContext, R.style.CustomDialogTheme).create()
//        alertDialog.setView(dialogView)
//        alertDialog.setCanceledOnTouchOutside(false)
//
//        val btnYes = dialogView.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnYes)
//        val btnNo = dialogView.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnNo)
//
//        // YES Button Clicked -> Show Ad directly
//        btnYes.setOnClickListener {
//            alertDialog.dismiss()
//            isRewardEarned = false
//            isAdFailedToLoadOrShow = false
//            rewardsHelper?.showRewardAds(this) // Trigger your reward ad helper
//            startPlayingSound()
//        }
//
//        // NO Button Clicked -> Close dialog
//        btnNo.setOnClickListener {
//            alertDialog.dismiss()
//        }
//
//        alertDialog.show()
//
//        // Transparent background stretch fallback handler
//        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//    }
//
//
//    // 6. VERY IMPORTANT: Stop sound if the user closes the app while holding down!
//    override fun onPause() {
//        super.onPause()
//        stopPlayingSound()
//    }
//}


class MonsterSoundFragment :
    BaseFragment<FragmentMonsterSoundBinding>(FragmentMonsterSoundBinding::inflate) {

    private var monsterName: String? = null
    private var imageRes: Int = R.drawable.app_logo
    private var soundResId: Int = -1
    private var mediaPlayer: MediaPlayer? = null

    private lateinit var nativeAdsHelper: NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper
    private var rewardsHelper: RewardAdsHelper? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nativeAdsHelper = NativeAdsHelper(requireActivity())
        fullScreenAdsHelper = FullScreenAdsHelper(requireActivity())
        rewardsHelper = RewardAdsHelper(requireActivity())

        monsterName = arguments?.getString("monsterName")
        imageRes = arguments?.getInt("imageRes", R.drawable.app_logo) ?: R.drawable.app_logo
        soundResId = arguments?.getInt("sound", -1) ?: -1

        setupUI()
        loadNativeAds()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI() {
        bindingOrNull?.let { safeBinding ->
            safeBinding.customToolbar.btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            safeBinding.customToolbar.btnSettings.setAdClickListener(
                requireActivity(),
                fullScreenAdsHelper
            ) {
                if (isAdded) findNavController().navigate(R.id.settingsFragment)
            }

            // Set Monster Data
            safeBinding.customToolbar.txtToolbarTitle.text =
                "${monsterName?.uppercase(Locale.getDefault())} SOUND"
            safeBinding.tvSoundName.text = "${monsterName} Sound"
            safeBinding.ivMonster.setImageResource(imageRes)

            // 1. Agar user locked state me tvHint par click kare toh popup aaye
            safeBinding.tvHint.setOnClickListener {
                this@MonsterSoundFragment.showRewardAdDialog(onRewardEarned = {
                    bindingOrNull?.let { b ->
                        b.tvHint.text = "Hold to play sound!" // UI text updated
                        b.tvHint.setOnClickListener(null) // Purana click listener clean kiya
                    }
                    // Ad khatam hote hi direct instant sound trigger
                    startPlayingSound()
                }) {
                    Toast.makeText(requireContext(), "Ad failed to display", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            // 2. ivMonster Touch Listener (Locked vs Unlocked control)
            safeBinding.ivMonster.setOnTouchListener { _, event ->
                this@MonsterSoundFragment.showRewardAdDialog(onRewardEarned = {
                    bindingOrNull?.let { b ->
                        b.tvHint.text = "Hold to play sound!" // UI text updated
                        b.tvHint.setOnClickListener(null) // Purana click listener clean kiya
                    }
                    // Ad khatam hote hi direct instant sound trigger
                    startPlayingSound()
                }) {
                    Toast.makeText(requireContext(), "Ad failed to display", Toast.LENGTH_SHORT)
                        .show()
                }

                // Unlocked behavior: Tap and hold to play
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startPlayingSound()
                        safeBinding.tvHint.alpha = 0.5f
                        true
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        stopPlayingSound()
                        safeBinding.tvHint.alpha = 1.0f
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun startPlayingSound() {
        if (!isAdded || soundResId == -1) return
        if (mediaPlayer == null) {
            try {
                mediaPlayer = MediaPlayer.create(requireContext(), soundResId).apply {
                    isLooping = true
                    start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun stopPlayingSound() {
        mediaPlayer?.let { player ->
            try {
                if (player.isPlaying) player.stop()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                player.release()
            }
        }
        mediaPlayer = null
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

    override fun onPause() {
        super.onPause()
        stopPlayingSound()
    }

    override fun onDestroyView() {
        stopPlayingSound()
        super.onDestroyView()
    }

}