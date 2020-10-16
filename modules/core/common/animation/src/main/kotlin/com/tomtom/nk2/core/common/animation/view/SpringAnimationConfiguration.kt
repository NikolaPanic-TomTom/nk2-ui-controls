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
import androidx.dynamicanimation.animation.SpringAnimation
import com.tomtom.nk2.core.common.animation.R

class SpringAnimationConfiguration(context: Context) {
    private val springStiffness = context.resources.getInteger(
        R.integer.ttivi_animation_dismiss_view_spring_stiffness
    ).toFloat()

    private val springDampingRatio = context.resources.getInteger(
        R.integer.ttivi_animation_dismiss_view_spring_damping_ratio
    ).toFloat() / NORMALIZATION_DIVIDER

    init {
        require(springDampingRatio > 0f) { "Spring damping ratio is out of range." }
    }

    fun applyTo(animation: SpringAnimation) = animation.apply {
        spring.dampingRatio = springDampingRatio
        spring.stiffness = springStiffness
    }

    companion object {
        /**
         * This constant is used to normalize spring damping ratio.
         * Value stored in resources is in range 0 to 100, but value in range 0 to 1 is expected
         * in code.
         */
        const val NORMALIZATION_DIVIDER = 100f
    }
}
