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

package com.tomtom.nk2.tools.testing.assertion

import java.time.LocalTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue

private const val ASSERT_TIMEOUT_MS = 10000L
private const val POLLING_INTERVAL_MS = 25L

fun assertTrueWithTimeout(predicate: () -> Boolean) {
    assertTrue(runBlocking { becomesTrueWithinMillis(predicate, ASSERT_TIMEOUT_MS) })
}

internal suspend fun becomesTrueWithinMillis(predicate: () -> Boolean, millis: Long): Boolean {
    val startTime = LocalTime.now()
    val endTime = startTime.plusNanos(millis * 1000_000L)

    do {
        if (predicate()) {
            return true
        }

        delay(POLLING_INTERVAL_MS)
    } while (LocalTime.now().isBefore(endTime))

    return false
}
