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
import kotlin.math.absoluteValue

/**
 * A [DraggedViewPositionUpdater] updates horizontal view translation.
 *
 * @param view View to animate.
 * @param fadingEnabled Whether fading effect is enabled.
 */
class DraggedViewPositionUpdater(private val view: View, private val fadingEnabled: Boolean) {

    /**
     * Moves the view.
     *
     * @param horizontalPositionPx Horizontal position from touch event.
     */
    fun onMoved(horizontalPositionPx: Float) {
        view.translationX = horizontalPositionPx
        if (fadingEnabled) {
            val offset = view.translationX.absoluteValue.coerceIn(
                0.0F,
                view.width.toFloat()
            )
            view.alpha = 1.0F - offset / view.width
        }
    }
}
