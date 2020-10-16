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

package com.tomtom.nk2.api.common.string_resource_helper

import android.content.Context
import android.os.Parcel
import android.os.Parcelable

/**
 * A [StringResolver] that can represent any object based on its [toString] implementation.
 */
// The `@Parcelize` annotation cannot be used here due to the type of [value].
data class StaticStringResolver(private val value: Any) : StringResolver {
    private constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun get(context: Context) = value.toString()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(value.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StaticStringResolver> {
        override fun createFromParcel(parcel: Parcel): StaticStringResolver {
            return StaticStringResolver(parcel)
        }

        override fun newArray(size: Int): Array<StaticStringResolver?> {
            return arrayOfNulls(size)
        }
    }
}
