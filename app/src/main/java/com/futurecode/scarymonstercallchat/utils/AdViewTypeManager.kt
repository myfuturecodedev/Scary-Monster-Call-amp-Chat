package com.futurecode.scarymonstercallchat.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

object AdViewTypeManager {
    const val TYPE_ITEM = 1
    const val TYPE_AD = 2

    // Makes Ads full-width in Grids
    fun setGridSpanSize(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, spanCount: Int) {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter.getItemViewType(position) == TYPE_AD) {
                        spanCount // Take up all columns
                    } else {
                        1 // Take up only one column
                    }
                }
            }
        }
    }
}