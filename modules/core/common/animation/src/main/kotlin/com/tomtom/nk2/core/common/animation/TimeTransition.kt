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

package com.tomtom.nk2.core.common.animation

import android.animation.ValueAnimator
import android.os.SystemClock

/**
 * Contains the time transition information from a start to an end point. This class helps to
 * animate views which can use time stamps from start and end points.
 *
 * [endTimeMs] shall be greater than or equal to [startTimeMs].
 * [startFraction] shall be in range [0.0,1.0].
 * [endFraction] shall be in range [0.0,1.0].
 */
data class TimeTransition(
    /**
     * The transition start point in range [0.0,1.0]
     */
    val startFraction: Double,
    /**
     * The transition end point in range [0.0,1.0]
     */
    val endFraction: Double,
    /**
     * The transition start time in milliseconds.
     */
    val startTimeMs: Long,
    /**
     * The transition end time in milliseconds.
     */
    val endTimeMs: Long
) {
    init {
        require(endTimeMs >= startTimeMs)
        require(startFraction in 0.0..1.0)
        require(endFraction in 0.0..1.0)
    }

    val durationMs = endTimeMs - startTimeMs
}

fun ValueAnimator.update(timeTransition: TimeTransition, vararg values: Int) {
    cancel()
    setIntValues(*values)
    updateTime(timeTransition)
    start()
}

fun ValueAnimator.update(timeTransition: TimeTransition, vararg values: Float) {
    cancel()
    setFloatValues(*values)
    updateTime(timeTransition)
    start()
}

fun ValueAnimator.update(timeTransition: TimeTransition, vararg values: Any) {
    cancel()
    setObjectValues(*values)
    updateTime(timeTransition)
    start()
}

private fun ValueAnimator.updateTime(timeTransition: TimeTransition) {
    duration = timeTransition.durationMs
    val currentTime = SystemClock.elapsedRealtime()
    currentPlayTime = currentTime - timeTransition.startTimeMs
    // In case the start time is in future we need to delay the animation start.
    startDelay = timeTransition.startTimeMs - currentTime
}
