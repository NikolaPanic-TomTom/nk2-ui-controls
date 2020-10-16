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

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomtom.nk2.core.common.uicontrols.list.TtiviGroupItemsAdapter

class TtiviGridItemDividerDecoration(
    private val itemSpacingHorizontalPx: Int,
    private val itemSpacingVerticalPx: Int = itemSpacingHorizontalPx
) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager

        outRect.apply {
            when (layoutManager) {
                is GridLayoutManager -> {
                    var position = parent.getChildAdapterPosition(view)

                    if (parent.adapter is TtiviGroupItemsAdapter<*>) {
                        val groupAdapter = parent.adapter as TtiviGroupItemsAdapter<*>
                        position = (groupAdapter.getPositionInGroup(position) ?: return)
                    }

                    val spanCount = layoutManager.spanCount
                    val column = position % spanCount
                    left = column * itemSpacingHorizontalPx / spanCount
                    right =
                        itemSpacingHorizontalPx - (column + 1) * itemSpacingHorizontalPx / spanCount
                    if (position >= spanCount) {
                        top = itemSpacingVerticalPx
                    }
                }
            }
        }
    }
}
