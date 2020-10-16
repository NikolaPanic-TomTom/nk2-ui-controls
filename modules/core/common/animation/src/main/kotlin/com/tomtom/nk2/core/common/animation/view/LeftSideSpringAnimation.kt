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
import com.tomtom.nk2.core.common.animation.view.LeftSideSpringAnimation.TransitionType

/**
 * A custom Android animation to perform left side animated enter and exit of a view on the screen.
 *
 * @param view View to animate.
 * @param transitionType Transition type. For more details refer to [TransitionType].
 * @param fadingEnabled Whether fading effect is enabled.
 */
class LeftSideSpringAnimation(
    private val view: View,
    private val transitionType: TransitionType,
    private val fadingEnabled: Boolean
) : Animation() {

    /**
     * Type of animation transition.
     */
    enum class TransitionType {
        /**
         * A view appears on the screen.
         */
        ENTER,

        /**
         * A view disappears from the screen.
         */
        EXIT
    }

    private var width: Int? = null

    private var isInProgress = false
    private var animationListener: AnimationListener? = null

    private fun onAnimationStart() {
        isInProgress = true
        animationListener?.onAnimationStart(this)
    }

    private fun onAnimationEnd() {
        isInProgress = false
        animationListener?.onAnimationEnd(this)
    }

    override fun setAnimationListener(listener: AnimationListener) {
        animationListener = listener
    }

    override fun applyTransformation(
        interpolatedTime: Float,
        transformation: Transformation
    ) = width.let {
        require(it != null) { "Cannot apply transformation before setting 'width'." }

        val animation = HorizontalMovementSpringAnimation(view, fadingEnabled)

        onAnimationStart()

        when (transitionType) {
            TransitionType.ENTER -> {
                view.translationX = -it.toFloat()
                animation.animateToPosition(TASK_PANEL_REST_POSITION_PX) { onAnimationEnd() }
            }
            TransitionType.EXIT -> animation.animateToPosition(-it.toFloat()) { onAnimationEnd() }
        }
    }

    override fun initialize(
        width: Int,
        height: Int,
        parentWidth: Int,
        parentHeight: Int
    ) {
        super.initialize(width, height, parentWidth, parentHeight)
        this.width = width
    }

    override fun getTransformation(
        currentTime: Long,
        outTransformation: Transformation?
    ): Boolean = isInProgress

    companion object {
        private const val TASK_PANEL_REST_POSITION_PX = 0f
    }
}
