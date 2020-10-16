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

package com.tomtom.nk2.core.common.util

import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener

/**
 * Sets the animation listener on the animation, dispatching only the
 * [AnimationListener.onAnimationEnd] callback.
 *
 * **Note:** This clears any animation listener previously set with
 * [Animation.setAnimationListener], and will be cleared by any subsequent call.
 */
fun Animation.setAnimationEndListener(onAnimationEnd: () -> Unit) {
    setAnimationListener(object : DefaultAnimationListener {
        override fun onAnimationEnd(animation: Animation?) {
            onAnimationEnd()
        }
    })
}

/**
 * An extension of [AnimationListener] with default implementations so that not every method has to
 * be implemented.
 */
interface DefaultAnimationListener : AnimationListener {
    override fun onAnimationRepeat(animation: Animation?) {}

    override fun onAnimationEnd(animation: Animation?) {}

    override fun onAnimationStart(animation: Animation?) {}
}
