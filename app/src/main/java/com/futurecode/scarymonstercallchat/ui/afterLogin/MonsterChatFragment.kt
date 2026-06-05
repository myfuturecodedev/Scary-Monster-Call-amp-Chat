package com.futurecode.scarymonstercallchat.ui.afterLogin

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.adapter.ChatAdapter
import com.futurecode.scarymonstercallchat.base.BaseFragment
import com.futurecode.scarymonstercallchat.databinding.FragmentMonsterChatBinding
import com.futurecode.scarymonstercallchat.model.ChatMessage
import java.util.Locale
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.futurecode.scarymonstercallchat.adapter.QuestionPickerAdapter
import com.futurecode.scarymonstercallchat.ads.AdInterface
import com.futurecode.scarymonstercallchat.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.scarymonstercallchat.ads.native_ad.NativeAdsHelper
import com.futurecode.scarymonstercallchat.model.MonsterChatData
import com.futurecode.scarymonstercallchat.model.QnA
import com.futurecode.scarymonstercallchat.utils.Utils.setAdClickListener

class MonsterChatFragment : BaseFragment<FragmentMonsterChatBinding>(FragmentMonsterChatBinding::inflate) {

    private var monsterName: String? = null
    private var imageRes: Int = R.drawable.app_logo

    private var chatAdCounter = 0

    private val handler = Handler(Looper.getMainLooper())

    private val activeChatMessages = mutableListOf<ChatMessage>()
    private val availableQuestions = mutableListOf<QnA>()

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var questionsAdapter: QuestionPickerAdapter
    private lateinit var nativeAdsHelper: NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nativeAdsHelper = NativeAdsHelper(requireActivity())
        fullScreenAdsHelper = FullScreenAdsHelper(requireActivity())

        monsterName = arguments?.getString("monsterName") ?: "Unknown"
        imageRes = arguments?.getInt("imageRes") ?: R.drawable.app_logo

        setupUI()
        loadMonsterData()
        setupRecyclerViews()
    }

    private fun setupUI() {
        binding.customToolbar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.customToolbar.btnSettings.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            findNavController().navigate(R.id.settingsFragment)
        }

        binding.customToolbar.txtToolbarTitle.text = monsterName?.uppercase(Locale.getDefault())
    }

    private fun loadMonsterData() {
        availableQuestions.clear()
        activeChatMessages.clear()

        val specificQuestions = MonsterChatData.getQuestionsFor(monsterName)
        availableQuestions.addAll(specificQuestions)

        activeChatMessages.add(ChatMessage("Do you want to play with me... forever?", isFromMonster = true, isTyping = false))
    }

    private fun setupRecyclerViews() {
        chatAdapter = ChatAdapter(activeChatMessages)
        binding.rvChatHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

        questionsAdapter = QuestionPickerAdapter(availableQuestions) { clickedQnA ->
            processChatSelection(clickedQnA)
        }
        binding.rvAvailableQuestions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = questionsAdapter
        }
    }

    private fun processChatSelection(qna: QnA) {
        binding.rvAvailableQuestions.isEnabled = false

        chatAdCounter++
        Log.d("CHAT_AD", "Current Chat Count: $chatAdCounter")

        if (chatAdCounter % 5 == 0) {
            Log.d("CHAT_AD", "Triggering FullScreenAdsHelper dynamic logic...")

            // FIXED: Using your exact method signature and functional AdInterface setup
            fullScreenAdsHelper.showInterstitialAds(true, object : AdInterface {
                override fun finished() {
                    Log.d("CHAT_AD", "Ad flow finished. Appending next question message.")
                    handleQuestionClicked(qna)
                }
            })
        } else {
            handleQuestionClicked(qna)
        }
    }

    private fun handleQuestionClicked(qna: QnA) {
        val position = availableQuestions.indexOf(qna)
        if (position != -1) {
            availableQuestions.removeAt(position)
            questionsAdapter.notifyItemRemoved(position)
        }

        activeChatMessages.add(ChatMessage(qna.question, isFromMonster = false, isTyping = false))
        chatAdapter.notifyItemInserted(activeChatMessages.size - 1)
        binding.rvChatHistory.scrollToPosition(activeChatMessages.size - 1)

        val typingMessage = ChatMessage(text = "", isFromMonster = true, isTyping = true)
        activeChatMessages.add(typingMessage)
        chatAdapter.notifyItemInserted(activeChatMessages.size - 1)
        binding.rvChatHistory.scrollToPosition(activeChatMessages.size - 1)

        handler.postDelayed({
            val currentTypingIndex = activeChatMessages.indexOf(typingMessage)

            if (currentTypingIndex != -1) {
                activeChatMessages.removeAt(currentTypingIndex)
                chatAdapter.notifyItemRemoved(currentTypingIndex)

                activeChatMessages.add(ChatMessage(qna.answer, isFromMonster = true, isTyping = false))
                chatAdapter.notifyItemInserted(activeChatMessages.size - 1)
                binding.rvChatHistory.scrollToPosition(activeChatMessages.size - 1)
            }

            binding.rvAvailableQuestions.isEnabled = true

        }, 1500)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }
}