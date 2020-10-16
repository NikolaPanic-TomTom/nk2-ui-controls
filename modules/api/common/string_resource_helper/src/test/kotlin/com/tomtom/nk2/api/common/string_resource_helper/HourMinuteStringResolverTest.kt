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
import android.text.format.DateFormat
import com.tomtom.nk2.tools.testing.mock.niceMockk
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import io.mockk.every
import io.mockk.mockkStatic
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Before
import org.junit.Test

class HourMinuteStringResolverTest : TomTomTestCase() {
    private var is24Hours = false
    private var locale = Locale.ENGLISH
    private var mockContext = niceMockk<Context> {
        every { resources.configuration.locales.get(any()) } answers { locale }
    }

    @Before
    fun setStaticMocks() {
        mockkStatic("android.text.format.DateFormat")
        every {
            DateFormat.is24HourFormat(any())
        } answers { is24Hours }
    }

    @Test
    fun `locale with am-pm time conversion test`() {
        // GIVEN
        locale = Locale.ENGLISH
        is24Hours = false
        val formatter = DateTimeFormatter.ofPattern("h:mm", Locale.FRENCH)

        val tested = HourMinuteStringResolver(TIME_3_AFTERNOON)
        val expectedString =
            formatter.format(ZonedDateTime.ofInstant(TIME_3_AFTERNOON, ZoneId.systemDefault()))

        // WHEN and THEN
        assertEquals(expectedString, tested.get(mockContext))
    }

    @Test
    fun `locale with 24 hours time conversion test`() {
        // GIVEN
        locale = Locale.FRENCH
        is24Hours = true
        val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.FRENCH)

        val tested = HourMinuteStringResolver(TIME_3_AFTERNOON)
        val expectedString =
            formatter.format(ZonedDateTime.ofInstant(TIME_3_AFTERNOON, ZoneId.systemDefault()))

        // WHEN and THEN
        assertEquals(expectedString, tested.get(mockContext))
    }

    @Test
    fun `equals and hashCode`() {
        EqualsVerifier.forClass(HourMinuteStringResolver::class.java).verify()
    }

    companion object {
        val TIME_3_AFTERNOON: Instant = Instant.ofEpochMilli(1590411600000)
    }
}
