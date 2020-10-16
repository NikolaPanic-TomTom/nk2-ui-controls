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
import java.util.Locale
import javax.measure.unit.NonSI.FOOT
import javax.measure.unit.NonSI.MILE
import javax.measure.unit.NonSI.YARD
import javax.measure.unit.SI.METER
import nl.jqno.equalsverifier.EqualsVerifier
import org.jscience.physics.amount.Amount
import org.junit.Test

class DistanceStringResolverTest : TomTomTestCase() {
    private fun testWith(expectedResult: String, meters: Int, locale: Locale) {
        // GIVEN
        val mockContext = niceMockk<Context> {
            every { resources.configuration.locales.get(any()) } answers { locale }
        }
        val tested = DistanceStringResolver(meters)

        // WHEN and THEN
        assertEquals(expectedResult, tested.get(mockContext))
    }

    @Test
    fun `don't display 0~ to 10 meters test`() {
        testWith("", 0, Locale.GERMAN)
        testWith("", 5, Locale.GERMAN)
        testWith("", 9, Locale.GERMAN)
    }

    @Test
    fun `10 meter~ to 500 meter and 10m steps test`() {
        testWith("10 m", 10, Locale.GERMAN)
        testWith("90 m", 85, Locale.GERMAN)
        testWith("100 m", 95, Locale.GERMAN)
        testWith("100 m", 100, Locale.GERMAN)
        testWith("490 m", 490, Locale.GERMAN)
    }

    @Test
    fun `500 meter~ to 1 kilometer and 100m steps test`() {
        testWith("500 m", 495, Locale.GERMAN)
        testWith("600 m", 550, Locale.GERMAN)
        testWith("600 m", 649, Locale.GERMAN)
        testWith("900 m", 900, Locale.GERMAN)
        testWith("900 m", 940, Locale.GERMAN)
    }

    @Test
    fun `1 kilometer~ to 10 kilometer and 1-tenth km steps test`() {
        testWith("1 km", 950, Locale.GERMAN)
        testWith("1 km", 1010, Locale.GERMAN)
        testWith("1,1 km", 1050, Locale.GERMAN)
        testWith("8,9 km", 8900, Locale.GERMAN)
        testWith("9,9 km", 9850, Locale.GERMAN)
    }

    @Test
    fun `greater equal 10 kilometer and 1km steps`() {
        testWith("10 km", 9950, Locale.GERMAN)
        testWith("100 km", 100 * METERS_PER_KILOMETER, Locale.GERMAN)
        testWith("150 km", 150 * METERS_PER_KILOMETER, Locale.GERMAN)
    }

    @Test
    fun `don't display 0~ to 30 feet test`() {
        testWith("", 0, Locale.US)
        testWith("",
            feetToMeters(
                25
            ), Locale.US)
    }

    @Test
    fun `30 feet~ to 500 feet and 10ft steps test`() {
        testWith("30 ft",
            feetToMeters(
                33
            ), Locale.US)
        testWith("90 ft",
            feetToMeters(
                85
            ), Locale.US)
        testWith("100 ft",
            feetToMeters(
                95
            ), Locale.US)
        testWith("100 ft",
            feetToMeters(
                100
            ), Locale.US)
        testWith("490 ft",
            feetToMeters(
                493
            ), Locale.US)
    }

    @Test
    fun `500 feet~ to 1000 feet and 100ft steps test`() {
        testWith("500 ft",
            feetToMeters(
                495
            ), Locale.US)
        testWith("600 ft",
            feetToMeters(
                550
            ), Locale.US)
        testWith("900 ft",
            feetToMeters(
                900
            ), Locale.US)
        testWith("900 ft",
            feetToMeters(
                949
            ), Locale.US)
    }

    @Test
    fun `1000 feet~ to 3 miles and one-tenth mile steps test`() {
        testWith("0.2 mi",
            feetToMeters(
                1007
            ), Locale.US)
        testWith("0.2 mi",
            feetToMeters(
                950
            ), Locale.US)
        testWith("2.5 mi",
            feetToMeters(
                12936
            ), Locale.US)
        testWith("2.9 mi",
            feetToMeters(
                15312
            ), Locale.US)
    }

    @Test
    fun `don't display 0~ to 10 yards test`() {
        testWith("", 0, Locale.UK)
        testWith("",
            yardsToMeters(
                9
            ), Locale.UK)
    }

    @Test
    fun `10 yards~ to 500 yards and 10yd steps test`() {
        testWith("10 yd",
            yardsToMeters(
                11
            ), Locale.UK)
        testWith("100 yd",
            yardsToMeters(
                95
            ), Locale.UK)
        testWith("100 yd",
            yardsToMeters(
                100
            ), Locale.UK)
        testWith("490 yd",
            yardsToMeters(
                493
            ), Locale.UK)
    }

    @Test
    fun `500 yards~ to 850 yards and 100yd steps test`() {
        testWith("500 yd",
            yardsToMeters(
                495
            ), Locale.UK)
        testWith("600 yd",
            yardsToMeters(
                550
            ), Locale.UK)
        testWith("800 yd",
            yardsToMeters(
                844
            ), Locale.UK)
    }

    @Test
    fun `850 yards~ to 3 miles and one-tenth mile steps test`() {
        testWith("0.5 mi",
            yardsToMeters(
                855
            ), Locale.UK)
        testWith("2.5 mi",
            yardsToMeters(
                4400
            ), Locale.UK)
        testWith("2.9 mi",
            yardsToMeters(
                5104
            ), Locale.UK)
    }

    @Test
    fun `3 miles~ to 10 miles and one-tenth mile steps test`() {
        miles3To10TestWithLocale(Locale.UK)
        miles3To10TestWithLocale(Locale.US)
    }

    @Test
    fun `greater equal 10 miles and one mile steps test`() {
        miles3To10TestWithLocale(Locale.UK)
        miles3To10TestWithLocale(Locale.US)
    }

    private fun miles3To10TestWithLocale(locale: Locale) {
        testWith("3 mi",
            milesToMeters(
                3f
            ), locale)
        testWith("3.5 mi",
            milesToMeters(
                3.5f
            ), locale)
        testWith("9.9 mi",
            milesToMeters(
                9.9f
            ), locale)
    }

    private fun milesGreaterThan10WithLocale(locale: Locale) {
        testWith("10 mi",
            milesToMeters(
                9.95f
            ), locale)
        testWith("10 mi",
            milesToMeters(
                10f
            ), locale)
        testWith("59 mi",
            milesToMeters(
                59f
            ), locale)
    }

    @Test
    fun `equals and hashCode`() {
        EqualsVerifier.forClass(DistanceStringResolver::class.java).verify()
    }

    companion object {
        private const val METERS_PER_KILOMETER = 1000

        private fun feetToMeters(feet: Int) =
            Amount.valueOf(feet.toDouble(), FOOT).to(METER).estimatedValue.roundToInt()

        private fun yardsToMeters(yards: Int) =
            Amount.valueOf(yards.toDouble(), YARD).to(METER).estimatedValue.roundToInt()

        private fun milesToMeters(miles: Float) =
            Amount.valueOf(miles.toDouble(), MILE).to(METER).estimatedValue.roundToInt()
    }
}
