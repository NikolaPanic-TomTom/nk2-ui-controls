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
import com.tomtom.nk2.tools.testing.mock.niceMockk
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import io.mockk.every
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Test

class InstantStringResolverTest : TomTomTestCase() {

    private fun testWith(time: Instant, formatStyle: FormatStyle, locale: Locale) {
        // GIVEN
        val mockContext = niceMockk<Context> {
            every { resources.configuration.locales.get(any()) } returns locale
        }

        val tested = InstantStringResolver(time, formatStyle)
        val formatter = DateTimeFormatter.ofLocalizedDateTime(formatStyle).withLocale(locale)

        // WHEN and THEN
        val expectedString = formatter.format(ZonedDateTime.ofInstant(time, ZoneId.systemDefault()))
        verifyGet(tested, expectedString, mockContext)
    }

    @Test
    fun `with time A style short locale a`() = testWith(
        TIME_A, FormatStyle.SHORT,
        LOCAL_A
    )

    @Test
    fun `with time A style medium locale a`() = testWith(
        TIME_A, FormatStyle.MEDIUM,
        LOCAL_A
    )

    @Test
    fun `with time A style long locale a`() = testWith(
        TIME_A, FormatStyle.LONG,
        LOCAL_A
    )

    @Test
    fun `with time A style full locale a`() = testWith(
        TIME_A, FormatStyle.FULL,
        LOCAL_A
    )

    @Test
    fun `with time B style short locale a`() = testWith(
        TIME_B, FormatStyle.SHORT,
        LOCAL_A
    )

    @Test
    fun `with time B style medium locale a`() = testWith(
        TIME_B, FormatStyle.MEDIUM,
        LOCAL_A
    )

    @Test
    fun `with time B style long locale a`() = testWith(
        TIME_B, FormatStyle.LONG,
        LOCAL_A
    )

    @Test
    fun `with time B style full locale a`() = testWith(
        TIME_B, FormatStyle.FULL,
        LOCAL_A
    )

    @Test
    fun `with time A style short locale b`() = testWith(
        TIME_A, FormatStyle.SHORT,
        LOCAL_B
    )

    @Test
    fun `with time A style medium locale b`() = testWith(
        TIME_A, FormatStyle.MEDIUM,
        LOCAL_B
    )

    @Test
    fun `with time A style long locale b`() = testWith(
        TIME_A, FormatStyle.LONG,
        LOCAL_B
    )

    @Test
    fun `with time A style full locale b`() = testWith(
        TIME_A, FormatStyle.FULL,
        LOCAL_B
    )

    @Test
    fun `with time B style short locale b`() = testWith(
        TIME_B, FormatStyle.SHORT,
        LOCAL_B
    )

    @Test
    fun `with time B style medium locale b`() = testWith(
        TIME_B, FormatStyle.MEDIUM,
        LOCAL_B
    )

    @Test
    fun `with time B style long locale b`() = testWith(
        TIME_B, FormatStyle.LONG,
        LOCAL_B
    )

    @Test
    fun `with time B style full locale b`() = testWith(
        TIME_B, FormatStyle.FULL,
        LOCAL_B
    )

    @Test
    fun `equals and hashCode`() {
        EqualsVerifier.forClass(InstantStringResolver::class.java).verify()
    }

    /**
     * Verifies the [InstantStringResolver.get] functions returns the [expectedValue].
     */
    private fun verifyGet(tested: InstantStringResolver, expectedValue: String, context: Context) {
        // WHEN
        val string = tested.get(context)

        // THEN
        assertEquals(expectedValue, string)
    }

    companion object {
        val TIME_A: Instant = Instant.ofEpochMilli(0L)
        val TIME_B: Instant = Instant.ofEpochMilli(1587563353956L)
        val LOCAL_A: Locale = Locale.ENGLISH
        val LOCAL_B: Locale = Locale.FRENCH
    }
}
