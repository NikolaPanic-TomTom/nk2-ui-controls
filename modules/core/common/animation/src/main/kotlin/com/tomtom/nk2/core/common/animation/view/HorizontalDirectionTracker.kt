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

/**
 * A [HorizontalDirectionTracker] tracks the direction of horizontal movement.
 */
class HorizontalDirectionTracker {

    /**
     * Direction of horizontal movement.
     */
    enum class Direction {
        LEFT,
        RIGHT,
        UNDEFINED
    }

    /**
     * Direction calculated from recently provided movements.
     */
    var lastRecentDirection = Direction.UNDEFINED
        private set

    private var previousHorizontalPositionPx: Float? = null

    /**
     * Consumes horizontal position.
     *
     * @param horizontalPositionPx Horizontal position. The direction is calculated based on the
     * position.
     */
    fun updateDirection(horizontalPositionPx: Float) {
        previousHorizontalPositionPx?.let {
            if (it == horizontalPositionPx) return
            lastRecentDirection = if (it < horizontalPositionPx) {
                Direction.RIGHT
            } else {
                Direction.LEFT
            }
        }
        previousHorizontalPositionPx = horizontalPositionPx
    }

    /**
     * Clears the state of a [HorizontalDirectionTracker] object.
     */
    fun clear() {
        lastRecentDirection = Direction.UNDEFINED
        previousHorizontalPositionPx = null
    }
}
