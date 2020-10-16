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

package com.tomtom.nk2.tools.testing.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

/**
 * This is useful for when writing a test that needs a LifecycleOwner but none is available.
 * The lifecycle state is [Lifecycle.State.STARTED] by default. The state can be changed by tests.
 */
class TestLifecycleOwner : LifecycleOwner {
    private var lifecycleRegistry = LifecycleRegistry(this).apply {
        // Use deprecated API for compatibility with Robolectric. Robolectric uses API 27 if test
        // runs from Android Studio. `markState` is deprecated for API 29.
        // TODO(IVI-1812): Do not use deprecated API.
        @Suppress("DEPRECATION")
        markState(Lifecycle.State.STARTED)
    }

    var state: Lifecycle.State
        get() = lifecycleRegistry.currentState
        set(value) {
            lifecycleRegistry.currentState = value
        }

    fun handleLifecycleEvent(event: Lifecycle.Event) {
        lifecycleRegistry.handleLifecycleEvent(event)
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}
