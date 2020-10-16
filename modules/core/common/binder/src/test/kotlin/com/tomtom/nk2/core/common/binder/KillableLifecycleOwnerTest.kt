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

package com.tomtom.nk2.core.common.binder

import android.os.DeadObjectException
import androidx.lifecycle.Lifecycle
import com.tomtom.nk2.tools.testing.lifecycle.TestLifecycleOwner
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import kotlin.test.assertFailsWith
import org.junit.Test

class KillableLifecycleOwnerTest : TomTomTestCase() {

    private val testLifecycleOwner = TestLifecycleOwner()

    @Test
    fun `lifecycle observer is registered through mainScopeProvider`() {
        var mainScopeProviderCalled = false
        val sut = KillableLifecycleOwner(testLifecycleOwner) { action ->
            mainScopeProviderCalled = true
            action()
        }
        assertEquals(Lifecycle.State.STARTED, sut.lifecycle.currentState)
        assertTrue(mainScopeProviderCalled)
    }

    @Test
    fun `lifecycle is shadowed with initial started lifecycle`() {
        // GIVEN
        val sut = KillableLifecycleOwner(testLifecycleOwner) { action ->
            action()
        }
        assertEquals(Lifecycle.State.STARTED, sut.lifecycle.currentState)

        // WHEN
        testLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        // THEN
        assertEquals(Lifecycle.State.RESUMED, sut.lifecycle.currentState)

        // WHEN
        testLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)

        // THEN
        assertEquals(Lifecycle.State.STARTED, sut.lifecycle.currentState)

        // WHEN
        testLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)

        // THEN
        assertEquals(Lifecycle.State.CREATED, sut.lifecycle.currentState)

        // WHEN
        testLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)

        // THEN
        assertEquals(Lifecycle.State.DESTROYED, sut.lifecycle.currentState)
    }

    @Test
    fun `lifecycle is shadowed with initial initialized lifecycle`() {
        // GIVEN
        testLifecycleOwner.state = Lifecycle.State.INITIALIZED
        val sut = KillableLifecycleOwner(testLifecycleOwner) { action ->
            action()
        }
        assertEquals(Lifecycle.State.INITIALIZED, sut.lifecycle.currentState)

        // WHEN
        testLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        // THEN
        assertEquals(Lifecycle.State.CREATED, sut.lifecycle.currentState)

        // WHEN
        testLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        // THEN
        assertEquals(Lifecycle.State.RESUMED, sut.lifecycle.currentState)

        // WHEN
        testLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)

        // THEN
        assertEquals(Lifecycle.State.STARTED, sut.lifecycle.currentState)

        // WHEN
        testLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)

        // THEN
        assertEquals(Lifecycle.State.CREATED, sut.lifecycle.currentState)

        // WHEN
        testLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)

        // THEN
        assertEquals(Lifecycle.State.DESTROYED, sut.lifecycle.currentState)
    }
    @Test
    fun `when the lifecycle is destroyed the parent listener is removed`() {
        // GIVEN
        val sut = KillableLifecycleOwner(testLifecycleOwner) { action ->
            action()
        }
        assertEquals(Lifecycle.State.STARTED, sut.lifecycle.currentState)
        testLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        assertEquals(Lifecycle.State.DESTROYED, sut.lifecycle.currentState)

        // WHEN
        testLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        // THEN
        assertEquals(Lifecycle.State.DESTROYED, sut.lifecycle.currentState)
    }

    @Test
    fun `invokeDeathSafe invokes action without invoking mainScopeProvider`() {
        // Given
        var mainScopeProviderCalled: Boolean
        val sut = KillableLifecycleOwner(testLifecycleOwner) { action ->
            mainScopeProviderCalled = true
            action()
        }
        assertEquals(Lifecycle.State.STARTED, sut.lifecycle.currentState)
        mainScopeProviderCalled = false

        // WHEN
        var actionInvoked = false
        sut.invokeDeathSafe { actionInvoked = true }

        // THEN
        assertFalse(mainScopeProviderCalled)
        assertTrue(actionInvoked)
        assertEquals(Lifecycle.State.STARTED, sut.lifecycle.currentState)
    }

    @Test
    fun `invokeDeathSafe destroys lifecycle through mainScopeProvider`() {
        // Given
        var mainScopeProviderCalled = false
        val sut = KillableLifecycleOwner(testLifecycleOwner) { action ->
            mainScopeProviderCalled = true
            action()
        }
        assertEquals(Lifecycle.State.STARTED, sut.lifecycle.currentState)

        // WHEN
        sut.invokeDeathSafe { throw DeadObjectException() }

        // THEN
        assertTrue(mainScopeProviderCalled)
        assertEquals(Lifecycle.State.DESTROYED, sut.lifecycle.currentState)
    }

    @Test
    fun `invokeDeathSafeThrow invokes action without invoking mainScopeProvider`() {
        // Given
        var mainScopeProviderCalled: Boolean
        val sut = KillableLifecycleOwner(testLifecycleOwner) { action ->
            mainScopeProviderCalled = true
            action()
        }
        assertEquals(Lifecycle.State.STARTED, sut.lifecycle.currentState)
        mainScopeProviderCalled = false

        // WHEN
        var actionInvoked = false
        sut.invokeDeathSafeThrow { actionInvoked = true }

        // THEN
        assertFalse(mainScopeProviderCalled)
        assertTrue(actionInvoked)
        assertEquals(Lifecycle.State.STARTED, sut.lifecycle.currentState)
    }

    @Test
    fun `invokeDeathSafeThrow destroys lifecycle through mainScopeProvider`() {
        // Given
        var mainScopeProviderCalled = false
        val sut = KillableLifecycleOwner(testLifecycleOwner) { action ->
            mainScopeProviderCalled = true
            action()
        }
        assertEquals(Lifecycle.State.STARTED, sut.lifecycle.currentState)

        // WHEN
        assertFailsWith(DeadObjectException::class) {
            sut.invokeDeathSafeThrow { throw DeadObjectException() }
        }

        // THEN
        assertTrue(mainScopeProviderCalled)
        assertEquals(Lifecycle.State.DESTROYED, sut.lifecycle.currentState)
    }
}
