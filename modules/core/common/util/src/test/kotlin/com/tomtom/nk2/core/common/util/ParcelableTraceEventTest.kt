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
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import com.tomtom.kotlin.traceevents.TraceEvent
import com.tomtom.kotlin.traceevents.TraceLog
import java.time.LocalDateTime
import org.junit.Test

class ParcelableTraceEventTest : TomTomTestCase() {

    enum class TestEnum {
        TEST
    }

    @Test
    fun `write and create with all arguments supported`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val traceEvent = TraceEvent(
            dateTime = LocalDateTime.MAX,
            logLevel = TraceLog.LogLevel.DEBUG,
            tracerClassName = TRACER_CLASS_NAME,
            taggingClassName = TAGGING_CLASS_NAME,
            interfaceName = INTERFACE_NAME,
            stackTraceHolder = null,
            eventName = EVENT_NAME,
            args = arrayOf(1, "2", null, TestEnum.TEST)
        )
        val parcelableTraceEvent = ParcelableTraceEvent(traceEvent)

        // WHEN
        parcelableTraceEvent.writeToParcel(parcel, 0)

        parcel.setDataPosition(0)
        val createdParcelableTraceEvent = ParcelableTraceEvent.createFromParcel(parcel)
        val createdTraceEvent = createdParcelableTraceEvent.traceEvent

        // THEN
        assertEquals(traceEvent, createdTraceEvent)
        assertTrue(createdParcelableTraceEvent.allArgumentsSupported)
    }

    @Test
    fun `write and create with all arguments supported and stackTraceHolder`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val traceEvent = TraceEvent(
            dateTime = LocalDateTime.MAX,
            logLevel = TraceLog.LogLevel.DEBUG,
            tracerClassName = TRACER_CLASS_NAME,
            taggingClassName = TAGGING_CLASS_NAME,
            interfaceName = INTERFACE_NAME,
            stackTraceHolder = Throwable(),
            eventName = EVENT_NAME,
            args = arrayOf(1, "2", null, TestEnum.TEST)
        )
        val parcelableTraceEvent = ParcelableTraceEvent(traceEvent)

        // WHEN
        parcelableTraceEvent.writeToParcel(parcel, 0)

        parcel.setDataPosition(0)
        val createdParcelableTraceEvent = ParcelableTraceEvent.createFromParcel(parcel)
        val createdTraceEvent = createdParcelableTraceEvent.traceEvent

        // THEN
        assertNotEquals(traceEvent, createdTraceEvent)
        assertEquals(traceEvent.dateTime, createdTraceEvent.dateTime)
        assertEquals(traceEvent.logLevel, createdTraceEvent.logLevel)
        assertEquals(traceEvent.tracerClassName, createdTraceEvent.tracerClassName)
        assertEquals(traceEvent.taggingClassName, createdTraceEvent.taggingClassName)
        assertEquals(traceEvent.interfaceName, createdTraceEvent.interfaceName)
        assertNull(createdTraceEvent.stackTraceHolder)
        assertEquals(traceEvent.eventName, createdTraceEvent.eventName)
        assertArrayEquals(traceEvent.args, createdTraceEvent.args)
        assertTrue(createdParcelableTraceEvent.allArgumentsSupported)
    }

    @Test
    fun `write and create without all arguments supported`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val traceEvent = TraceEvent(
            dateTime = LocalDateTime.MAX,
            logLevel = TraceLog.LogLevel.DEBUG,
            tracerClassName = TRACER_CLASS_NAME,
            taggingClassName = TAGGING_CLASS_NAME,
            interfaceName = INTERFACE_NAME,
            stackTraceHolder = null,
            eventName = EVENT_NAME,
            args = arrayOf(
                1, "2", null, TestEnum.TEST,
                NotParcelableObject(42) // Not supported.
            )
        )
        val parcelableTraceEvent = ParcelableTraceEvent(traceEvent)

        // WHEN
        parcelableTraceEvent.writeToParcel(parcel, 0)

        parcel.setDataPosition(0)
        val createdParcelableTraceEvent = ParcelableTraceEvent.createFromParcel(parcel)
        val createdTraceEvent = createdParcelableTraceEvent.traceEvent

        // THEN expect toString() is called for last element in traceEvent.args
        assertNotEquals(traceEvent, createdTraceEvent)
        assertEquals(traceEvent.dateTime, createdTraceEvent.dateTime)
        assertEquals(traceEvent.logLevel, createdTraceEvent.logLevel)
        assertEquals(traceEvent.tracerClassName, createdTraceEvent.tracerClassName)
        assertEquals(traceEvent.taggingClassName, createdTraceEvent.taggingClassName)
        assertEquals(traceEvent.interfaceName, createdTraceEvent.interfaceName)
        assertEquals(traceEvent.eventName, createdTraceEvent.eventName)
        assertArrayEquals(arrayOf(1, "2", null, TestEnum.TEST, "42"), createdTraceEvent.args)
        assertFalse(createdParcelableTraceEvent.allArgumentsSupported)
    }

    private companion object {
        const val TRACER_CLASS_NAME = "TracerClassName"
        const val TAGGING_CLASS_NAME = "TaggingClassName"
        const val INTERFACE_NAME = "InterfaceName"
        const val EVENT_NAME = "eventName"
    }
}
