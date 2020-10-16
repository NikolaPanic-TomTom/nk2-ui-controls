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

import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import java.time.Duration
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Test

class MinuteSecondStringResolverTest : TomTomTestCase() {

    @Test
    fun `0 second duration`() {
        // GIVEN
        val sut = MinuteSecondStringResolver(Duration.ZERO)

        // WHEN and THEN
        assertEquals("0:00", sut.get(context))
    }

    @Test
    fun `1 second duration`() {
        // GIVEN
        val sut = MinuteSecondStringResolver(Duration.ofSeconds(1))

        // WHEN and THEN
        assertEquals("0:01", sut.get(context))
    }

    @Test
    fun `1 minute duration`() {
        // GIVEN
        val sut = MinuteSecondStringResolver(Duration.ofMinutes(1))

        // WHEN and THEN
        assertEquals("1:00", sut.get(context))
    }

    @Test
    fun `10 minutes duration`() {
        // GIVEN
        val sut = MinuteSecondStringResolver(Duration.ofMinutes(10))

        // WHEN and THEN
        assertEquals("10:00", sut.get(context))
    }

    @Test
    fun `90 minutes duration`() {
        // GIVEN
        val sut = MinuteSecondStringResolver(Duration.ofMinutes(90))

        // WHEN and THEN
        assertEquals("90:00", sut.get(context))
    }

    @Test
    fun `1 minute 1 seconds duration`() {
        // GIVEN
        val sut = MinuteSecondStringResolver(Duration.ofMinutes(1).plusSeconds(1))

        // WHEN and THEN
        assertEquals("1:01", sut.get(context))
    }

    @Test
    fun `negative seconds duration`() {
        // GIVEN
        val sut = MinuteSecondStringResolver(Duration.ofSeconds(-10))

        // WHEN and THEN
        assertEquals("-0:10", sut.get(context))
    }

    @Test
    fun `negative minutes duration`() {
        // GIVEN
        val sut = MinuteSecondStringResolver(Duration.ofMinutes(-10))

        // WHEN and THEN
        assertEquals("-10:00", sut.get(context))
    }

    @Test
    fun `equals and hashCode`() {
        EqualsVerifier.forClass(MinuteSecondStringResolver::class.java).verify()
    }
}
