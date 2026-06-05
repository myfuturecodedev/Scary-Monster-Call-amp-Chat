package com.futurecode.scarymonstercallchat.ui.afterLogin
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.base.BaseFragment
import com.futurecode.scarymonstercallchat.databinding.FragmentMonsterStartCallBinding
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.futurecode.scarymonstercallchat.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.scarymonstercallchat.ads.native_ad.NativeAdsHelper
import com.futurecode.scarymonstercallchat.utils.Utils.setAdClickListener

class MonsterStartCallFragment : BaseFragment<FragmentMonsterStartCallBinding>(FragmentMonsterStartCallBinding::inflate) {

    private var name: String? = ""
    private var image: Int? = 0
    private var videoResId: Int = -1

    // Hardware components
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    // Flashlight components
    private var cameraManager: CameraManager? = null
    private var cameraId: String? = null
    private var isFlashOn = false
    private val flashHandler = Handler(Looper.getMainLooper())
    private var flashRunnable: Runnable? = null

    // Preferences
    private var isSoundEnabled = true
    private var isVibrationEnabled = true
    private var isFlashEnabled = true

    private lateinit var nativeAdsHelper: NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nativeAdsHelper= NativeAdsHelper(requireActivity())
        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())

        name = arguments?.getString("monsterName")
        image = arguments?.getInt("imageRes")
        videoResId = arguments?.getInt("video", -1) ?: -1

        // 1. Load the user's settings
        loadSettings()

        // 2. Setup the UI
        setupUI()

        // 3. Start the alerts (Sound, Vibrate, Flash) based on settings
        startAlerts()
    }

    private fun loadSettings() {
        // CLEANED UP: Since BaseFragment gives us 'prefManager', we just read directly from it!
        // We DO NOT set them to true here, otherwise we overwrite the user's choices.
        isSoundEnabled = prefManager.isSoundEnabled
        isVibrationEnabled = prefManager.isVibrationEnabled
        isFlashEnabled = prefManager.isFlashEnabled
    }

    private fun setupUI() {
        binding.tvCallerName.text = name ?: "Unknown"

        if (image != null && image != 0) {
            binding.ivMonsterAvatar.setImageResource(image!!)
            binding.ivBackgroundBlur.setImageResource(image!!)
        }

        binding.btnAcceptCall.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            stopAlerts() // Stop ALL alerts before navigating
            val bundle = Bundle().apply {
                putString("monsterName", name)
                putInt("video", videoResId)
            }
            findNavController().navigate(R.id.action_monsterStartCallFragment_to_prankCallFragment, bundle)
        }

        binding.btnDeclineCall.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            stopAlerts() // Stop ALL alerts before navigating
            findNavController().navigateUp()
        }
    }

    // --- ALERT MANAGEMENT ---

    private fun startAlerts() {
        if (isSoundEnabled) playRingtone()
        if (isVibrationEnabled) startVibration()
        if (isFlashEnabled) startFlashlightBlink()
    }

    private fun stopAlerts() {
        stopRingtone()
        stopVibration()
        stopFlashlightBlink()
    }

    // --- 1. RINGTONE LOGIC ---
    private fun playRingtone() {
        try {
           // val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            val ringtoneUri = R.raw.ringtone_new
            mediaPlayer = MediaPlayer.create(requireContext(), ringtoneUri)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopRingtone() {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        mediaPlayer = null
    }

    // --- 2. VIBRATION LOGIC ---
    private fun startVibration() {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = requireContext().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (vibrator?.hasVibrator() == true) {
            // Pattern: Wait 0ms, Vibrate 1000ms, Wait 1000ms
            val pattern = longArrayOf(0, 1000, 1000)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // The '0' means repeat the pattern from index 0 forever
                vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(pattern, 0)
            }
        }
    }

    private fun stopVibration() {
        vibrator?.cancel()
    }

    // --- 3. FLASHLIGHT LOGIC ---
    private fun startFlashlightBlink() {
        try {
            cameraManager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
            cameraId = cameraManager?.cameraIdList?.firstOrNull() // Usually index 0 is the back camera with the flash

            if (cameraId != null) {
                flashRunnable = object : Runnable {
                    override fun run() {
                        isFlashOn = !isFlashOn
                        try {
                            cameraManager?.setTorchMode(cameraId!!, isFlashOn)
                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
                        }
                        // Toggle the flash every 500 milliseconds
                        flashHandler.postDelayed(this, 500)
                    }
                }
                flashHandler.post(flashRunnable!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopFlashlightBlink() {
        flashRunnable?.let { flashHandler.removeCallbacks(it) }
        try {
            if (cameraId != null && isFlashOn) {
                cameraManager?.setTorchMode(cameraId!!, false) // Ensure flash is turned OFF
                isFlashOn = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // CRITICAL: Stop everything if the user presses the system Back button
    override fun onDestroyView() {
        super.onDestroyView()
        stopAlerts()
    }
}


