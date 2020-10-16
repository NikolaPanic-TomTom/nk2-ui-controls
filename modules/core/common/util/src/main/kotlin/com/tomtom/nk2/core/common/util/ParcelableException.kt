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

package com.tomtom.nk2.core.common.util

import android.os.Parcel
import android.os.Parcelable
import kotlin.reflect.KClass
import kotlinx.android.parcel.Parcelize

/**
 * A parcelable variant of an [Exception].
 *
 * [Parcel.readException] throws an exception when an exception is written to the parcel by using
 * [Parcel.writeException]. When throwing the exception is undesired when reading the exception
 * from a parcel, this type can be used.
 */
@Suppress("MemberVisibilityCanBePrivate", "DataClassPrivateConstructor")
@Parcelize
data class ParcelableException private constructor(
    val exceptionType: String?,
    val exceptionMessage: String?
) : Parcelable {

    /**
     * Constructs a [ParcelableException] from an [Exception] instance.
     */
    constructor(exception: Exception) : this(exception::class.qualifiedName, exception.message)

    /**
     * Constructs a [ParcelableException] from an [exceptionType] class type and an optional
     * [exceptionMessage]. Useful for testing purposes.
     */
    constructor(
        exceptionType: KClass<*>,
        exceptionMessage: String?
    ) : this(exceptionType.qualifiedName, exceptionMessage)

    override fun toString() = "$exceptionType: $exceptionMessage"
}

fun Exception.toParcelable() = ParcelableException(this)
