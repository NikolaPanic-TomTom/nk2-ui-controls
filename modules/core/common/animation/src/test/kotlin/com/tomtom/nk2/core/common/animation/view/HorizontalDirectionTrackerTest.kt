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

package com.tomtom.nk2.core.common.animation.view

import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import org.junit.Test

class HorizontalDirectionTrackerTest : TomTomTestCase() {

    private var sut = HorizontalDirectionTracker()

    @Test
    fun `undefined direction after creation`() {
        // THEN
        assertEquals(HorizontalDirectionTracker.Direction.UNDEFINED, sut.lastRecentDirection)
    }

    @Test
    fun `undefined direction after one update of direction`() {
        // WHEN
        sut.updateDirection(100f)

        // THEN
        assertEquals(HorizontalDirectionTracker.Direction.UNDEFINED, sut.lastRecentDirection)
    }

    @Test
    fun `two points determine left direction`() {
        // WHEN
        sut.updateDirection(100f)
        sut.updateDirection(50f)

        // THEN
        assertEquals(HorizontalDirectionTracker.Direction.LEFT, sut.lastRecentDirection)
    }

    @Test
    fun `two points determine right direction`() {
        // WHEN
        sut.updateDirection(50f)
        sut.updateDirection(100f)

        // THEN
        assertEquals(HorizontalDirectionTracker.Direction.RIGHT, sut.lastRecentDirection)
    }

    @Test
    fun `changing direction`() {
        // GIVEN
        sut.updateDirection(50f)
        sut.updateDirection(100f)
        assertEquals(HorizontalDirectionTracker.Direction.RIGHT, sut.lastRecentDirection)

        // WHEN
        sut.updateDirection(75f)

        // THEN
        assertEquals(HorizontalDirectionTracker.Direction.LEFT, sut.lastRecentDirection)

        // WHEN
        sut.updateDirection(80f)

        // THEN
        assertEquals(HorizontalDirectionTracker.Direction.RIGHT, sut.lastRecentDirection)
    }

    @Test
    fun `identical update do not change direction`() {
        // GIVEN
        sut.updateDirection(50f)
        sut.updateDirection(100f)
        assertEquals(HorizontalDirectionTracker.Direction.RIGHT, sut.lastRecentDirection)

        // WHEN
        sut.updateDirection(100f)

        // THEN
        assertEquals(HorizontalDirectionTracker.Direction.RIGHT, sut.lastRecentDirection)
    }

    @Test
    fun `clear`() {
        // GIVEN
        sut.updateDirection(50f)
        sut.updateDirection(100f)
        assertEquals(HorizontalDirectionTracker.Direction.RIGHT, sut.lastRecentDirection)

        // WHEN
        sut.clear()

        // THEN
        assertEquals(HorizontalDirectionTracker.Direction.UNDEFINED, sut.lastRecentDirection)
    }
}
