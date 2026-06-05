package com.futurecode.scarymonstercallchat.ui.afterLogin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.base.BaseFragment
import com.futurecode.scarymonstercallchat.databinding.FragmentPrankCallBinding
import java.util.Locale
import android.net.Uri
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import android.media.MediaPlayer
import com.futurecode.scarymonstercallchat.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.scarymonstercallchat.ads.native_ad.NativeAdsHelper
import com.futurecode.scarymonstercallchat.utils.Utils.setAdClickListener

class PrankCallFragment : BaseFragment<FragmentPrankCallBinding>(FragmentPrankCallBinding::inflate) {

    private var seconds = 0
    private val handler = Handler(Looper.getMainLooper())
    private var isVideoOn = true
    private var isSpeakerOn = true // Set to true so sound plays initially
    private var name: String? = ""

    // Variable to hold the media player so we can mute/unmute it
    private var mediaPlayer: MediaPlayer? = null
    private var videoResId: Int = -1

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "Camera permission is required for selfie preview", Toast.LENGTH_SHORT).show()
                binding.cvUserPreview.visibility = View.GONE
            }
        }

    private val runnable = object : Runnable {
        override fun run() {
            seconds++
            val minutes = (seconds % 3600) / 60
            val secs = seconds % 60
            val time = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs)
            binding.tvTimer.text = time
            handler.postDelayed(this, 1000)
        }
    }

    private lateinit var nativeAdsHelper: NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nativeAdsHelper= NativeAdsHelper(requireActivity())
        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())

        // Set the caller name from arguments
        name = arguments?.getString("monsterName")
        videoResId = arguments?.getInt("video", -1) ?: -1

        Log.d("TAG", "onViewCreatedvvvvvvvvv: $videoResId")

        binding.tvCallerName.text = name

        setupUI()
        setupVideo()
        startTimer()
        checkCameraPermissionAndStart()
    }

    private fun setupUI() {
        binding.btnEndCall.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            endCallAndNavigate()
        }

        binding.btnVideo.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            isVideoOn = !isVideoOn
            // Toggle the visibility of the camera preview
            binding.cvUserPreview.visibility = if (isVideoOn) View.VISIBLE else View.GONE
            binding.btnVideo.alpha = if (isVideoOn) 1.0f else 0.5f
        }

        binding.btnSpeaker.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            isSpeakerOn = !isSpeakerOn
            binding.btnSpeaker.alpha = if (isSpeakerOn) 1.0f else 0.5f

            // Mute or Unmute the video playback
            if (isSpeakerOn) {
                mediaPlayer?.setVolume(1.0f, 1.0f) // Sound ON
            } else {
                mediaPlayer?.setVolume(0f, 0f)     // Sound OFF (Muted)
            }
        }
    }

    private fun setupVideo() {
        // Only attempt to load the video if a valid ID was passed
        if (videoResId != -1) {
            val videoPath = "android.resource://${requireContext().packageName}/$videoResId"
            val uri = Uri.parse(videoPath)

            binding.ivMonsterCall.setVideoURI(uri)

            binding.ivMonsterCall.setOnPreparedListener { mp ->
                // Save the MediaPlayer instance to our class variable
                this.mediaPlayer = mp

                mp.isLooping = false

                // Apply initial volume state
                if (isSpeakerOn) {
                    mp.setVolume(1.0f, 1.0f)
                } else {
                    mp.setVolume(0f, 0f)
                }

                binding.ivMonsterCall.start()
            }

            binding.ivMonsterCall.setOnCompletionListener {
                endCallAndNavigate()
            }

            binding.ivMonsterCall.setOnErrorListener { _, _, _ ->
                endCallAndNavigate()
                true
            }
        } else {
            // Safety fallback: If there is no video, just navigate to the end screen immediately
            // You could also add a Toast here saying "Video not found" if you want to alert the user
            endCallAndNavigate()
        }
    }

    private fun endCallAndNavigate() {
        val bundle = Bundle().apply {
            putString("monsterName", name)
        }
        if (findNavController().currentDestination?.id == R.id.prankCallFragment) {
            findNavController().navigate(R.id.action_prankCallFragment_to_prankCallEndFragment,bundle)
        } else {
            findNavController().navigateUp()
        }
    }

    private fun startTimer() {
        handler.postDelayed(runnable, 1000)
    }

    private fun checkCameraPermissionAndStart() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                startCamera()
            }
            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            try {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.previewView.surfaceProvider)
                    }

                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview
                )
            } catch (exc: Exception) {
                Log.e("PrankCallFragment", "Use case binding failed", exc)
                Toast.makeText(requireContext(), "Failed to start camera.", Toast.LENGTH_SHORT).show()
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }



    override fun onResume() {
        super.onResume()
        if (!binding.ivMonsterCall.isPlaying) {
            binding.ivMonsterCall.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (binding.ivMonsterCall.isPlaying) {
            binding.ivMonsterCall.pause()
        }
    }

    override fun onDestroyView() {
        handler.removeCallbacks(runnable)
        binding.ivMonsterCall.stopPlayback()

        // Clear mediaPlayer reference to avoid memory leaks
        mediaPlayer = null

        super.onDestroyView()
    }
}