package com.futurecode.scarymonstercallchat.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.scarymonstercallchat.ads.native_ad.NativeAdsHelper
import com.futurecode.scarymonstercallchat.databinding.ItemMonsterBinding
import com.futurecode.scarymonstercallchat.databinding.ItemNativeAdsAdapterBinding
import com.futurecode.scarymonstercallchat.model.Monster
import com.futurecode.scarymonstercallchat.utils.AdViewTypeManager
import com.futurecode.scarymonstercallchat.utils.Utils.setAdClickListener

class MonsterAdapter(
    // 1. Changed to List<Any> to support both Monster and "AD_UNIT"
    private val activity: Activity,
    private val items: List<Any>,
    private val prankType: String = "call",
    private val fullScreenAdsHelper: FullScreenAdsHelper, // Added Helper directly here
    private val onItemClick: (Monster) -> Unit

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() { // 2. Changed to generic ViewHolder

    // 3. Define view types based on the item in the list
    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return if (item is String && item == "AD_UNIT") {
            AdViewTypeManager.TYPE_AD
        } else {
            AdViewTypeManager.TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == AdViewTypeManager.TYPE_AD) {
            // Inflate Ad Layout
            val adBinding = ItemNativeAdsAdapterBinding.inflate(inflater, parent, false)
            AdViewHolder(adBinding)

        } else {
            // Inflate Monster Layout
            val binding = ItemMonsterBinding.inflate(inflater, parent, false)
            MonsterViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        // 4. Bind the correct data to the correct ViewHolder
        if (holder is MonsterViewHolder && item is Monster) {
            holder.bind(item)
        } else if (holder is AdViewHolder) {
            holder.bindAd()
        }
    }

    override fun getItemCount(): Int = items.size

    // --- VIEWHOLDER FOR MONSTERS ---
    inner class MonsterViewHolder(private val binding: ItemMonsterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(monster: Monster) {
            binding.tvMonsterName.text = monster.name
            binding.ivMonster.setImageResource(monster.imageResId)

            val context = binding.root.context

            if (prankType == "call") {
                // Show text
                binding.tvReadyStatus.visibility = View.VISIBLE

                // CRITICAL: Reset padding back to 0 when the text is visible
                binding.tvMonsterName.setPadding(
                    binding.tvMonsterName.paddingLeft,
                    binding.tvMonsterName.paddingTop,
                    binding.tvMonsterName.paddingRight,
                    0 // 0 bottom padding
                )
            } else {
                // Hide text
                binding.tvReadyStatus.visibility = View.GONE
                val padding10sdp = (10 * context.resources.displayMetrics.density).toInt()
                // Apply the padding
                binding.tvMonsterName.setPadding(
                    binding.tvMonsterName.paddingLeft,
                    binding.tvMonsterName.paddingTop,
                    binding.tvMonsterName.paddingRight,
                    padding10sdp // 10sdp bottom padding
                )
            }

            // FIX: Bind the ad click layout tracking right here on the view inside the ViewHolder
            binding.root.setAdClickListener(activity, fullScreenAdsHelper) {
                onItemClick(monster)
            }
        }
    }

    // --- VIEWHOLDER FOR ADS ---
    inner class AdViewHolder(val binding: ItemNativeAdsAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindAd() {
            NativeAdsHelper(activity).showNativeAd(
                binding.frameLayout,
                binding.relativeLayout,
                binding.placeholder
            )
        }
    }
}

//class MonsterAdapter(
//    private val activity: Activity,
//    private val items: List<Monster>, // Changed back to List<Monster> because adapter handles Ad logic now!
//    private val prankType: String = "call",
//    private val onItemClick: (Monster) -> Unit
//) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    // Determines if a position should show an Ad (Every 5th item: index 4, 9, 14, etc.)
//    override fun getItemViewType(position: Int): Int {
//        return if ((position + 1) % 5 == 0) {
//            AdViewTypeManager.TYPE_AD
//        } else {
//            AdViewTypeManager.TYPE_ITEM
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        return if (viewType == AdViewTypeManager.TYPE_AD) {
//            val adBinding = ItemNativeAdsAdapterBinding.inflate(inflater, parent, false)
//            AdViewHolder(adBinding)
//        } else {
//            val binding = ItemMonsterBinding.inflate(inflater, parent, false)
//            MonsterViewHolder(binding)
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (holder is AdViewHolder) {
//            holder.bindAd()
//        } else if (holder is MonsterViewHolder) {
//            // Map the adapter position to the correct index in your monster list
//            val monsterIndex = position - (position + 1) / 5
//            if (monsterIndex < items.size) {
//                holder.bind(items[monsterIndex])
//            }
//        }
//    }
//
//    // Expand item count to include space for the ads
//    override fun getItemCount(): Int {
//        if (items.isEmpty()) return 0
//        // Formula adds 1 ad slot for every 4 items
//        return items.size + (items.size / 4)
//    }
//
//    // --- VIEWHOLDER FOR MONSTERS ---
//    inner class MonsterViewHolder(private val binding: ItemMonsterBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(monster: Monster) {
//            binding.tvMonsterName.text = monster.name
//            binding.ivMonster.setImageResource(monster.imageResId)
//
//            val context = binding.root.context
//
//            if (prankType == "call") {
//                binding.tvReadyStatus.visibility = View.VISIBLE
//                binding.tvMonsterName.setPadding(
//                    binding.tvMonsterName.paddingLeft,
//                    binding.tvMonsterName.paddingTop,
//                    binding.tvMonsterName.paddingRight,
//                    0
//                )
//            } else {
//                binding.tvReadyStatus.visibility = View.GONE
//                val padding10sdp = (10 * context.resources.displayMetrics.density).toInt()
//                binding.tvMonsterName.setPadding(
//                    binding.tvMonsterName.paddingLeft,
//                    binding.tvMonsterName.paddingTop,
//                    binding.tvMonsterName.paddingRight,
//                    padding10sdp
//                )
//            }
//
//            binding.root.setOnClickListener {
//                onItemClick(monster)
//            }
//        }
//    }
//
//    // --- VIEWHOLDER FOR ADS ---
//    inner class AdViewHolder(val binding: ItemNativeAdsAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bindAd() {
//            NativeAdsHelper(activity).showNativeAd(
//                binding.frameLayout,
//                binding.relativeLayout,
//                binding.placeholder
//            )
//        }
//    }
//}