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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged

/**
 * An implementation of [MutableLiveData] that only emits updates when the value actually changed,
 * i.e. when `newValue != oldValue`. This differs from regular [MutableLiveData] because if you set
 * its value to an equal same value, it still emits an update, causing observers to get a callback.
 *
 * This is more suitable than the [distinctUntilChanged] transformation for the cases where the
 * values are not necessarily going to be observed: [distinctUntilChanged] only changes value when
 * observers are registered.
 *
 * **NB:** This should only be used in situations where it's guaranteed to not lead to side-effects.
 * The most common situation where this should not be used is for instances of `T` that are both
 * mutable and have a custom [Object.equals] implementation. This could lead to situations where
 * a set value is ignored but mutated afterwards, meaning the value shouldn't have been ignored
 * after all. As a guideline, only use [DistinctMutableLiveData] for types that could also be used
 * as a key in a [Map].
 */
class DistinctMutableLiveData<T> : MutableLiveData<T> {

    constructor() : super()

    constructor(value: T) : super(value)

    override fun setValue(newValue: T?) {
        if ((value == null && newValue != null) ||
            value?.equals(newValue) == false) {
            super.setValue(newValue)
        }
    }
}
