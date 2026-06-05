package com.futurecode.scarymonstercallchat.ui.afterLogin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.futurecode.scarymonstercallchat.BuildConfig
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.adapter.MonsterAdapter
import com.futurecode.scarymonstercallchat.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.scarymonstercallchat.ads.native_ad.NativeAdsHelper
import com.futurecode.scarymonstercallchat.base.BaseFragment
import com.futurecode.scarymonstercallchat.databinding.FragmentPrankHomeCallBinding
import com.futurecode.scarymonstercallchat.model.Monster
import com.futurecode.scarymonstercallchat.utils.RandomCallManager
import com.futurecode.scarymonstercallchat.utils.Utils.setAdClickListener

class PrankHomeCallFragment : BaseFragment<FragmentPrankHomeCallBinding>(FragmentPrankHomeCallBinding::inflate) {

    private var prankType: String? = "call"
    private var randomCallManager: RandomCallManager? = null
    private lateinit var nativeAdsHelper: NativeAdsHelper
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prankType = arguments?.getString("prankType") ?: "call"

        nativeAdsHelper = NativeAdsHelper(requireActivity())
        fullScreenAdsHelper = FullScreenAdsHelper(requireActivity())


        val name = BuildConfig.APPLICATION_ID
        Log.d("TAG", "onViewCreated: $name")


        setupUI()
        setupRecyclerView()

        // Initialize and define what happens when "Accept" is clicked
        randomCallManager = RandomCallManager(binding.layoutIncomingCall.root) { randomMonster ->
            val bundle = Bundle().apply {
                putString("monsterName", randomMonster.name)
                putInt("imageRes", randomMonster.imageResId)
                randomMonster.videoCall?.let { safeVideo ->
                    putInt("video", safeVideo)
                }
            }
            findNavController().navigate(R.id.action_prankHomeCallFragment_to_monsterStartCallFragment, bundle)
        }

