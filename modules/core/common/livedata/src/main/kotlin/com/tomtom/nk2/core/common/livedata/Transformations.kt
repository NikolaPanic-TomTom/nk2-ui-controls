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

import androidx.lifecycle.LiveData


/**
 * Transforms multiple [LiveData]s containing any type of value using the given [combineFunction].
 * Whenever any of the sources are updated, the returned [LiveData] value is also updated.
 *
 * For example:
 * ```
 * // GIVEN
 * val source1 = MutableLiveData<Int?>(1)
 * val source2 = MutableLiveData<Int>(2)
 *
 * // WHEN
 * val combined = combine(source1, source2) { source1.value ?: source2.value }
 *
 * // THEN
 * // Observing combined.value yields 1
 *
 * // WHEN
 * source1.value = null
 *
 * // THEN
 * // Observing combined.value yields 2
 * ```
 *
 * @param S The type of the source [LiveData] values.
 * @param T The type of the returned [LiveData] value.
 *
 * @param combineFunction A function that maps source values to [T].
 */
fun <S, T> combine(vararg sources: LiveData<out S>, combineFunction: (List<S?>) -> T):
    LiveData<T> =
    CombinedLiveData(combineFunction).apply {
        setSources(sources.toList())
    }
