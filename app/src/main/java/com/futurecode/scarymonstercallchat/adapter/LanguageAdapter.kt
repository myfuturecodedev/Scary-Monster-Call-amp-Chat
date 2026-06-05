package com.futurecode.scarymonstercallchat.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.futurecode.scarymonstercallchat.ads.native_ad.NativeAdsHelper
import com.futurecode.scarymonstercallchat.databinding.ItemLanguageBinding
import com.futurecode.scarymonstercallchat.databinding.ItemNativeAdsAdapterBinding
import com.futurecode.scarymonstercallchat.model.LanguageModel
import com.futurecode.scarymonstercallchat.utils.AdViewTypeManager
//class LanguageAdapter(
//    private val activity: Activity,
//    private var list: List<Any>,
//    private val onItemClick: (LanguageModel) -> Unit
//) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    private var selectedPosition = -1
//
//    init {
//        // Automatically find the selected language when the screen opens
//        selectedPosition = list.indexOfFirst {
//            it is LanguageModel && it.isSelected
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        val item = list[position]
//        return if (item is String && item == "AD_UNIT") {
//            AdViewTypeManager.TYPE_AD
//        } else {
//            AdViewTypeManager.TYPE_ITEM
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        return if (viewType == AdViewTypeManager.TYPE_AD) {
//            // Make sure you have layout_ad_banner created in your resources
//            // FIX: Inflate the binding class directly, don't inflate a plain View
//            val adBinding = ItemNativeAdsAdapterBinding.inflate(inflater, parent, false)
//            AdViewHolder(adBinding)
//
//        } else {
//            val binding = ItemLanguageBinding.inflate(inflater, parent, false)
//            LanguageViewHolder(binding)
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val item = list[position]
//        if (holder is LanguageViewHolder && item is LanguageModel) {
//            holder.bind(item, position)
//        } else if (holder is AdViewHolder) {
//            holder.bindAd()
//        }
//    }
//
//    override fun getItemCount(): Int = list.size
//
//    inner class LanguageViewHolder(private val binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: LanguageModel, position: Int) {
//            binding.apply {
//                tvLanguageName.text = item.name
//                ivFlag.setImageResource(item.flag)
//
//                // Update UI based on selection state
//                val isSelected = position == selectedPosition
//                rbSelect.isChecked = isSelected
//
//                // Create the click action
//                val clickAction = View.OnClickListener {
//                    if (selectedPosition != position) {
//                        val prev = selectedPosition
//                        selectedPosition = position
//
//                        if (prev != -1) {
//                            notifyItemChanged(prev) // Uncheck previous
//                        }
//                        notifyItemChanged(selectedPosition) // Check new
//
//                        // Send the selected item back to the Fragment
//                        onItemClick(item)
//                    }
//                }
//
//                // Attach the click action to both the row and the radio button
//                root.setOnClickListener(clickAction)
//                rbSelect.setOnClickListener(clickAction)
//            }
//        }
//    }
//
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

class LanguageAdapter(
    private val activity: Activity,
    private var list: List<Any>,
    private val onItemClick: (LanguageModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedPosition = -1

    init {
        // Automatically find the selected language when the screen opens
        selectedPosition = list.indexOfFirst {
            it is LanguageModel && it.isSelected
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return if (item is String && item == "AD_UNIT") {
            AdViewTypeManager.TYPE_AD
        } else {
            AdViewTypeManager.TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == AdViewTypeManager.TYPE_AD) {
            val adBinding = ItemNativeAdsAdapterBinding.inflate(inflater, parent, false)
            AdViewHolder(adBinding)
        } else {
            val binding = ItemLanguageBinding.inflate(inflater, parent, false)
            LanguageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (holder is LanguageViewHolder && item is LanguageModel) {
            holder.bind(item, position)
        } else if (holder is AdViewHolder) {
            holder.bindAd()
        }
    }

    override fun getItemCount(): Int = list.size

    inner class LanguageViewHolder(private val binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LanguageModel, position: Int) {
            binding.apply {
                tvLanguageName.text = item.name
                ivFlag.setImageResource(item.flag)

                // Update UI based on selection state
                val isSelected = position == selectedPosition
                rbSelect.isChecked = isSelected

                // Fixed click action logic
                val clickAction = View.OnClickListener {
                    if (selectedPosition != position) {
                        val prev = selectedPosition
                        selectedPosition = position

                        if (prev != -1) {
                            notifyItemChanged(prev) // Uncheck old item
                        }
                        notifyItemChanged(selectedPosition) // Check new item
                    }

                    // FIX: This must sit OUTSIDE the if-condition block
                    // so clicking English immediately triggers your next action.
                    onItemClick(item)
                }

                // Attach the click action to both the row and the radio button
                root.setOnClickListener(clickAction)
                rbSelect.setOnClickListener(clickAction)
            }
        }
    }

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