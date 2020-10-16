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
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class VerticalMoveAnimationTest : TomTomTestCase() {

    private val mockView = mockk<View>()
    private var mockEndAnimation: ((Boolean) -> Unit)? = null
    private var mockTransformation = Transformation()
    private val mockAnimationListener = mockk<Animation.AnimationListener> {
        every { onAnimationStart(any()) } answers {}
        every { onAnimationEnd(any()) } answers {}
    }

    @Before
    fun setUpVerticalMovementSpringAnimationMock() {
        mockkConstructor(VerticalMovementSpringAnimation::class)
        every {
            anyConstructed<VerticalMovementSpringAnimation>()
                .animateToPosition(any(), any(), any(), any())
        } answers {
            mockEndAnimation = arg(3)
        }
    }

    @Test
    fun `animation listener start and end callbacks should be only called once`() {
        // GIVEN
        val sut = VerticalMoveAnimation(mockView, 0, true)
        sut.startTime = 0
        sut.duration = 1000
        sut.setAnimationListener(mockAnimationListener)

        // WHEN
        sut.getTransformation(100, mockTransformation)
        sut.getTransformation(200, mockTransformation)
        sut.getTransformation(1000, mockTransformation)

        // THEN
        assertNotNull(mockEndAnimation)
        verify(exactly = 1) { mockAnimationListener.onAnimationStart(any()) }
        verify(exactly = 0) { mockAnimationListener.onAnimationEnd(any()) }

        // WHEN
        mockEndAnimation!!.invoke(true)

        // THEN
        verify(exactly = 1) { mockAnimationListener.onAnimationStart(any()) }
        verify(exactly = 1) { mockAnimationListener.onAnimationEnd(any()) }

        // WHEN
        sut.getTransformation(1100, mockTransformation)

        // THEN
        verify(exactly = 1) { mockAnimationListener.onAnimationStart(any()) }
        verify(exactly = 1) { mockAnimationListener.onAnimationEnd(any()) }
    }
}
