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
import java.time.Duration
import org.junit.Test

class DurationExtensionsTest : TomTomTestCase() {

    @Test
    fun `rounded duration`() {
        assertEquals(0, Duration.ZERO.roundToNearestSecond())
        assertEquals(0, Duration.ofMillis(499).roundToNearestSecond())
        assertEquals(1, Duration.ofMillis(500).roundToNearestSecond())
        assertEquals(1, Duration.ofMillis(1499).roundToNearestSecond())
        assertEquals(2, Duration.ofMillis(1500).roundToNearestSecond())

        assertEquals(0, Duration.ZERO.roundToNearestMinute())
        assertEquals(0, Duration.ofSeconds(29).roundToNearestMinute())
        assertEquals(1, Duration.ofSeconds(30).roundToNearestMinute())
        assertEquals(1, Duration.ofSeconds(89).roundToNearestMinute())
        assertEquals(2, Duration.ofSeconds(90).roundToNearestMinute())

        assertEquals(0, Duration.ZERO.roundToNearestHour())
        assertEquals(0, Duration.ofMinutes(29).roundToNearestHour())
        assertEquals(1, Duration.ofMinutes(30).roundToNearestHour())
        assertEquals(1, Duration.ofMinutes(89).roundToNearestHour())
        assertEquals(2, Duration.ofMinutes(90).roundToNearestHour())

        assertEquals(0, Duration.ZERO.roundToNearestHalfDay())
        assertEquals(0, Duration.ofHours(5).roundToNearestHalfDay())
        assertEquals(1, Duration.ofHours(6).roundToNearestHalfDay())
        assertEquals(1, Duration.ofHours(17).roundToNearestHalfDay())
        assertEquals(2, Duration.ofHours(18).roundToNearestHalfDay())
    }
}
