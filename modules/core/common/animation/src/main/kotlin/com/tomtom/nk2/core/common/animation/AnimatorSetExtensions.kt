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

import android.animation.AnimatorSet
import android.animation.ValueAnimator

/**
 * A helper function to play multiple [ValueAnimator]s together and emit callbacks for each batch of
 * updates. This assumes that `this` has a duration specified such that all [ValueAnimator]s run for
 * the same amount of time, and that all [ValueAnimator]s animate the same type, namely [T].
 */
fun <T> AnimatorSet.playTogether(
    animators: Collection<ValueAnimator>,
    updateListener: (List<T>) -> Unit
) {
    require(animators.isNotEmpty()) { "No animators provided." }

    animators.last().addUpdateListener {
        updateListener(animators.map {
            @Suppress("UNCHECKED_CAST") // ValueAnimator is not strongly typed.
            it.animatedValue as T
        })
    }
    playTogether(animators)
}

/**
 * A helper function to create [ValueAnimator]s for multiple transitions between [Int] values, play
 * them together, and emit callbacks for each batch of updates. This assumes that `this` has a
 * duration specified such that all [ValueAnimator]s run for the same amount of time.
 */
fun AnimatorSet.playTogether(
    vararg animatorValues: Pair<Int, Int>,
    updateListener: (List<Int>) -> Unit
) = playTogether(animatorValues.map { ValueAnimator.ofInt(it.first, it.second) }, updateListener)
