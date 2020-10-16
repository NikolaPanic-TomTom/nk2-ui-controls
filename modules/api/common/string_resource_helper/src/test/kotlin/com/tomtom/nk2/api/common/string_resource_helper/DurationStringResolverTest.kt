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
import java.time.Duration
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Test

class DurationStringResolverTest : TomTomTestCase() {

    private val mockContext = niceMockk<Context>()

    @Test
    fun `1 second duration`() {
        // GIVEN
        val sut = DurationStringResolver(Duration.ofSeconds(1))

        // WHEN and THEN
        verifyGet(sut, "00:01")
    }

    @Test
    fun `1 minute duration`() {
        // GIVEN
        val sut = DurationStringResolver(Duration.ofMinutes(1))

        // WHEN and THEN
        verifyGet(sut, "01:00")
    }

    @Test
    fun `1 hour duration`() {
        // GIVEN
        val sut = DurationStringResolver(Duration.ofHours(1))

        // WHEN and THEN
        verifyGet(sut, "1:00:00")
    }

    @Test
    fun `24 hours duration`() {
        // GIVEN
        val sut = DurationStringResolver(Duration.ofHours(24))

        // WHEN and THEN
        verifyGet(sut, "24:00:00")
    }

    @Test
    fun `1 hour 1 minute 1 second duration`() {
        // GIVEN
        val sut = DurationStringResolver(Duration.ofHours(1).plusMinutes(1).plusSeconds(1))

        // WHEN and THEN
        verifyGet(sut, "1:01:01")
    }

    @Test
    fun `equals and hashCode`() {
        EqualsVerifier.forClass(DurationStringResolver::class.java).verify()
    }

    /**
     * Verify the [DurationStringResolver.get] functions returns the [expectedValue].
     */
    private fun verifyGet(sut: DurationStringResolver, expectedValue: String) {
        // WHEN
        val string = sut.get(mockContext)

        // THEN
        assertEquals(expectedValue, string)
    }
}
