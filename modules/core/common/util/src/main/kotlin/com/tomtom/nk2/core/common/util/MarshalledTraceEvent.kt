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
import com.tomtom.kotlin.traceevents.TraceEvent

/**
 * A marshalled [ParcelableTraceEvent].
 */
class MarshalledTraceEvent private constructor(val byteArray: ByteArray) : Parcelable {

    constructor(parcelableTraceEvent: ParcelableTraceEvent) : this(parcelableTraceEvent.marshal())

    private constructor(parcel: Parcel) : this(parcel.createByteArray()!!)

    fun toParcelableTraceEvent(): ParcelableTraceEvent {
        val parcel = Parcel.obtain()
        try {
            parcel.unmarshall(byteArray, 0, byteArray.size)
            parcel.setDataPosition(0)
            return ParcelableTraceEvent.createFromParcel(parcel)
        } finally {
            parcel.recycle()
        }
    }

    override fun hashCode() = byteArray.contentHashCode()

    override fun equals(other: Any?) = if (other is MarshalledTraceEvent) {
        byteArray.contentEquals(other.byteArray)
    } else {
        false
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByteArray(byteArray)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MarshalledTraceEvent> {
        override fun createFromParcel(parcel: Parcel): MarshalledTraceEvent {
            return MarshalledTraceEvent(parcel)
        }

        override fun newArray(size: Int): Array<MarshalledTraceEvent?> {
            return arrayOfNulls(size)
        }

        private fun ParcelableTraceEvent.marshal(): ByteArray {
            val parcel = Parcel.obtain()
            try {
                writeToParcel(parcel, 0)
                return parcel.marshall()
            } finally {
                parcel.recycle()
            }
        }
    }
}

fun TraceEvent.toMarshalled() = MarshalledTraceEvent(toParcelable())
