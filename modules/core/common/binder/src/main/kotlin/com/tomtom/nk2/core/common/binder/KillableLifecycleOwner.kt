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
import androidx.annotation.MainThread
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

typealias MainScopeProvider = (() -> Unit) -> Unit

/**
 * LifecycleOwner which is destroyed when it catches a [DeadObjectException].
 *
 * Shadows the [parentLifecycleOwner]. Use [invokeDeathSafe] to catch the [DeadObjectException]
 * and handle it. Use [invokeDeathSafeThrow] to handle the [DeadObjectException] and rethrow it.
 *
 * @param mainScopeProvider Called to post the given action on the main thread.
 */
class KillableLifecycleOwner(
    private var parentLifecycleOwner: LifecycleOwner,
    private val mainScopeProvider: MainScopeProvider
) : LifecycleOwner {
    private var lifecycleRegistry = LifecycleRegistry(this)

    private val parentLifecycleObserver = object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        }

        override fun onStart(owner: LifecycleOwner) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        }

        override fun onResume(owner: LifecycleOwner) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }

        override fun onPause(owner: LifecycleOwner) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        }

        override fun onStop(owner: LifecycleOwner) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            destroy()
        }
    }

    init {
        mainScopeProvider {
            parentLifecycleOwner.lifecycle.addObserver(parentLifecycleObserver)
        }
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    /**
     * Invokes [action] and destroys the lifecycle owner when it throws a [DeadObjectException].
     *
     * The exception is swallowed, as such, this method should only be used when the call can be
     * ignored safely.
     */
    fun invokeDeathSafe(action: () -> Unit) {
        try {
            action()
        } catch (e: DeadObjectException) {
            mainScopeProvider { destroy() }
        }
    }

    /**
     * Invokes [action] and destroys the lifecycle owner when it throws a [DeadObjectException].
     *
     * The exception is rethrown. Use this method when a return value is required or when the call
     * cannot be ignored safely. The caller needs to handle the [DeadObjectException] too.
     */
    fun <R> invokeDeathSafeThrow(action: () -> R): R {
        try {
            return action()
        } catch (e: DeadObjectException) {
            mainScopeProvider { destroy() }
            throw e
        }
    }

    @MainThread
    private fun destroy() {
        parentLifecycleOwner.lifecycle.removeObserver(parentLifecycleObserver)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}
