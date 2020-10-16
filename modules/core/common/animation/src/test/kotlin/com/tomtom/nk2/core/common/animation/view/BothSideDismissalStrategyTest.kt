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

import org.junit.Test

class BothSideDismissalStrategyTest : DismissalStrategyTestCase() {
    private val sut = BothSideDismissalStrategy(mockContext, DISMISS_VIEW_WIDTH_THRESHOLD)

    @Test
    fun `Do not dismiss if not dragged from default position`() {
        // GIVEN
        val view = viewAtPosition(DEFAULT_VIEW_POSITION)

        // WHEN
        val dismissDirection = sut.getDismissDirection(view, NO_VELOCITY, NOT_DRAGGED)

        // THEN
        assertEquals(NOT_DISMISSED, dismissDirection)
    }

    @Test
    fun `Do not dismiss if slightly dragged left from default position`() {
        // GIVEN
        val view = viewAtPosition(DEFAULT_VIEW_POSITION)

        // WHEN
        val dismissDirection =
            sut.getDismissDirection(view, -LOW_VELOCITY, DRAGGED_TO_LEFT)

        // THEN
        assertEquals(NOT_DISMISSED, dismissDirection)
    }

    @Test
    fun `Dismiss to the left if very dragged left from default position`() {
        // GIVEN
        val view = viewAtPosition(DEFAULT_VIEW_POSITION)

        // WHEN
        val dismissDirection =
            sut.getDismissDirection(view, -HIGH_VELOCITY, DRAGGED_TO_LEFT)

        // THEN
        assertEquals(DISMISSED_TO_LEFT, dismissDirection)
    }

    @Test
    fun `Do not dismiss if not dragged from left threshold position`() {
        // GIVEN
        val view = viewAtPosition(LEFT_THRESHOLD_POSITION)

        // WHEN
        val dismissDirection =
            sut.getDismissDirection(view, NO_VELOCITY, NOT_DRAGGED)

        // THEN
        assertEquals(NOT_DISMISSED, dismissDirection)
    }

    @Test
    fun `Dismiss to the left if not dragged from beyond left threshold position`() {
        // GIVEN
        val view = viewAtPosition(BEYOND_LEFT_THRESHOLD_POSITION)

        // WHEN
        val dismissDirection =
            sut.getDismissDirection(view, NO_VELOCITY, NOT_DRAGGED)

        // THEN
        assertEquals(DISMISSED_TO_LEFT, dismissDirection)
    }

    @Test
    fun `Dismiss to the left if slightly dragged right from far beyond left threshold position`() {
        // GIVEN
        val view = viewAtPosition(FAR_BEYOND_LEFT_THRESHOLD_POSITION)

        // WHEN
        val dismissDirection =
            sut.getDismissDirection(view, -LOW_VELOCITY, DRAGGED_TO_RIGHT)

        // THEN
        assertEquals(DISMISSED_TO_LEFT, dismissDirection)
    }

    @Test
    fun `Do not dismiss if very dragged right from far beyond left threshold position`() {
        // GIVEN
        val view = viewAtPosition(FAR_BEYOND_LEFT_THRESHOLD_POSITION)

        // WHEN
        val dismissDirection =
            sut.getDismissDirection(view, -HIGH_VELOCITY, DRAGGED_TO_RIGHT)

        // THEN
        assertEquals(NOT_DISMISSED, dismissDirection)
    }

    @Test
    fun `Do not dismiss if slightly dragged right from default position`() {
        // GIVEN
        val view = viewAtPosition(DEFAULT_VIEW_POSITION)

        // WHEN
        val dismissDirection =
            sut.getDismissDirection(view, LOW_VELOCITY, DRAGGED_TO_RIGHT)

        // THEN
        assertEquals(NOT_DISMISSED, dismissDirection)
    }

    @Test
    fun `Dismiss to the right if very dragged right from default position`() {
        // GIVEN
        val view = viewAtPosition(DEFAULT_VIEW_POSITION)

        // WHEN
        val dismissDirection =
            sut.getDismissDirection(view, HIGH_VELOCITY, DRAGGED_TO_RIGHT)

        // THEN
        assertEquals(DISMISSED_TO_RIGHT, dismissDirection)
    }

    @Test
    fun `Do not dismiss if not dragged from right threshold position`() {
        // GIVEN
        val view = viewAtPosition(RIGHT_THRESHOLD_POSITION)

        // WHEN
        val dismissDirection =
            sut.getDismissDirection(view, NO_VELOCITY, NOT_DRAGGED)

        // THEN
        assertEquals(NOT_DISMISSED, dismissDirection)
    }

    @Test
    fun `Dismiss to the right if not dragged from beyond right threshold position`() {
        // GIVEN
        val view = viewAtPosition(BEYOND_RIGHT_THRESHOLD_POSITION)

        // WHEN
        val dismissDirection =
            sut.getDismissDirection(view, NO_VELOCITY, NOT_DRAGGED)

        // THEN
        assertEquals(DISMISSED_TO_RIGHT, dismissDirection)
    }

    @Test
    fun `Dismiss to the right if slightly dragged left from far beyond right threshold position`() {
        // GIVEN
        val view = viewAtPosition(FAR_BEYOND_RIGHT_THRESHOLD_POSITION)

        // WHEN
        val dismissDirection =
            sut.getDismissDirection(view, LOW_VELOCITY, DRAGGED_TO_LEFT)

        // THEN
        assertEquals(DISMISSED_TO_RIGHT, dismissDirection)
    }

    @Test
    fun `Do not dismiss if very dragged left from far beyond right threshold position`() {
        // GIVEN
        val view = viewAtPosition(FAR_BEYOND_RIGHT_THRESHOLD_POSITION)

        // WHEN
        val dismissDirection =
            sut.getDismissDirection(view, HIGH_VELOCITY, DRAGGED_TO_LEFT)

        // THEN
        assertEquals(NOT_DISMISSED, dismissDirection)
    }
}
