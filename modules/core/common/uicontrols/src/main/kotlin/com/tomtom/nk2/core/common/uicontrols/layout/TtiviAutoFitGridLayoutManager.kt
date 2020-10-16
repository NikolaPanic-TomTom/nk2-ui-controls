/*
 * Copyright (c) 2020 - 2020 TomTom N.V. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom N.V. and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * licensee agreement between you and TomTom. If you are the licensee, you are only permitted
 * to use this Software in accordance with the terms of your license agreement. If you are
 * not the licensee then you are not authorised to use this software in any manner and should
 * immediately return it to TomTom N.V.
 */

package com.tomtom.nk2.core.common.uicontrols.layout

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomtom.nk2.core.common.uicontrols.list.TtiviGroupItemsAdapter

/**
 * A [GridLayoutManager] implementation that calculates span count depends on items size.
 * @param itemSpacingPx Spacing between items.
 */
open class TtiviAutoFitGridLayoutManager(
    context: Context,
    private val itemSpacingPx: Int
) : GridLayoutManager(context, 1, VERTICAL, false) {

    override fun onAdapterChanged(
        oldAdapter: RecyclerView.Adapter<*>?,
        newAdapter: RecyclerView.Adapter<*>?
    ) {
        super.onAdapterChanged(oldAdapter, newAdapter)
        setSpanSizeLookup(newAdapter)
    }

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        setSpanSizeLookup(view.adapter)
    }

    override fun onDetachedFromWindow(view: RecyclerView?, recycler: RecyclerView.Recycler?) {
        super.onDetachedFromWindow(view, recycler)
        setSpanSizeLookup()
    }

    private fun setSpanSizeLookup(adapter: RecyclerView.Adapter<*>? = null) {
        spanSizeLookup = when (adapter) {
            is TtiviGroupItemsAdapter<*> -> TtiviGroupSpanSizeLookup(adapter)
            else -> DefaultSpanSizeLookup()
        }
    }

    /**
     * Calculating the optimal number of columns works as follows:
     *   w = width of container view in pixels (containerWidthPx)
     *   s = width of source tile in pixels (minItemWidthPx)
     *   g = margin between two source tiles in pixels (itemSpacingPx)
     *   c = number of tiles fitting in the container width
     *   pl = left padding
     *   pr = right padding
     * The full width of the view (w) is utilized when the optimum number (c) of tile widths (s)
     * fit, with the desired spacing (g) between them and taking horizontal padding into account:
     *   w = c * s + (c - 1) * g + pl + pr =>
     *   w = c * s + c * g - g + pl + pr =>
     *   w = c * (s + g) - g pl + pr =>
     *   c * (s + g) = w + g - pl - pr =>
     *   c = (w + g - pl - pr) / (s + g)
     */
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        val childrenCount = state.itemCount
        if (childrenCount > 0) {
            val item = recycler.getViewForPosition(childrenCount - 1)
            val itemMinimumWidth = when (item.minimumWidth) {
                0 -> item.width
                else -> item.minimumWidth
            }

            if (itemMinimumWidth != 0) {
                val viewUsableWidth =
                    (width + itemSpacingPx - paddingLeft - paddingRight)
                val itemWidth = (itemMinimumWidth + itemSpacingPx)
                spanCount = (viewUsableWidth / itemWidth).coerceAtLeast(1)
            }
        }

        super.onLayoutChildren(recycler, state)
    }

    inner class TtiviGroupSpanSizeLookup(private val adapter: TtiviGroupItemsAdapter<*>) :
        GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int =
            if (adapter.isGroupHeader(position)) spanCount else 1
    }
}
