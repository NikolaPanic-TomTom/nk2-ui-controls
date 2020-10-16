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
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.recyclerview.widget.RecyclerView.Recycler

/**
 * [TtiviAutoFitGridLayoutManager] implementation that supports sticky headers.
 * @param itemSpacing Spacing between items.
 * @param isStickyHeader predicate that returns true if the item in specified position is a header.
 */
class TtiviStickyHeaderGridLayoutManager(
    context: Context,
    itemSpacing: Int,
    private val isStickyHeader: (Int) -> Boolean
) : TtiviAutoFitGridLayoutManager(context, itemSpacing) {

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        registerAdapter(view.adapter)
    }

    override fun onAdapterChanged(
        oldAdapter: RecyclerView.Adapter<*>?,
        newAdapter: RecyclerView.Adapter<*>?
    ) {
        super.onAdapterChanged(oldAdapter, newAdapter)
        registerAdapter(newAdapter)
    }

    override fun scrollVerticallyBy(dy: Int, recycler: Recycler, state: RecyclerView.State): Int {
        val scrolled = runWithStickyHeaderDetached { super.scrollVerticallyBy(dy, recycler, state) }
        if (scrolled != 0) {
            updateStickyHeader(recycler, false)
        }
        return scrolled
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: Recycler, state: RecyclerView.State): Int {
        val scrolled =
            runWithStickyHeaderDetached { super.scrollHorizontallyBy(dx, recycler, state) }
        if (scrolled != 0) {
            updateStickyHeader(recycler, false)
        }
        return scrolled
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        runWithStickyHeaderDetached { super.onLayoutChildren(recycler, state) }
        if (!state.isPreLayout) {
            updateStickyHeader(recycler, true)
        }
    }

    override fun scrollToPosition(position: Int) {
        scrollToPositionWithOffset(position, INVALID_OFFSET)
    }

    override fun computeVerticalScrollExtent(state: RecyclerView.State) =
        runWithStickyHeaderDetached { super.computeVerticalScrollExtent(state) }

    override fun computeVerticalScrollOffset(state: RecyclerView.State) =
        runWithStickyHeaderDetached { super.computeVerticalScrollOffset(state) }

    override fun computeVerticalScrollRange(state: RecyclerView.State) =
        runWithStickyHeaderDetached { super.computeVerticalScrollRange(state) }

    override fun computeHorizontalScrollExtent(state: RecyclerView.State) =
        runWithStickyHeaderDetached { super.computeHorizontalScrollExtent(state) }

    override fun computeHorizontalScrollOffset(state: RecyclerView.State) =
        runWithStickyHeaderDetached { super.computeHorizontalScrollExtent(state) }

    override fun computeHorizontalScrollRange(state: RecyclerView.State) =
        runWithStickyHeaderDetached { super.computeHorizontalScrollExtent(state) }

    override fun onFocusSearchFailed(
        focused: View,
        focusDirection: Int,
        recycler: Recycler,
        state: RecyclerView.State
    ) = runWithStickyHeaderDetached {
        super.onFocusSearchFailed(focused, focusDirection, recycler, state)
    }

    private fun registerAdapter(newAdapter: RecyclerView.Adapter<*>?) {
        adapter?.unregisterAdapterDataObserver(headerPositionsObserver)
        adapter = newAdapter
        adapter?.registerAdapterDataObserver(headerPositionsObserver)
        headerPositionsObserver.onChanged()
    }

    private fun updateStickyHeader(recycler: Recycler, layout: Boolean) {
        if (headerPositions.size > 0 && childCount > 0) {
            updateAnchor()
            anchorView?.let { anchorView ->

                val anchorPos = getPosition(anchorView)
                val headerPos = headerPositions.lastOrNull { it <= anchorPos } ?: return@let
                val nextHeaderPos = headerPositions.firstOrNull { it > headerPos }

                if ((headerPos != anchorPos || isCropped(anchorView)) &&
                    nextHeaderPos != headerPos + 1
                ) {
                    stickyHeader?.let {
                        if (getItemViewType(it) != adapter?.getItemViewType(headerPos)) {
                            scrapStickyHeader(recycler)
                        }
                    }

                    stickyHeader ?: createStickyHeader(recycler, headerPos)

                    stickyHeader?.let {
                        if (layout || getPosition(it) != headerPos) {
                            bindStickyHeader(recycler, headerPos)
                        }

                        val nextHeaderView: View? = nextHeaderPos?.let { nextHeaderPos ->
                            getChildAt(anchorIndex + (nextHeaderPos - anchorPos))
                                .takeIf { next -> next !== it }
                        }

                        translateHeader(it, nextHeaderView)
                    }

                    return
                }
            }
        }

        stickyHeader?.let { scrapStickyHeader(recycler) }
    }

    private fun createStickyHeader(recycler: Recycler, position: Int) {
        val view = recycler.getViewForPosition(position)

        addView(view)
        layoutView(view)

        // Take over management of the view
        ignoreView(view)

        stickyHeader = view
        stickyHeaderPosition = position
    }

    private fun bindStickyHeader(recycler: Recycler, position: Int) {
        stickyHeader?.let {
            recycler.bindViewToPosition(it, position)
            stickyHeaderPosition = position
            layoutView(it)
        }
    }

    private fun layoutView(view: View) {
        measureChildWithMargins(view, 0, 0)
        view.layout(paddingLeft, 0, width - paddingRight, view.measuredHeight)
    }

    private fun scrapStickyHeader(recycler: Recycler? = null) {
        stickyHeader?.let {
            stickyHeaderPosition = RecyclerView.NO_POSITION

            it.translationX = 0f
            it.translationY = 0f

            stopIgnoringView(it)
            removeView(it)
            recycler?.recycleView(it)

            stickyHeader = null
        }
    }

    private fun updateAnchor() {
        for (index in 0 until childCount) {
            getChildAt(index)?.let {
                val params = it.layoutParams as RecyclerView.LayoutParams
                if (!params.isItemRemoved &&
                    !params.isViewInvalid &&
                    (it.bottom >= it.translationY)
                ) {
                    anchorView = it
                    anchorIndex = index
                    return
                }
            }
        }
    }

    private fun isCropped(view: View) = view.top + view.translationY < 0

    private fun translateHeader(headerView: View, nextHeaderView: View?) {
        headerView.translationY = nextHeaderView?.let {
            (it.top - headerView.height.toFloat()).coerceAtMost(0f)
        } ?: 0f
    }

    private inline fun <T, R> T.runWithStickyHeaderDetached(block: (T) -> R): R {
        stickyHeader?.let { detachView(it) }
        val any = block(this)
        stickyHeader?.let { attachView(it) }
        return any
    }

    private var adapter: RecyclerView.Adapter<*>? = null
    private val headerPositions: MutableList<Int> = ArrayList(0)
    private val headerPositionsObserver: AdapterDataObserver = HeaderPositionsAdapterDataObserver()

    private var anchorView: View? = null
    private var anchorIndex: Int = -1

    private var stickyHeader: View? = null
    private var stickyHeaderPosition = RecyclerView.NO_POSITION

    private inner class HeaderPositionsAdapterDataObserver : AdapterDataObserver() {
        override fun onChanged() {
            headerPositions.clear()

            val itemCount = adapter?.itemCount ?: 0
            (0 until itemCount).filterTo(headerPositions) {
                isStickyHeader(it)
            }

            if (stickyHeader != null && !headerPositions.contains(stickyHeaderPosition)) {
                scrapStickyHeader()
            }
        }
    }

    inner class TtiviGroupSpanSizeLookup : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int =
            if (isStickyHeader(position)) spanCount else 1
    }
}
