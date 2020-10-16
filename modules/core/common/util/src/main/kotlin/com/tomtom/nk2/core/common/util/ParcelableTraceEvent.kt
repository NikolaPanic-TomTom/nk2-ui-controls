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
import com.tomtom.kotlin.traceevents.TraceLog
import java.time.LocalDateTime

/**
 * Parcelable [TraceEvent].
 *
 * When an [TraceEvent.args] element is not supported by [Parcel.writeValue] the element's
 * `toString()` value is written to the parcel instead.
 *
 * The [TraceEvent.stackTraceHolder] is never written to a parcel.
 */
data class ParcelableTraceEvent(
    val traceEvent: TraceEvent,
    val allArgumentsSupported: Boolean
) : Parcelable {

    constructor(traceEvent: TraceEvent) : this(traceEvent, true)

    constructor(parcel: Parcel) : this(
        TraceEvent(
            dateTime = parcel.readSerializable() as LocalDateTime,
            logLevel = TraceLog.LogLevel.values()[parcel.readInt()],
            tracerClassName = parcel.readString()!!,
            taggingClassName = parcel.readString()!!,
            interfaceName = parcel.readString()!!,
            stackTraceHolder = null,
            eventName = parcel.readString()!!,
            args = parcel.readArray(ParcelableTraceEvent::class.java.classLoader)!!.map {
                when (it) {
                    is ParcelableEnum -> {
                        it.toEnum(ParcelableTraceEvent::class.java.classLoader!!)
                    }
                    else -> {
                        it
                    }
                }
            }.toTypedArray()
        ),
        /** TODO(IVI-674): Replace with [Parcel.readBoolean] once min SDK >= 29. */
        parcel.readInt() != 0
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(traceEvent.dateTime)
        parcel.writeInt(traceEvent.logLevel.ordinal)
        parcel.writeString(traceEvent.tracerClassName)
        parcel.writeString(traceEvent.taggingClassName)
        parcel.writeString(traceEvent.interfaceName)
        parcel.writeString(traceEvent.eventName)
        val allArgumentSupported = parcel.writeUnsafeArray(traceEvent.args.map {
            when (it) {
                is Enum<*> -> {
                    ParcelableEnum(it)
                }
                else -> {
                    it
                }
            }
        }.toTypedArray())
        /** TODO(IVI-674): Replace with [Parcel.writeBoolean] once min SDK >= 29. */
        parcel.writeInt(if (allArgumentSupported) 1 else 0)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<ParcelableTraceEvent> {
        override fun createFromParcel(parcel: Parcel): ParcelableTraceEvent {
            return ParcelableTraceEvent(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableTraceEvent?> {
            return arrayOfNulls(size)
        }
    }
}

fun TraceEvent.toParcelable() = ParcelableTraceEvent(this)
