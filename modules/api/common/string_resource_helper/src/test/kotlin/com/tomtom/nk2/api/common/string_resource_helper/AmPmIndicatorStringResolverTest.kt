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
import java.util.Locale
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Before
import org.junit.Test

class AmPmIndicatorStringResolverTest : TomTomTestCase() {
    private var is24Hours = false
    private var locale = Locale.ENGLISH
    private var mockContext = niceMockk<Context> {
        every { getString(R.string.ttivi_time_am_indicator) } returns "am"
        every { getString(R.string.ttivi_time_pm_indicator) } returns "pm"
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
    fun `am-pm locale with afternoon time test`() {
        // GIVEN
        locale = Locale.ENGLISH
        is24Hours = false

        val tested = AmPmIndicatorStringResolver(TIME_3_AFTERNOON)

        // WHEN and THEN
        assertEquals(PM_INDICATOR, tested.get(mockContext))
    }

    @Test
    fun `am-pm locale with morning time test`() {
        // GIVEN
        locale = Locale.ENGLISH
        is24Hours = false

        val tested = AmPmIndicatorStringResolver(TIME_3_MORNING)

        // WHEN and THEN
        assertEquals(AM_INDICATOR, tested.get(mockContext))
    }

    @Test
    fun `24 hours locale with morning time test`() {
        // GIVEN
        locale = Locale.FRENCH
        is24Hours = true

        val tested = AmPmIndicatorStringResolver(TIME_3_MORNING)

        // WHEN and THEN
        assertEquals("", tested.get(mockContext))
    }

    @Test
    fun `24 hours locale with afternoon time test`() {
        // GIVEN
        locale = Locale.FRENCH
        is24Hours = true

        val tested = AmPmIndicatorStringResolver(TIME_3_AFTERNOON)

        // WHEN and THEN
        assertEquals("", tested.get(mockContext))
    }

    @Test
    fun `equals and hashCode`() {
        EqualsVerifier.forClass(AmPmIndicatorStringResolver::class.java).verify()
    }

    companion object {
        val TIME_3_AFTERNOON: Instant = Instant.ofEpochMilli(1590411600000)
        val TIME_3_MORNING: Instant = Instant.ofEpochMilli(1590368400000)
        const val AM_INDICATOR = "am"
        const val PM_INDICATOR = "pm"
    }
}
