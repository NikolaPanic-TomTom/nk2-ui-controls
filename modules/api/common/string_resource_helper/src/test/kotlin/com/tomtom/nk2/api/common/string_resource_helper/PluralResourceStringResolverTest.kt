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
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import io.mockk.spyk
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Test

class PluralResourceStringResolverTest : TomTomTestCase() {

    private val spyContext: Context = spyk(context)

    private val descriptor = StaticStringResolver("test")

    @Test
    fun `test plural for single`() {
        // GIVEN
        val tested = PluralResourceStringResolver(R.plurals.test_plural, 1)

        // WHEN and THEN
        verifyGet(tested, "One")
    }

    @Test
    fun `test plural for multiple`() {
        // GIVEN
        val tested = PluralResourceStringResolver(R.plurals.test_plural, 2)

        // WHEN and THEN
        verifyGet(tested, "Multiple")
    }

    @Test
    fun `test plural for argument`() {
        // GIVEN
        val tested = PluralResourceStringResolver(R.plurals.test_plural_argument, 1, "test")

        // WHEN and THEN
        verifyGet(tested, "One test")
    }

    @Test
    fun `test plural for descriptor argument`() {
        // GIVEN
        val tested = PluralResourceStringResolver(R.plurals.test_plural_argument, 1, descriptor)

        // WHEN and THEN
        verifyGet(tested, "One test")
    }

    @Test
    fun `test plural for string with formatting arguments`() {
        // GIVEN
        val tested =
            PluralResourceStringResolver(R.plurals.test_plural_multiple_arguments, 1000, 30, "10")

        // WHEN and THEN
        verifyGet(tested, "10:30 Multiple")
    }

    @Test
    fun `test plural for string with formatting arguments that are not provided`() {
        // GIVEN
        val tested = PluralResourceStringResolver(R.plurals.test_plural_multiple_arguments, 1)

        // WHEN and THEN
        verifyGet(tested, "%1\$d:%2\$s One")
    }

    @Test
    fun `equals and hashCode`() {
        EqualsVerifier.forClass(PluralResourceStringResolver::class.java).verify()
    }

    /**
     * Verify the [PluralResourceStringResolver.get] functions returns the [expectedValue] before
     * and after writing and reading the [tested] to and from a [Parcel].
     */
    private fun verifyGet(
        tested: PluralResourceStringResolver,
        expectedValue: String
    ) {
        // WHEN
        val string = tested.get(spyContext)

        // THEN
        assertEquals(expectedValue, string)

        // WHEN
        val parcel = Parcel.obtain()
        val string2 = try {
            tested.writeToParcel(parcel, 0)
            parcel.setDataPosition(0)
            val testedFromParcel = PluralResourceStringResolver.createFromParcel(parcel)
            testedFromParcel.get(spyContext)
        } finally {
            parcel.recycle()
        }

        // THEN
        assertEquals(expectedValue, string2)
    }
}
