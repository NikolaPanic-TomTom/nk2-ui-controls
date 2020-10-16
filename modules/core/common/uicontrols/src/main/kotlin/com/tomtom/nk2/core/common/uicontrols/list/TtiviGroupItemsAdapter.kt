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

import androidx.lifecycle.LiveData

/**
 * This adapter can be extended to be used together with [TtiviAutoFitGridLayoutManager] to create
 * a list which has group headers.
 */
interface TtiviGroupItemsAdapter<T> where T : TtiviListItemViewModel {
    /**
     * Header and content items.
     */
    val items: LiveData<List<T>>

    /**
     * Position of group headers in [items].
     */
    val headerPositions: MutableList<Int>

    /**
     * Indicate whether the item at [position] is a header of a group.
     * @param position Position of an item.
     * @return `true` if the item at [position] is a header, `false` otherwise.
     */
    fun isGroupHeader(position: Int): Boolean =
        items.value?.get(position)?.type == TtiviListItemViewModel.ListItemType.HEADER

    /**
     * Returns the position of an item relative to its group.
     * @param position Position of an item.
     * @return Position of an item relative to its group. `null` if the item is a header.
     * [position] if there are no headers.
     */
    fun getPositionInGroup(position: Int): Int? {
        val headerPosition: Int? = (headerPositions.size - 1 downTo 0)
            .firstOrNull { position >= headerPositions[it] }
            ?.let { headerPositions[it] }

        val positionInGroup = when (headerPosition) {
            null -> position
            else -> position - headerPosition - 1
        }

        return positionInGroup.takeIf { it != -1 }
    }
}
