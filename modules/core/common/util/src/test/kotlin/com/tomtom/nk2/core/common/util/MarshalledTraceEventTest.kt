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
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Test

class MarshalledTraceEventTest : TomTomTestCase() {

    enum class TestEnum {
        TEST
    }

    private val traceEvent = TraceEvent(
        dateTime = LocalDateTime.MAX,
        logLevel = TraceLog.LogLevel.DEBUG,
        tracerClassName = TRACER_CLASS_NAME,
        taggingClassName = TAGGING_CLASS_NAME,
        interfaceName = INTERFACE_NAME,
        stackTraceHolder = null,
        eventName = EVENT_NAME,
        args = arrayOf(1, "2", null, TestEnum.TEST)
    )
    private val parcelableTraceEvent = ParcelableTraceEvent(traceEvent)

    @Test
    fun `equals and hashCode`() {
        EqualsVerifier.forClass(MarshalledTraceEvent::class.java).verify()
    }

    @Test
    fun `write and read ParcelableTraceEvent to and from Parcel`() {
        // GIVEN
        val parcel = Parcel.obtain()

        // WHEN
        val sut = MarshalledTraceEvent(parcelableTraceEvent)
        parcel.writeParcelable(sut, 0)

        // THEN
        parcel.setDataPosition(0)
        val createdMarshalledTraceEvent = parcel.readParcelable<MarshalledTraceEvent>(
            MarshalledTraceEventTest::class.java.classLoader
        )
        assertEquals(sut, createdMarshalledTraceEvent)

        // THEN
        val createdParcelableTraceEvent = createdMarshalledTraceEvent!!.toParcelableTraceEvent()
        assertEquals(parcelableTraceEvent, createdParcelableTraceEvent)
    }

    private companion object {
        const val TRACER_CLASS_NAME = "TracerClassName"
        const val TAGGING_CLASS_NAME = "TaggingClassName"
        const val INTERFACE_NAME = "InterfaceName"
        const val EVENT_NAME = "eventName"
    }
}
