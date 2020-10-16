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

package com.tomtom.nk2.core.common.uicontrols.list

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView

/**
 * A decoration used as a sticky header in [TtiviListRecyclerView]
 *
 * @param isHeader expression to define if the item in the itemPosition is a header
 */
class TtiviListStickyHeaderDecoration(
    private val isHeader: (itemPosition: Int) -> Boolean
) : RecyclerView.ItemDecoration() {

    /** Header position in the [RecyclerView] -> header view's [RecyclerView.ViewHolder] */
    private var currentHeader: Pair<Int, RecyclerView.ViewHolder>? = null

    val boundingRect = Rect()

    fun invalidate() {
        currentHeader = null
        boundingRect.setEmpty()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        updateHeaderItem(parent)
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        updateHeaderItem(parent)?.let {
            drawHeader(canvas, it)
        }
    }

    private fun updateHeaderItem(recyclerView: RecyclerView): View? {
        val headerPosition = recyclerView.getChildAt(0)
            ?.let { recyclerView.getChildAdapterPosition(it) }
            ?.takeIf { it != RecyclerView.NO_POSITION }
            ?.let { getHeaderPositionForItem(it) }

        val adapter = recyclerView.adapter

        if (headerPosition == null || adapter == null) {
            currentHeader = null
            return null
        }

        val headerType = adapter.getItemViewType(headerPosition)

        currentHeader?.let { (position, viewHolder) ->
            if (position == headerPosition && viewHolder.itemViewType == headerType) {
                return viewHolder.itemView
            }
        }

        val headerHolder = adapter.onCreateViewHolder(recyclerView, headerType)
        adapter.onBindViewHolder(headerHolder, headerPosition)
        measureLayoutSize(recyclerView, headerHolder.itemView)
        currentHeader = headerPosition to headerHolder
        return headerHolder.itemView
    }

    private fun measureLayoutSize(parent: ViewGroup, view: View) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec =
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        val childWidthSpec = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )

        val childHeightSpec = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )

        // Measure sizes before padding update
        view.measure(childWidthSpec, childHeightSpec)

        // Turn vertical margin to padding
        view.setPadding(
            view.paddingStart,
            view.paddingTop + view.marginTop,
            view.paddingEnd,
            view.paddingBottom + view.marginBottom
        )

        boundingRect.right = view.measuredWidth
        boundingRect.bottom = view.measuredHeight + view.marginTop + view.marginBottom
        view.layout(boundingRect.left, boundingRect.top, boundingRect.right, boundingRect.bottom)
    }

    private fun getHeaderPositionForItem(itemPosition: Int): Int? =
        (0..itemPosition).reversed().firstOrNull(isHeader)

    private fun drawHeader(canvas: Canvas, header: View) {
        canvas.save()
        canvas.translate(header.marginStart.toFloat(), 0f)
        header.draw(canvas)
        canvas.restore()
    }
}
