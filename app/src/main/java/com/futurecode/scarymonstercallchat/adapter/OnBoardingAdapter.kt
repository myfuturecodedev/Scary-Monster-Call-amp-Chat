package com.futurecode.scarymonstercallchat.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.ads.adpager.AdPagerAdType
import com.futurecode.scarymonstercallchat.ads.adpager.AdPagerItem
import com.futurecode.scarymonstercallchat.ads.adpager.AdPagerTimerController
import com.futurecode.scarymonstercallchat.ads.ads_new.NativeAdPagerController
import com.futurecode.scarymonstercallchat.databinding.ItemOnboardingBinding
import com.futurecode.scarymonstercallchat.databinding.ItemOnboardingNativeAdBinding
import com.futurecode.scarymonstercallchat.model.OnBoardingModel

class OnBoardingAdapter(
    private val activity: Activity,
    val list: List<AdPagerItem<OnBoardingModel>>, // Changed model mapping type generic safely
    private val nativeAdPagerController: NativeAdPagerController,
    private val timerController: AdPagerTimerController,
    private val onAdAdvanceRequested: (position: Int) -> Unit,
    private val onContentContinueRequested: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val boundNativeHolders = mutableMapOf<String, NativeAdViewHolder>()
    private var selectedPosition = RecyclerView.NO_POSITION

    class ViewHolder(val binding: ItemOnboardingBinding) : RecyclerView.ViewHolder(binding.root)

    class NativeAdViewHolder(val binding: ItemOnboardingNativeAdBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var boundPageKey: String? = null
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = list[position]) {
            is AdPagerItem.Content -> TYPE_ONBOARDING
            is AdPagerItem.NativeAd -> when (item.adType) {
                AdPagerAdType.NORMAL -> TYPE_NORMAL_AD
                AdPagerAdType.TIMER -> TYPE_TIMER_AD
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_NORMAL_AD,
            TYPE_TIMER_AD -> NativeAdViewHolder(
                ItemOnboardingNativeAdBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> ViewHolder(
                ItemOnboardingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is AdPagerItem.Content -> bindOnboarding(holder as ViewHolder, item.data, position)
            is AdPagerItem.NativeAd -> bindNativeAd(holder as NativeAdViewHolder, item)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is NativeAdViewHolder) {
            holder.boundPageKey?.let(timerController::detach)
            holder.boundPageKey?.let(boundNativeHolders::remove)
            holder.boundPageKey = null
        }
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int = list.size

    fun isLastContentPage(position: Int): Boolean {
        return position == list.indexOfLast { it is AdPagerItem.Content }
    }

    fun nextPositionAfter(position: Int): Int? {
        val nextPosition = position + 1
        return nextPosition.takeIf { it in list.indices }
    }

    fun release() {
        timerController.clear()
        boundNativeHolders.clear()
    }

    fun onPageSelected(position: Int) {
        selectedPosition = position
        timerController.clear()
        resetBoundTimerPages()
        startTimerIfNeeded(position)
    }

    // ✅ FIXED DESIGN: Kept only background resource loading from the first simple adapter pattern!
    private fun bindOnboarding(
        holder: ViewHolder,
        item: OnBoardingModel,
        position: Int
    ) {
        holder.binding.apply {
            // Extra text views, subtitles aur redundant image layouts safely clear kiye
            // Pure Image is loaded onto target container as full scale screen layout
            ivOnboardingBg.setImageResource(item.image)

            // Layout fields hidden/removed dynamically to avoid graphic text overlap


            // Indicator and forward handling remains intact
           // bindIndicators(indicatorContainer, position)
        }
    }

    private fun bindIndicators(
        container: LinearLayout,
        activePosition: Int
    ) {
        container.removeAllViews()

        // Dynamic matching filters indicator counts for contents only
        val contentIndices = list.indices.filter { list[it] is AdPagerItem.Content }

        contentIndices.forEachIndexed { visualIndex, actualIndex ->
            val isSelected = actualIndex == activePosition
            val dotView = View(container.context).apply {
                setBackgroundResource(if (isSelected) R.drawable.bg_indicator_selected else R.drawable.bg_indicator_unselected)
                layoutParams = LinearLayout.LayoutParams(
                    if (isSelected) 24.dpToPx(container) else 8.dpToPx(container),
                    8.dpToPx(container)
                ).apply {
                    marginStart = 4.dpToPx(container)
                    marginEnd = 4.dpToPx(container)
                }
            }
            container.addView(dotView)
        }
    }

    private fun bindNativeAd(holder: NativeAdViewHolder, item: AdPagerItem.NativeAd) {
        holder.boundPageKey = item.pageKey
        boundNativeHolders[item.pageKey] = holder
        nativeAdPagerController.bind(item.pageKey, holder.binding)

        when (item.adType) {
            AdPagerAdType.NORMAL -> bindNormalAdControls(holder)
            AdPagerAdType.TIMER -> bindTimerAdControls(holder, item)
        }
    }

    private fun bindNormalAdControls(holder: NativeAdViewHolder) {
        holder.binding.tvAdStatus.isVisible = true
        holder.binding.tvAdStatus.text = "ADVERTISEMENT"
        holder.binding.btnAdNext.isVisible = true
        holder.binding.btnAdNext.isEnabled = true
        holder.binding.btnAdNext.alpha = ENABLED_ALPHA
        holder.binding.btnAdNext.text = "X"
        holder.binding.btnAdNext.setOnClickListener {
            val position = holder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) onAdAdvanceRequested(position)
        }
    }

    private fun bindTimerAdControls(
        holder: NativeAdViewHolder,
        item: AdPagerItem.NativeAd
    ) {
        holder.binding.tvAdStatus.isVisible = true
        holder.binding.tvAdStatus.text = "ADVERTISEMENT"
        setTimerLockedState(holder, item.unlockDurationMs)
        holder.binding.btnAdNext.setOnClickListener {
            val position = holder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) onAdAdvanceRequested(position)
        }

        if (holder.bindingAdapterPosition == selectedPosition) {
            startTimer(holder, item)
        }
    }

    private fun startTimerIfNeeded(position: Int) {
        val item = list.getOrNull(position) as? AdPagerItem.NativeAd ?: return
        if (item.adType != AdPagerAdType.TIMER) return

        val holder = boundNativeHolders[item.pageKey]
        if (holder == null) {
            notifyItemChanged(position)
            return
        }
        startTimer(holder, item)
    }

    private fun startTimer(
        holder: NativeAdViewHolder,
        item: AdPagerItem.NativeAd
    ) {
        setTimerLockedState(holder, item.unlockDurationMs)
        timerController.bind(
            pageKey = item.pageKey,
            durationMs = item.unlockDurationMs,
            onTick = { seconds ->
                holder.binding.tvAdStatus.text = "ADVERTISEMENT"
                holder.binding.btnAdNext.isVisible = true
                holder.binding.btnAdNext.isEnabled = false
                holder.binding.btnAdNext.alpha = ENABLED_ALPHA
                holder.binding.btnAdNext.text = seconds.toString()
            },
            onUnlocked = {
                holder.binding.tvAdStatus.text = "ADVERTISEMENT"
                holder.binding.btnAdNext.isVisible = true
                holder.binding.btnAdNext.text = "X"
                holder.binding.btnAdNext.isEnabled = true
                holder.binding.btnAdNext.alpha = ENABLED_ALPHA
            }
        )
    }

    private fun resetBoundTimerPages() {
        boundNativeHolders.values.forEach { holder ->
            val position = holder.bindingAdapterPosition
            val item = list.getOrNull(position) as? AdPagerItem.NativeAd ?: return@forEach
            if (item.adType == AdPagerAdType.TIMER) {
                setTimerLockedState(holder, item.unlockDurationMs)
            }
        }
    }

    private fun setTimerLockedState(
        holder: NativeAdViewHolder,
        durationMs: Long
    ) {
        holder.binding.tvAdStatus.text = "ADVERTISEMENT"
        holder.binding.btnAdNext.isVisible = true
        holder.binding.btnAdNext.isEnabled = false
        holder.binding.btnAdNext.alpha = DISABLED_ALPHA
        holder.binding.btnAdNext.text = ((durationMs + 999L) / 1_000L).toString()
    }

    companion object {
        private const val TYPE_ONBOARDING = 0
        private const val TYPE_NORMAL_AD = 1
        private const val TYPE_TIMER_AD = 2
        private const val DISABLED_ALPHA = 0.55f
        private const val ENABLED_ALPHA = 1f
    }
}

private fun Int.dpToPx(view: View): Int {
    return (this * view.resources.displayMetrics.density).toInt()
}