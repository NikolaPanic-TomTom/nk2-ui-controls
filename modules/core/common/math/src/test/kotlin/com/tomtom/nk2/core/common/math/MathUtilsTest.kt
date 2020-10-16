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

package com.tomtom.nk2.core.common.math

import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import org.junit.Test

class MathUtilsTest : TomTomTestCase() {

    @Test
    fun `roundToNearest integer test`() {
        assertEquals(0, roundToNearest(0, 0))

        assertEquals(1, roundToNearest(1, 1))
        assertEquals(3, roundToNearest(3, 1))
        assertEquals(10000, roundToNearest(10000, 1))

        assertEquals(0, roundToNearest(0, 5))
        assertEquals(0, roundToNearest(2, 5))
        assertEquals(5, roundToNearest(5, 5))
        assertEquals(10, roundToNearest(9, 5))
        assertEquals(10, roundToNearest(10, 5))
        assertEquals(95, roundToNearest(93, 5))
        assertEquals(90005, roundToNearest(90003, 5))

        assertEquals(0, roundToNearest(3, 12))
        assertEquals(12, roundToNearest(9, 12))
        assertEquals(36, roundToNearest(33, 12))
    }

    @Test
    fun `roundToNearest double test`() {
        assertEqualsDouble(0.0, roundToNearest(0.0, 0.0))

        assertEqualsDouble(6.0, roundToNearest(6.5, 1.5))
        assertEqualsDouble(100.0, roundToNearest(99.95, 0.1))
        assertEqualsDouble(100.6, roundToNearest(100.64, 0.1))

        assertEqualsDouble(1.0, roundToNearest(1.0, 1.0))
        assertEqualsDouble(3.0, roundToNearest(3.0, 1.0))
        assertEqualsDouble(10000.0, roundToNearest(10000.0, 1.0))

        assertEqualsDouble(0.0, roundToNearest(0.0, 5.0))
        assertEqualsDouble(0.0, roundToNearest(2.0, 5.0))
        assertEqualsDouble(5.0, roundToNearest(5.0, 5.0))
        assertEqualsDouble(10.0, roundToNearest(9.0, 5.0))
        assertEqualsDouble(10.0, roundToNearest(10.0, 5.0))
        assertEqualsDouble(95.0, roundToNearest(93.0, 5.0))
        assertEqualsDouble(90005.0, roundToNearest(90003.0, 5.0))

        assertEqualsDouble(0.0, roundToNearest(3.0, 12.0))
        assertEqualsDouble(12.0, roundToNearest(9.0, 12.0))
        assertEqualsDouble(36.0, roundToNearest(33.0, 12.0))

        assertEqualsDouble(100.0, roundToNearest(99.995, 0.01))
    }

    companion object {
        private const val EPS = 0.000001
        private fun assertEqualsDouble(expected: Double, value: Double) {
            assertEquals(expected, value, EPS)
        }
    }
}
