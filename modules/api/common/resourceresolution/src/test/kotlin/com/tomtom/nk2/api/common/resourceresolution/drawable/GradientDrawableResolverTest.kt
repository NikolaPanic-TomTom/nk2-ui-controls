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

package com.tomtom.nk2.api.common.resourceresolution.drawable

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import com.tomtom.nk2.api.common.resourceresolution.test.R
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Before
import org.junit.Test

class GradientDrawableResolverTest : TomTomTestCase() {

    @Before
    fun setTestTheme() {
        context.setTheme(R.style.TtiviTestTheme)
    }

    @Test
    fun `gradient get`() {
        // GIVEN
        val tested = GradientDrawableResolver(
            GradientDrawable.Orientation.TL_BR,
            R.attr.ttivi_valid_test_gradient
        )

        // WHEN and THEN
        verifyGet(tested, GradientDrawable.Orientation.TL_BR, intArrayOf(
            Color.parseColor("#7bff46"),
            Color.parseColor("#4cd2ff"),
            Color.parseColor("#2acbdc")
        ))
    }

    @Test
    fun `equals and hashCode`() {
        EqualsVerifier.forClass(GradientDrawableResolver::class.java).verify()
    }

    /**
     * Verifies the [GradientDrawableResolver.get] functions returns the [expectedOrientation] and
     * the [expectedColors]
     */
    private fun verifyGet(
        tested: GradientDrawableResolver,
        expectedOrientation: GradientDrawable.Orientation,
        expectedColors: IntArray
    ) {
        // WHEN
        val drawable = tested.get(context)

        // THEN
        assertEquals(expectedOrientation, drawable.orientation)
        assertArrayEquals(expectedColors, drawable.colors)
    }
}
