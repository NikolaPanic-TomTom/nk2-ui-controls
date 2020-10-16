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

import android.os.Parcelable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.android.parcel.Parcelize

/**
 * Represents a registration token.
 *
 * See [RegistrationTokenMap.getNewToken] for creating a new instance.
 */
@Parcelize
data class RegistrationToken internal constructor(val token: Int) : Parcelable

/**
 * Utility class to generate unique tokens and map a value to this token.
 *
 * This class is thread-safe.
 *
 * It is assumed tokens can be reused after a [Int.MAX_VALUE] rollover.
 */
class RegistrationTokenMap<T> {
    private val registrations = ConcurrentHashMap<RegistrationToken, T>()

    private var lastToken = AtomicInteger(0)

    /**
     * Returns a new token.
     */
    fun getNewToken() = RegistrationToken(lastToken.addAndGet(1))

    /**
     * Maps the given [value] to the given [token]
     */
    fun setTokenEntry(token: RegistrationToken, value: T) {
        registrations[token] = value
    }

    /**
     * Maps the given [value] to a new token and returns the new token.
     */
    fun addNewEntry(value: T): RegistrationToken {
        val token = getNewToken()
        setTokenEntry(token, value)
        return token
    }

    /**
     * Removes a token entry.
     */
    fun removeTokenEntry(token: RegistrationToken) {
        registrations.remove(token)
    }

    /**
     * Returns the entry mapped for the given [token]. Null when the token is not mapped.
     */
    fun getTokenEntry(token: RegistrationToken) = registrations[token]

    /**
     * Applies [block] on the mapped entry for the given [token] and removes the entry afterwards.
     *
     * [block] is not invoked when the token is not mapped.
     */
    fun applyOnTokenEntryAndRemove(token: RegistrationToken, block: (T) -> Unit) {
        getTokenEntry(token)?.let {
            block(it)
            removeTokenEntry(token)
        }
    }
}
