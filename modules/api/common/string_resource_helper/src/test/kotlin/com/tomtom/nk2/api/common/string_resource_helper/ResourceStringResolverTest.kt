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
import android.os.Parcel
import com.tomtom.nk2.tools.testing.mock.niceMockk
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import io.mockk.every
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Test

class ResourceStringResolverTest : TomTomTestCase() {

    private val mockContext = niceMockk<Context> {
        every { getString(1) } returns "string"
        every { getString(1, "a") } returns "string [a]"
        every { getString(1, 2) } returns "string [2]"
        every { getString(1, 'a', 2, 'b') } returns "string [a, 2, b]"
    }

    private val descriptor = StaticStringResolver("a")

    @Test
    fun `string without argument`() {
        // GIVEN
        val tested = ResourceStringResolver(1)

        // WHEN and THEN
        verifyGet(tested, "string")
    }

    @Test
    fun `string with string argument`() {
        // GIVEN
        val tested = ResourceStringResolver(1, "a")

        // WHEN and THEN
        verifyGet(tested, "string [a]")
    }

    @Test
    fun `string with int argument`() {
        // GIVEN
        val tested = ResourceStringResolver(1, 2)

        // WHEN and THEN
        verifyGet(tested, "string [2]")
    }

    @Test
    fun `string with descriptor argument`() {
        // GIVEN
        val tested = ResourceStringResolver(1, descriptor)

        // WHEN and THEN
        verifyGet(tested, "string [a]")
    }

    @Test
    fun `string with multiple arguments`() {
        // GIVEN
        val tested = ResourceStringResolver(1, 'a', 2, 'b')

        // WHEN and THEN
        verifyGet(tested, "string [a, 2, b]")
    }

    @Test
    fun `equals and hashCode`() {
        EqualsVerifier.forClass(ResourceStringResolver::class.java).verify()
    }

    /**
     * Verify the [ResourceStringResolver.get] functions returns the [expectedValue] before and
     * after writing and reading the [tested] to and from a [Parcel].
     */
    private fun verifyGet(tested: ResourceStringResolver, expectedValue: String) {
        // WHEN
        val string = tested.get(mockContext)

        // THEN
        assertEquals(expectedValue, string)

        // WHEN
        val parcel = Parcel.obtain()
        val string2 = try {
            tested.writeToParcel(parcel, 0)
            parcel.setDataPosition(0)
            val testedFromParcel = ResourceStringResolver.createFromParcel(parcel)
            testedFromParcel.get(mockContext)
        } finally {
            parcel.recycle()
        }

        // THEN
        assertEquals(expectedValue, string2)
    }
}
