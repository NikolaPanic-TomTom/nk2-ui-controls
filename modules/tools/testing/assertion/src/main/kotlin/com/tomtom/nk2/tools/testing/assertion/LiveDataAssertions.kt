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

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import org.junit.Assert

/**
 * Unobserved LiveData does not emit values. Use this function to force one observer.
 *
 * This function is due to be added to Jetpack. Until it is there, we can use this
 * implementation.
 *
 * Copyright 2019 Google LLC.
 * SPDX-License-Identifier: Apache-2.0
 */
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = java.util.concurrent.CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

fun assertLiveDataTrue(liveData: LiveData<Boolean>) =
    Assert.assertTrue(liveData.getOrAwaitValue())

fun assertLiveDataFalse(liveData: LiveData<Boolean>) =
    Assert.assertFalse(liveData.getOrAwaitValue())

fun <T> assertLiveDataEquals(expected: T, actualLiveData: LiveData<T>) =
    Assert.assertEquals(expected, actualLiveData.getOrAwaitValue())
