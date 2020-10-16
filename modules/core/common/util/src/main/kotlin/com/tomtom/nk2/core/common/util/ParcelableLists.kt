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

data class ParcelableIntList(val value: List<Int>) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createListOptimized { p -> p.readInt() }!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) =
        parcel.writeCollectionOptimized(value) { p, e -> p.writeInt(e) }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<ParcelableIntList> {
        override fun createFromParcel(parcel: Parcel) = ParcelableIntList(parcel)
        override fun newArray(size: Int) = arrayOfNulls<ParcelableIntList?>(size)
    }
}

data class ParcelableFloatList(val value: List<Float>) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createListOptimized { p -> p.readFloat() }!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) =
        parcel.writeCollectionOptimized(value) { p, e -> p.writeFloat(e) }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<ParcelableFloatList> {
        override fun createFromParcel(parcel: Parcel) = ParcelableFloatList(parcel)
        override fun newArray(size: Int) = arrayOfNulls<ParcelableFloatList?>(size)
    }
}

data class ParcelableStringList(val value: List<String>) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createListOptimized { p -> p.readString() ?: "" }!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) =
        parcel.writeCollectionOptimized(value) { p, e -> p.writeString(e) }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<ParcelableStringList> {
        override fun createFromParcel(parcel: Parcel) = ParcelableStringList(parcel)
        override fun newArray(size: Int) = arrayOfNulls<ParcelableStringList?>(size)
    }
}
