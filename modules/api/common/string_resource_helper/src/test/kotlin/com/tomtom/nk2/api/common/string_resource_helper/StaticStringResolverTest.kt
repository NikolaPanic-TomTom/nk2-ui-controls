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

import android.os.Parcel
import com.tomtom.nk2.tools.testing.mock.niceMockk
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import io.mockk.every
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Test

class StaticStringResolverTest : TomTomTestCase() {

    private val mockDescriptor = niceMockk<StringResolver> {
        every { this@niceMockk.toString() } returns "toString"
    }

    @Test
    fun `string with string argument`() {
        // GIVEN
        val tested = StaticStringResolver("a")

        // WHEN and THEN
        verifyGet(tested, "a")
    }

    @Test
    fun `string with int argument`() {
        // GIVEN
        val tested = StaticStringResolver(2)

        // WHEN and THEN
        verifyGet(tested, "2")
    }

    @Test
    fun `string with descriptor argument`() {
        // GIVEN
        val tested = StaticStringResolver(mockDescriptor)

        // WHEN and THEN
        verifyGet(tested, "toString")
    }

    @Test
    fun `equals and hashCode`() {
        EqualsVerifier.forClass(StaticStringResolver::class.java).verify()
    }

    /**
     * Verifies the [StaticStringResolver.get] functions returns the [expectedValue] before and
     * after writing and reading the [tested] to and from a [Parcel].
     */
    private fun verifyGet(tested: StaticStringResolver, expectedValue: String) {
        // WHEN
        val string = tested.get(context)

        // THEN
        assertEquals(expectedValue, string)

        // WHEN
        val parcel = Parcel.obtain()
        val string2 = try {
            tested.writeToParcel(parcel, 0)
            parcel.setDataPosition(0)
            val testedFromParcel = StaticStringResolver.createFromParcel(parcel)
            testedFromParcel.get(context)
        } finally {
            parcel.recycle()
        }

        // THEN
        assertEquals(expectedValue, string2)
    }
}
