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

import android.view.View
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import com.tomtom.nk2.core.common.animation.areAnimationsInstant
import com.tomtom.nk2.core.common.animation.view.MovementSpringAnimation.Direction
import kotlin.math.absoluteValue

/**
 * A custom animation to perform movement of a view on the screen.
 *
 * @param view View to animate.
 * @param direction Direction to move. For more details refer to [Direction].
 * @param fadingEnabled Whether fading effect is enabled.
 */
open class MovementSpringAnimation(
    private val view: View,
    private val direction: Direction,
    private val fadingEnabled: Boolean
) {
    enum class Direction {
        HORIZONTAL,
        VERTICAL
    }

    /**
     * Performs an animation to move a view to [positionPx] with [velocityPxPerSec].
     *
     * @param positionPx Final position in pixels.
     * @param velocityPxPerSec Movement velocity in pixels per seconds.
     * @param animationRange Minimum and maximum position to be animated. For more details, see
     *     [DynamicAnimation.setMinValue] and [DynamicAnimation.setMaxValue].
     * @param endListener An action to be executed when animation ends or has been canceled.
     */
    fun animateToPosition(
        positionPx: Float,
        velocityPxPerSec: Float? = null,
        animationRange: ClosedFloatingPointRange<Float>? = null,
        endListener: ((canceled: Boolean) -> Unit)? = null
    ) {
        // The regular animation played here is a dynamic animation, which does not have a duration
        // defined. Because of that, it's not possible to make the regular animation instant during
        // tests (or at least, we couldn't find a way to achieve that). Instead, we skip the
        // animation manually by instantly transitioning the view to the animation's end state.
        if (areAnimationsInstant(view.context)) {
            when (direction) {
                Direction.HORIZONTAL -> view.translationX = positionPx
                Direction.VERTICAL -> view.translationY = positionPx
            }
            view.post { endListener?.invoke(false) }
            return
        }

        SpringAnimation(view, direction.toViewProperty(), positionPx).apply {
            SpringAnimationConfiguration(view.context).applyTo(this)

            setStartVelocity(velocityPxPerSec ?: NON_GESTURE_INITIAL_DISMISSAL_VELOCITY_PX_PER_SEC)

            animationRange?.let {
                setMinValue(it.start)
                setMaxValue(it.endInclusive)
            }

            addEndListener { _, canceled, _, _ ->
                endListener?.invoke(canceled)
            }

            if (fadingEnabled) {
                val fadeAnimation = SpringAnimation(view, DynamicAnimation.ALPHA, 0.0F)
                addUpdateListener { _, value, _ ->
                    // It should fade out by the end of animation so let's use the main
                    // animation's position.
                    val alphaDelta = when (positionPx) {
                        0.0F -> 0.0F
                        else -> (value / positionPx).absoluteValue
                    }
                    fadeAnimation.animateToFinalPosition(
                        1.0f - alphaDelta
                    )
                }
            }
        }.start()
    }

    private fun Direction.toViewProperty() = when (this) {
        Direction.HORIZONTAL -> DynamicAnimation.TRANSLATION_X
        Direction.VERTICAL -> DynamicAnimation.TRANSLATION_Y
    }

    companion object {
        private const val NON_GESTURE_INITIAL_DISMISSAL_VELOCITY_PX_PER_SEC = 0f
    }
}

class HorizontalMovementSpringAnimation(view: View, fadingEnabled: Boolean) :
    MovementSpringAnimation(view, Direction.HORIZONTAL, fadingEnabled)

class VerticalMovementSpringAnimation(view: View, fadingEnabled: Boolean) :
    MovementSpringAnimation(view, Direction.VERTICAL, fadingEnabled)
