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
import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * A custom Android animation to perform animated movement of a view on the screen.
 *
 * @param view View to animate.
 * @param finalPositionPx Final position of a view.
 * @param fadingEnabled Whether fading effect is enabled where a view fades out during movement.
 */
class VerticalMoveAnimation(
    private val view: View,
    private val finalPositionPx: Int,
    private val fadingEnabled: Boolean
) : Animation() {
    private var animationListener: AnimationListener? = null

    /**
     * Whether or not the animation has started before. Can be used to prevent starting the
     * animation multiple times.
     */
    private var started = false

    override fun setAnimationListener(listener: AnimationListener) {
        animationListener = listener
    }

    /**
     * Note that this implementation does not replace the specified [transformation].
     */
    override fun applyTransformation(
        interpolatedTime: Float,
        transformation: Transformation
    ) {
        if (started) {
            return
        }

        started = true
        animationListener?.onAnimationStart(this)

        VerticalMovementSpringAnimation(view, fadingEnabled)
            .animateToPosition(finalPositionPx.toFloat()) {
                animationListener?.onAnimationEnd(this)
            }
    }
}
