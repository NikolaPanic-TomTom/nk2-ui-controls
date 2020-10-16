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

import com.tomtom.nk2.tools.testing.mock.niceMockk
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Test

class CompositeReleasableTest : TomTomTestCase() {

    private val mockReleasable1 = niceMockk<Releasable>()
    private val mockReleasable2 = niceMockk<Releasable>()

    private val sut = CompositeReleasable()

    @Test
    fun `release all contents`() {
        // GIVEN
        sut += mockReleasable1
        sut += mockReleasable2

        // WHEN
        sut.release()

        // THEN
        verifySequence {
            mockReleasable2.release()
            mockReleasable1.release()
        }
    }

    @Test
    fun `releasing twice`() {
        // GIVEN
        sut += mockReleasable1
        sut += mockReleasable2

        // WHEN
        sut.release()
        sut.release()

        // THEN
        verifySequence {
            mockReleasable2.release()
            mockReleasable1.release()
        }
    }

    @Test
    fun `remove releasable`() {
        // GIVEN
        sut += mockReleasable1
        sut += mockReleasable2

        // WHEN
        sut -= mockReleasable2
        sut.release()

        // THEN
        verify(exactly = 1) { mockReleasable1.release() }
        verify(exactly = 0) { mockReleasable2.release() }
    }

    @Test
    fun isEmpty() {
        // GIVEN no releasables are added

        // THEN
        assertTrue(sut.isEmpty())
        assertFalse(sut.isNotEmpty())

        // GIVEN
        sut += mockReleasable1
        sut += mockReleasable2

        // THEN
        assertFalse(sut.isEmpty())
        assertTrue(sut.isNotEmpty())

        // WHEN
        sut -= mockReleasable2

        // THEN
        assertFalse(sut.isEmpty())
        assertTrue(sut.isNotEmpty())

        // WHEN
        sut -= mockReleasable1

        // THEN
        assertTrue(sut.isEmpty())
        assertFalse(sut.isNotEmpty())
    }
}