        randomCallManager?.startTimer()
    }

    private fun setupUI() {
        binding.customToolbar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.customToolbar.btnSettings.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            findNavController().navigate(R.id.settingsFragment)
        }

        when (prankType) {
            "call" -> {
                binding.customToolbar.txtToolbarTitle.text = getString(R.string.prank_call)
                binding.tvSubtitle.text = getString(R.string.choose_your_nightmare)
            }
            "chat" -> {
                binding.customToolbar.txtToolbarTitle.text = getString(R.string.prank_chat)
                binding.tvSubtitle.text = getString(R.string.choose_your_nightmare)
            }
            "sound" -> {
                binding.customToolbar.txtToolbarTitle.text = getString(R.string.prank_sound)
                binding.tvSubtitle.text = getString(R.string.who_do_you_want_to_hear)
            }
        }
    }

    private fun setupRecyclerView() {
        val list = when (prankType) {
            "call" -> getCallList()
            "chat" -> getChatList()
            "sound" -> getSoundList()
            else -> getCallList()
        }

        val monstersWithAds = mutableListOf<Any>()

        list.forEachIndexed { index, monster ->
            monstersWithAds.add(monster)

            // index starts at 0, so item 4 is index 3, item 8 is index 7, etc.
            if ((index + 1) % 4 == 0) {
                monstersWithAds.add("AD_UNIT")
            }
        }

// Change your adapter initialization inside setupRecyclerView() to match this:
        val adapter = MonsterAdapter(
            requireActivity(),
            monstersWithAds,
            prankType ?: "call",
            fullScreenAdsHelper // Pass your fragment's initialization instance here
        ) { monster ->
            // Clean and simple navigation logic. The ad handles interception internally now!
            val bundle = Bundle().apply {
                putString("monsterName", monster.name)
                putInt("imageRes", monster.imageResId)

                when (prankType) {
                    "call" -> {
                        Log.d("TAG", "Adapter sending video: ${monster.videoCall}")
                        monster.videoCall?.let { putInt("video", it) }
                    }
                    "sound" -> {
                        monster.soundResId?.let { putInt("sound", it) }
                    }
                }
            }

            when (prankType) {
                "call" -> findNavController().navigate(R.id.action_prankHomeCallFragment_to_monsterStartCallFragment, bundle)
                "chat" -> findNavController().navigate(R.id.action_prankHomeCallFragment_to_monsterChatFragment, bundle)
                "sound" -> findNavController().navigate(R.id.action_prankHomeCallFragment_to_monsterSoundFragment, bundle)
            }
        }

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = monstersWithAds[position]
                return if (item is String && item == "AD_UNIT") 2 else 1
            }
        }

        binding.rvMonsters.apply {
            layoutManager = gridLayoutManager
            this.adapter = adapter
        }
    }

    private fun getCallList(): List<Monster> {
        return listOf(
            Monster(name = getString(R.string.monster_freddy), imageResId = R.drawable.call1, videoCall = R.raw.call1),
            Monster(name = getString(R.string.monster_jason), imageResId = R.drawable.call2, videoCall = R.raw.call2),
            Monster(name = getString(R.string.monster_jeff_the), imageResId = R.drawable.call3, videoCall = R.raw.call3),
            Monster(name = getString(R.string.monster_joker), imageResId = R.drawable.call4, videoCall = R.raw.call4),
            Monster(name = getString(R.string.monster_pennywise), imageResId = R.drawable.call5, videoCall = R.raw.call5),
            Monster(name = getString(R.string.monster_scary_claus), imageResId = R.drawable.call6, videoCall = R.raw.call6),
            Monster(name = getString(R.string.monster_serbian), imageResId = R.drawable.call7, videoCall = R.raw.call7),
            Monster(name = getString(R.string.monster_the_closet), imageResId = R.drawable.call8, videoCall = R.raw.call8),
            Monster(name = getString(R.string.monster_the_upside), imageResId = R.drawable.call9, videoCall = R.raw.call9),
            Monster(name = getString(R.string.monster_the_nun), imageResId = R.drawable.call10, videoCall = R.raw.call10)
        )
    }

    private fun getChatList(): List<Monster> {
        return listOf(
            Monster(getString(R.string.monster_annabelle), R.drawable.chat1),
            Monster(getString(R.string.monster_chucky), R.drawable.chat2),
            Monster(getString(R.string.monster_bloody_mary), R.drawable.chat3),
            Monster(getString(R.string.monster_jeff_killer), R.drawable.chat4),
            Monster(getString(R.string.monster_eyeless_jack), R.drawable.chat5),
            Monster(getString(R.string.monster_la_llorona), R.drawable.chat6),
            Monster(getString(R.string.monster_the_nun), R.drawable.chat7),
            Monster(getString(R.string.monster_sadako), R.drawable.chat8),
            Monster(getString(R.string.monster_slender_man), R.drawable.chat9),
            Monster(getString(R.string.monster_pennywise), R.drawable.chat10)
        )
    }

    private fun getSoundList(): List<Monster> {
        return listOf(
            Monster(name = getString(R.string.monster_banshee), imageResId = R.drawable.sound1, soundResId = R.raw.sound1),
            Monster(name = getString(R.string.monster_screaming), imageResId = R.drawable.sound2, soundResId = R.raw.sound2),
            Monster(name = getString(R.string.monster_sea_monster), imageResId = R.drawable.sound3, soundResId = R.raw.sound3),
            Monster(name = getString(R.string.monster_werewolf), imageResId = R.drawable.sound4, soundResId = R.raw.sound4),
            Monster(name = getString(R.string.monster_creepy), imageResId = R.drawable.sound6, soundResId = R.raw.sound5),
            Monster(name = getString(R.string.monster_crow_sound), imageResId = R.drawable.sound5, soundResId = R.raw.crow),
            Monster(name = getString(R.string.monster_door_banging), imageResId = R.drawable.sound7, soundResId = R.raw.sound7),
            Monster(name = getString(R.string.monster_evil_laugh), imageResId = R.drawable.sound8, soundResId = R.raw.sound8),
            Monster(name = getString(R.string.monster_female_ghost), imageResId = R.drawable.sound9, soundResId = R.raw.sound9),
            Monster(name = getString(R.string.monster_possessed), imageResId = R.drawable.sound10, soundResId = R.raw.sound10),
            Monster(name = getString(R.string.monster_ghostly), imageResId = R.drawable.sound11, soundResId = R.raw.sound11),
            Monster(name = getString(R.string.monster_ghost_breath), imageResId = R.drawable.sound12, soundResId = R.raw.sound12)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        randomCallManager?.stopTimer()
        randomCallManager = null
    }
}