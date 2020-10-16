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

package com.tomtom.nk2.tools.testing.unit

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.test.core.app.ApplicationProvider
import com.tomtom.nk2.tools.testing.assertion.getOrAwaitValue as assertionGetOrAwaitValue
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog
import org.robolectric.shadows.ShadowLooper

@RunWith(TomTomTestRunner::class)
abstract class TomTomTestCase : Assert() {

    @get:Rule
    val watcher: TestRule = LoggingTestWatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    protected val context: Context
        get() = ApplicationProvider.getApplicationContext()

    private val mainHandler by lazy { Handler(Looper.getMainLooper()) }

    @Before
    @Throws(Exception::class)
    fun prepareShadows() {
        // Redirect the logcat output to stdout.
        ShadowLog.stream = System.out
    }

    @After
    @Throws(Exception::class)
    fun cleanupMocks() {
        // Necessary to ensure that object mocks do not leak and cause issues in unrelated tests.
        unmockkAll()
    }

    /**
     * Runs an operation on the main looper. Execution and scheduling is affected by [ShadowLooper].
     */
    fun onMainLooper(operation: () -> Unit) {
        mainHandler.post(operation)
    }

    fun <T> LiveData<T>.getOrAwaitValue(time: Long = 2, timeUnit: TimeUnit = TimeUnit.SECONDS): T =
        assertionGetOrAwaitValue(time, timeUnit)

    /**
     * Helper function for mocking [MainScope] and use [TestCoroutineScope] as [CoroutineScope].
     */
    @ExperimentalCoroutinesApi
    fun mockMainScope() =
        TestCoroutineScope().also {
            mockkStatic("kotlinx.coroutines.CoroutineScopeKt")
            every { MainScope() } returns it
        }
}

private class LoggingTestWatcher : TestWatcher() {
    override fun starting(description: Description) {
        println("STARTING: " + description.methodName)
        super.starting(description)
    }

    override fun finished(description: Description) {
        println("FINISHED: " + description.methodName + "\n")
        super.finished(description)
    }
}
