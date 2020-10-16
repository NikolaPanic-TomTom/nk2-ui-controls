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
import android.view.View
import com.tomtom.nk2.core.common.animation.R
import com.tomtom.nk2.core.common.animation.view.HorizontalDirectionTracker.Direction
import kotlin.math.absoluteValue
import kotlin.math.pow

/**
 * Define dismissal behavior of an animation.
 *
 * @param context Context.
 * @param dismissViewWidthThreshold A constant to determine width proportion threshold to dismiss a
 * view. This constant should be in range 0f to 1f.
 */
abstract class DismissalStrategy(
    private val context: Context,
    private val dismissViewWidthThreshold: Float
) {
    /** Direction of view dismissal. */
    enum class DismissDirection {
        LEFT,
        RIGHT
    }

    private val accelerationAtFingerLiftPxPerSec2 = context.resources.getInteger(
        R.integer.ttivi_animation_dismiss_view_acceleration_finger_lift_px_per_sec2
    ).toFloat()

    /**
     * Gets a direction that a [view] dragged with [horizontalVelocityPx] in [horizontalDirection]
     * should be dismissed, if it should, otherwise returns the null.
     */
    fun getDismissDirection(
        view: View,
        horizontalVelocityPx: Float,
        horizontalDirection: Direction
    ): DismissDirection? {
        val motionDistancePx = view.translationX
        val thresholdPx = view.width * dismissViewWidthThreshold

        val correction = when (horizontalDirection) {
            Direction.LEFT -> -1.0F
            Direction.RIGHT -> 1.0F
            else -> 0.0F
        }

        val calculatedDistancePx = calculateDistance(
            motionDistancePx,
            correction * thresholdPx,
            correction * horizontalVelocityPx.absoluteValue
        )

        return getDismissDirection(calculatedDistancePx, thresholdPx)
    }

    /** Get an area within which a [view] can be dragged. */
    abstract fun getMovableArea(view: View): ClosedFloatingPointRange<Float>

    protected abstract fun getDismissDirection(
        distancePx: Float,
        thresholdPx: Float
    ): DismissDirection?

    /**
     * Calculate the distance of motion based on the dragging distance and velocity.
     * Calculations are based on: [UX Spec](https://confluence.tomtomgroup.com/x/FlHPLw).
     *
     * @param initialDistancePx Distance of dragging.
     * @param gravitationThresholdPx Point of gravitation.
     * @param initialVelocityPxPerSec Velocity just before finger is lifted from motion.
     *
     * @returns Distance.
     */
    private fun calculateDistance(
        initialDistancePx: Float,
        gravitationThresholdPx: Float,
        initialVelocityPxPerSec: Float
    ): Float {
        val directionalAccelerationPxPerSec2 = if (initialDistancePx > gravitationThresholdPx) {
            -accelerationAtFingerLiftPxPerSec2
        } else {
            accelerationAtFingerLiftPxPerSec2
        }
        val durationSec = (initialVelocityPxPerSec / accelerationAtFingerLiftPxPerSec2)
            .absoluteValue.coerceAtMost(MAXIMUM_TIME_OF_MOTION_SEC)

        /**
         * Below is calculated integral from velocity equation:
         * velocity(durationSec) =
         *      initialDistancePx + directionalAccelerationPxPerSec2 * durationSec
         */
        return initialDistancePx +
            initialVelocityPxPerSec * durationSec -
            directionalAccelerationPxPerSec2 * durationSec.pow(2) * 0.5f
    }

    companion object {
        /**
         * This constant is used in calculation of distance based on some specific acceleration.
         */
        const val MAXIMUM_TIME_OF_MOTION_SEC = 1000f
    }
}
