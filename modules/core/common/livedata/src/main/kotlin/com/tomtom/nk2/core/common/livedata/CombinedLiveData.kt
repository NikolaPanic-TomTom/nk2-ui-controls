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

package com.tomtom.nk2.core.common.livedata

import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Similar to [MediatorLiveData], but with a few key differences:
 * - There's a single function to update the value when any of the sources change.
 * - Updating multiple sources simultaneously only results in a single value update.
 *
 * @param I The type of the input. I.e., the source [LiveData] values.
 * @param O The type of the output [LiveData] value.
 *
 * @param calculateValue A method that calculates the value for this [LiveData] after one or more
 *     sources are updated.
 */
open class CombinedLiveData<I, O>(val calculateValue: (List<I?>) -> O) :
    MutableLiveData<O>() {

    private val sources = mutableListOf<LiveData<out I>>()
    private val onChangedObserver = Observer<I> { onChanged() }

    private var ignoreChanges = false
    private var changeIgnored = false

    init {
        onChanged()
    }

    private fun onChanged() {
        if (ignoreChanges) {
            changeIgnored = true
            return
        }

        value = calculateValue()
        changeIgnored = false
    }

    override fun getValue() =
        if (hasActiveObservers()) super.getValue() else calculateValue()

    private fun calculateValue() = calculateValue(sources.map { it.value })

    /**
     * Adds a source, triggering a value update. Any future change to the added source's value
     * will also trigger a value update.
     *
     * Duplicate sources are not allowed.
     */
    @MainThread
    fun addSource(source: LiveData<out I>) {
        require(!sources.contains(source)) { "Source already added, source=$source" }

        sources.add(source)

        if (hasActiveObservers()) {
            source.observeForever(onChangedObserver)
        }
    }

    /**
     * Removes a source, triggering a value update. Any future change to the added source's value
     * will no longer trigger a value update.
     *
     * Removing a source that was not added will be silently ignored.
     */
    @MainThread
    fun removeSource(source: LiveData<out I>) {
        val sourceWasPresent = sources.remove(source)
        if (sourceWasPresent) {
            source.removeObserver(onChangedObserver)

            // Removing an observer doesn't trigger a callback, so trigger it manually.
            onChanged()
        }
    }

    /**
     * Replaces all sources, triggering a value update. Any future change to any of the new sources'
     * values will also trigger a value update.
     *
     * Duplicate sources are not allowed.
     */
    @MainThread
    fun setSources(newSources: List<LiveData<out I>>) {
        require(newSources.size == newSources.distinct().size) {
            "Sources may not contain duplicates"
        }

        withSingleUpdate {
            (sources - newSources).forEach { removeSource(it) }
            (newSources - sources).forEach { addSource(it) }
        }
    }

    @CallSuper
    override fun onActive() {
        withSingleUpdate {
            sources.forEach { it.observeForever(onChangedObserver) }
        }
    }

    @CallSuper
    override fun onInactive() {
        sources.forEach { it.removeObserver(onChangedObserver) }
    }

    /**
     * Emits a single update after performing an operation. Useful for updating multiple sources.
     */
    private fun withSingleUpdate(operation: () -> Unit) {
        ignoreChanges = true
        operation()
        ignoreChanges = false

        if (changeIgnored) {
            onChanged()
        }
    }
}
