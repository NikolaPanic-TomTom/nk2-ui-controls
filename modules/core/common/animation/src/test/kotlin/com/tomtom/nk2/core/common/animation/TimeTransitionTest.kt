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

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import com.tomtom.nk2.tools.testing.mock.niceMockk
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import io.mockk.clearAllMocks
import io.mockk.verify
import java.lang.IllegalArgumentException
import org.junit.Test
import org.robolectric.annotation.LooperMode
import org.robolectric.shadows.ShadowLooper

@LooperMode(LooperMode.Mode.PAUSED)
class TimeTransitionTest : TomTomTestCase() {

    private val intValueAnimator = ValueAnimator.ofInt(0, 10)
    private val floatValueAnimator = ValueAnimator.ofFloat(0.0f, 10.0f)
    private val objectValueAnimator =
        ValueAnimator.ofObject(TypeEvaluator<Long> { fraction, startValue, endValue ->
            startValue + ((endValue - startValue) * fraction).toLong()
        }, 0L, 10L)

    private val mockUpdateListener = niceMockk<ValueAnimator.AnimatorUpdateListener>()

    @Test
    fun `constructor`() {
        // GIVEN-WHEN-THEN
        TimeTransition(0.0, 1.0, 100L, 100L)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `wrong start fraction out of range negative`() {
        // GIVEN-WHEN-THEN
        TimeTransition(-0.1, 1.0, 100L, 100L)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `wrong start fraction out of range greater than 1`() {
        // GIVEN-WHEN-THEN
        TimeTransition(1.1, 1.0, 100L, 100L)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `wrong end fraction out of range negative`() {
        // GIVEN-WHEN-THEN
        TimeTransition(0.0, -0.1, 100L, 100L)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `wrong end fraction out of range greater than 1`() {
        // GIVEN-WHEN-THEN
        TimeTransition(0.0, 1.1, 100L, 100L)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `wrong startTime greater than endTime`() {
        // GIVEN-WHEN-THEN
        TimeTransition(0.0, 1.0, 101L, 100L)
    }

    @Test
    fun `update valueAnimator of Int`() {
        // GIVEN Elapsed time is 100ms.
        val sut = TimeTransition(0.0, 1.0, 100L, 200L)
        intValueAnimator.addUpdateListener(mockUpdateListener)

        verify(exactly = 0) {
            mockUpdateListener.onAnimationUpdate(intValueAnimator)
        }

        // WHEN Update the animator with SUT.
        intValueAnimator.update(sut, 0, 10)

        // THEN 2 calls to listener caused by currentPlayTime update and start().
        verify(exactly = 2) {
            mockUpdateListener.onAnimationUpdate(intValueAnimator)
        }
        assertEquals(0.0f, intValueAnimator.animatedFraction)
        assertEquals(0, intValueAnimator.animatedValue as Int)
        clearAllMocks()

        // WHEN Animation is played.
        ShadowLooper.idleMainLooper()

        // THEN 100ms to 200ms every milliseconds animate time 0..100 inclusive is 100+1 calls.
        verify(exactly = 200 - 100 + 1) {
            mockUpdateListener.onAnimationUpdate(intValueAnimator)
        }
        assertEquals(1.0f, intValueAnimator.animatedFraction)
        assertEquals(10, intValueAnimator.animatedValue as Int)
    }

    @Test
    fun `update valueAnimator of Int with animation already started`() {
        // GIVEN Elapsed time is 100ms.
        val sut = TimeTransition(0.0, 1.0, 50L, 150L)
        intValueAnimator.addUpdateListener(mockUpdateListener)
        verify(exactly = 0) {
            mockUpdateListener.onAnimationUpdate(intValueAnimator)
        }

        // WHEN Update the animator with SUT.
        intValueAnimator.update(sut, 0, 10)

        // THEN 2 calls to listener caused by currentPlayTime update and start().
        verify(exactly = 2) {
            mockUpdateListener.onAnimationUpdate(intValueAnimator)
        }
        assertEquals(0.5f, intValueAnimator.animatedFraction)
        assertEquals(5, intValueAnimator.animatedValue as Int)
        clearAllMocks()

        // WHEN Animation is played.
        ShadowLooper.idleMainLooper()

        // THEN 100ms to 150ms every milliseconds animate time 50..100 inclusive is 50+1 calls.
        verify(exactly = 150 - 100 + 1) {
            mockUpdateListener.onAnimationUpdate(intValueAnimator)
        }
        assertEquals(1.0f, intValueAnimator.animatedFraction)
        assertEquals(10, intValueAnimator.animatedValue as Int)
    }

    @Test
    fun `update valueAnimator of Int with animation starting in the future`() {
        // GIVEN Elapsed time is 100ms.
        val sut = TimeTransition(0.0, 1.0, 150L, 250L)
        intValueAnimator.addUpdateListener(mockUpdateListener)
        verify(exactly = 0) {
            mockUpdateListener.onAnimationUpdate(intValueAnimator)
        }

        // WHEN Update the animator with SUT.
        intValueAnimator.update(sut, 0, 10)

        // THEN 2 calls to listener caused by currentPlayTime update and start().
        verify(exactly = 2) {
            mockUpdateListener.onAnimationUpdate(intValueAnimator)
        }
        assertEquals(0.0f, intValueAnimator.animatedFraction)
        assertEquals(0, intValueAnimator.animatedValue as Int)
        clearAllMocks()

        // WHEN Animation is played.
        ShadowLooper.idleMainLooper()

        // THEN 150ms to 250ms every milliseconds animate time 0..100 inclusive is 100+1 calls.
        verify(exactly = 250 - 150 + 1) {
            mockUpdateListener.onAnimationUpdate(intValueAnimator)
        }
        assertEquals(1.0f, intValueAnimator.animatedFraction)
        assertEquals(10, intValueAnimator.animatedValue as Int)
    }

    @Test
    fun `update valueAnimator of Float`() {
        // GIVEN Elapsed time is 100ms.
        val sut = TimeTransition(0.0, 1.0, 100L, 200L)
        floatValueAnimator.addUpdateListener(mockUpdateListener)
        verify(exactly = 0) {
            mockUpdateListener.onAnimationUpdate(floatValueAnimator)
        }

        // WHEN Update the animator with SUT.
        floatValueAnimator.update(sut, 0.0f, 10.0f)

        // THEN 2 calls to listener caused by currentPlayTime update and start().
        verify(exactly = 2) {
            mockUpdateListener.onAnimationUpdate(floatValueAnimator)
        }
        assertEquals(0.0f, floatValueAnimator.animatedFraction)
        assertEquals(0.0f, floatValueAnimator.animatedValue as Float)
        clearAllMocks()

        // WHEN Animation is played.
        ShadowLooper.idleMainLooper()

        // THEN 100ms to 200ms every milliseconds, animate time 0..100 inclusive is 100+1 calls.
        verify(exactly = 200 - 100 + 1) {
            mockUpdateListener.onAnimationUpdate(floatValueAnimator)
        }
        assertEquals(1.0f, floatValueAnimator.animatedFraction)
        assertEquals(10.0f, floatValueAnimator.animatedValue as Float)
    }

    @Test
    fun `update valueAnimator of Object`() {
        // GIVEN Elapsed time is 100ms.
        val sut = TimeTransition(0.0, 1.0, 100L, 200L)
        objectValueAnimator.addUpdateListener(mockUpdateListener)
        verify(exactly = 0) {
            mockUpdateListener.onAnimationUpdate(objectValueAnimator)
        }

        // WHEN Update the animator with SUT.
        objectValueAnimator.update(sut, 0L, 10L)

        // THEN 2 calls to listener caused by currentPlayTime update and start().
        verify(exactly = 2) {
            mockUpdateListener.onAnimationUpdate(objectValueAnimator)
        }
        assertEquals(0.0f, objectValueAnimator.animatedFraction)
        assertEquals(0L, objectValueAnimator.animatedValue as Long)
        clearAllMocks()

        // WHEN Animation is played.
        ShadowLooper.idleMainLooper()

        // THEN 100ms to 200ms every milliseconds, animate time 0..100 inclusive is 100+1 calls.
        verify(exactly = 200 - 100 + 1) {
            mockUpdateListener.onAnimationUpdate(objectValueAnimator)
        }
        assertEquals(1.0f, objectValueAnimator.animatedFraction)
        assertEquals(10L, objectValueAnimator.animatedValue as Long)
    }

    @Test
    fun `update valueAnimator of Object with different value ranges`() {
        // GIVEN Elapsed time is 100ms.
        val sut = TimeTransition(0.0, 1.0, 100L, 200L)
        objectValueAnimator.addUpdateListener(mockUpdateListener)
        verify(exactly = 0) {
            mockUpdateListener.onAnimationUpdate(objectValueAnimator)
        }

        // WHEN Update the animator with SUT.
        objectValueAnimator.update(sut, -200L, 200L)

        // THEN 2 calls to listener caused by currentPlayTime update and start().
        verify(exactly = 2) {
            mockUpdateListener.onAnimationUpdate(objectValueAnimator)
        }
        assertEquals(0.0f, objectValueAnimator.animatedFraction)
        assertEquals(-200L, objectValueAnimator.animatedValue as Long)
        clearAllMocks()

        // WHEN Animation is played.
        ShadowLooper.idleMainLooper()

        // THEN 100ms to 200ms every milliseconds, animate time 0..100 inclusive is 100+1 calls.
        verify(exactly = 200 - 100 + 1) {
            mockUpdateListener.onAnimationUpdate(objectValueAnimator)
        }
        assertEquals(1.0f, objectValueAnimator.animatedFraction)
        assertEquals(200L, objectValueAnimator.animatedValue as Long)
    }
}
