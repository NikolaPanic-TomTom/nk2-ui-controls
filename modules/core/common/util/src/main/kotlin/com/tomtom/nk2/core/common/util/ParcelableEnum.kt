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

import android.os.Parcelable
import kotlin.reflect.jvm.jvmName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParcelableEnum constructor(
    private val className: String,
    private val ordinal: Int
) : Parcelable {

    constructor(enumValue: Enum<*>) : this(enumValue::class.jvmName, enumValue.ordinal)

    fun toEnum(classLoader: ClassLoader): Enum<*> {
        val classType = classLoader.loadClass(className)
        require(classType.isEnum)
        val valueMethod = classType.getDeclaredMethod("values")
        val values = valueMethod.invoke(classType) as Array<*>
        return values[ordinal] as Enum<*>
    }
}
