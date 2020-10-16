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

import android.content.Context
import android.content.res.Resources
import android.view.View
import com.tomtom.nk2.core.common.animation.R
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import io.mockk.every
import io.mockk.mockk

open class DismissalStrategyTestCase : TomTomTestCase() {
    private val mockResource = mockk<Resources> {
        every {
            getInteger(
                R.integer.ttivi_animation_dismiss_view_acceleration_finger_lift_px_per_sec2
            )
        } returns ANIMATION_VIEW_ACCELERATION_PULL_PX_PER_SEC2
    }

    val mockContext = mockk<Context> {
        every { resources } returns mockResource
    }

    fun viewAtPosition(position: Float) = mockk<View> {
        every { width } returns VIEW_WIDTH
        every { translationX } returns position
    }

    companion object {
        const val VIEW_WIDTH = 100

        const val DISMISS_VIEW_WIDTH_THRESHOLD = 0.5F
        const val ANIMATION_VIEW_ACCELERATION_PULL_PX_PER_SEC2 = 100

        const val NO_VELOCITY = 0.0F
        const val LOW_VELOCITY = 50.0F
        const val HIGH_VELOCITY = 150.0F

        val NOT_DRAGGED = HorizontalDirectionTracker.Direction.UNDEFINED
        val DRAGGED_TO_LEFT = HorizontalDirectionTracker.Direction.LEFT
        val DRAGGED_TO_RIGHT = HorizontalDirectionTracker.Direction.RIGHT

        val NOT_DISMISSED = null
        val DISMISSED_TO_LEFT = DismissalStrategy.DismissDirection.LEFT
        val DISMISSED_TO_RIGHT = DismissalStrategy.DismissDirection.RIGHT

        const val DEFAULT_VIEW_POSITION = 0.0f
        const val LEFT_THRESHOLD_POSITION = -VIEW_WIDTH * DISMISS_VIEW_WIDTH_THRESHOLD
        const val BEYOND_LEFT_THRESHOLD_POSITION = LEFT_THRESHOLD_POSITION - 1.0F
        const val FAR_BEYOND_LEFT_THRESHOLD_POSITION = LEFT_THRESHOLD_POSITION * 2

        const val RIGHT_THRESHOLD_POSITION = VIEW_WIDTH * DISMISS_VIEW_WIDTH_THRESHOLD
        const val BEYOND_RIGHT_THRESHOLD_POSITION = RIGHT_THRESHOLD_POSITION + 1.0F
        const val FAR_BEYOND_RIGHT_THRESHOLD_POSITION = RIGHT_THRESHOLD_POSITION * 2
    }
}
