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
import android.view.animation.LinearInterpolator
import com.tomtom.nk2.tools.testing.mock.niceMockk
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import io.mockk.verifySequence
import kotlin.test.assertFails
import org.junit.Test

class AnimatorSetExtensionsTest : TomTomTestCase() {

    private val sampleAnimator1 = ValueAnimator.ofInt(0, 5)
    private val sampleAnimator2 = ValueAnimator.ofInt(0, 10)
    private val mockUpdateListener = niceMockk<(List<Int>) -> Unit>()

    private val sut = AnimatorSet().apply {
        duration = 100
        interpolator = LinearInterpolator()
    }

    @Test
    fun `progression of single animator`() {
        // GIVEN
        sut.playTogether(listOf(sampleAnimator1), mockUpdateListener)

        // WHEN
        onMainLooper {
            sut.start()
        }

        // THEN
        verifySequence {
            mockUpdateListener(listOf(0))
            mockUpdateListener(listOf(0))
            mockUpdateListener(listOf(0))
            mockUpdateListener(listOf(1))
            mockUpdateListener(listOf(2))
            mockUpdateListener(listOf(3))
            mockUpdateListener(listOf(4))
            mockUpdateListener(listOf(5))
        }
    }

    @Test
    fun `progression of multiple animators`() {
        // GIVEN
        sut.playTogether(listOf(sampleAnimator1, sampleAnimator2), mockUpdateListener)

        // WHEN
        onMainLooper {
            sut.start()
        }

        // THEN
        verifySequence {
            mockUpdateListener(listOf(0, 0))
            mockUpdateListener(listOf(0, 0))
            mockUpdateListener(listOf(0, 0))
            mockUpdateListener(listOf(1, 2))
            mockUpdateListener(listOf(2, 4))
            mockUpdateListener(listOf(3, 6))
            mockUpdateListener(listOf(4, 8))
            mockUpdateListener(listOf(5, 10))
        }
    }

    @Test
    fun `no animators`() {
        assertFails { sut.playTogether(listOf(), mockUpdateListener) }
    }

    @Test
    fun `progression of pairs`() {
        // GIVEN
        sut.playTogether(Pair(0, 5), Pair(0, 10)) { mockUpdateListener(it) }

        // WHEN
        onMainLooper {
            sut.start()
        }

        // THEN
        verifySequence {
            mockUpdateListener(listOf(0, 0))
            mockUpdateListener(listOf(0, 0))
            mockUpdateListener(listOf(0, 0))
            mockUpdateListener(listOf(1, 2))
            mockUpdateListener(listOf(2, 4))
            mockUpdateListener(listOf(3, 6))
            mockUpdateListener(listOf(4, 8))
            mockUpdateListener(listOf(5, 10))
        }
    }
}
