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
import androidx.lifecycle.MutableLiveData
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import org.junit.Test

class TtiviGroupItemsAdapterTest : TomTomTestCase() {

    @Test
    fun `items without headers`() {
        // GIVEN
        val sut = Adapter(
            MutableLiveData(listOf(CONTENT, CONTENT, CONTENT, CONTENT, CONTENT, CONTENT)),
            mutableListOf()
        )

        // WHEN THEN
        assertEquals(0, sut.getPositionInGroup(0))
        assertEquals(1, sut.getPositionInGroup(1))
        assertEquals(5, sut.getPositionInGroup(5))
    }

    @Test
    fun `items with headers`() {
        // GIVEN
        val sut = Adapter(
            MutableLiveData(listOf(HEADER, CONTENT, CONTENT, HEADER, CONTENT, CONTENT, CONTENT)),
            mutableListOf(0, 3)
        )

        // WHEN THEN
        assertEquals(0, sut.getPositionInGroup(1))
        assertEquals(1, sut.getPositionInGroup(5))
        assertEquals(null, sut.getPositionInGroup(0))
        assertEquals(null, sut.getPositionInGroup(3))
    }

    @Test
    fun `is group`() {
        // GIVEN
        val sut = Adapter(
            MutableLiveData(listOf(HEADER, CONTENT, CONTENT, HEADER, CONTENT, CONTENT, CONTENT)),
            mutableListOf(0, 3)
        )

        // WHEN THEN
        assertTrue(sut.isGroupHeader(0))
        assertTrue(sut.isGroupHeader(3))
        assertFalse(sut.isGroupHeader(1))
        assertFalse(sut.isGroupHeader(2))
        assertFalse(sut.isGroupHeader(5))
    }

    class Adapter(
        override val items: LiveData<List<TtiviListItemViewModel>>,
        override val headerPositions: MutableList<Int>
    ) : TtiviGroupItemsAdapter<TtiviListItemViewModel>

    companion object {
        private val HEADER = object : TtiviListItemViewModel {
            override val type = TtiviListItemViewModel.ListItemType.HEADER
        }

        val CONTENT = object : TtiviListItemViewModel {
            override val type = TtiviListItemViewModel.ListItemType.CONTENT
        }
    }
}
