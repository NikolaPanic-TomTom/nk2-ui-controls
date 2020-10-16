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

package com.tomtom.nk2.core.common.uicontrols

import com.tomtom.nk2.core.common.uicontrols.test.R
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Before
import org.junit.Test

class QuantityIconDrawableResolverTest : TomTomTestCase() {

    @Before
    fun setTestTheme() {
        context.setTheme(R.style.TtiviTestTheme)
    }

    @Test
    fun `constructor with quantity 0`() {
        // GIVEN-WHEN-THEN
        QuantityIconDrawableResolver(
            0,
            R.attr.ttivi_valid_test_icon_size,
            R.attr.ttivi_valid_test_color,
            R.attr.ttivi_valid_test_font,
            R.attr.ttivi_valid_test_color_white,
            R.attr.ttivi_valid_test_text_size_h5,
            R.attr.ttivi_valid_test_text_letter_spacing_to_text_size_ratio_h5
        )
    }

    @Test
    fun `constructor with negative quantity`() {
        // GIVEN-WHEN-THEN
        QuantityIconDrawableResolver(
            -11,
            R.attr.ttivi_valid_test_icon_size,
            R.attr.ttivi_valid_test_color,
            R.attr.ttivi_valid_test_font,
            R.attr.ttivi_valid_test_color_white,
            R.attr.ttivi_valid_test_text_size_h5,
            R.attr.ttivi_valid_test_text_letter_spacing_to_text_size_ratio_h5
        )
    }

    @Test
    fun `constructor with too big quantity`() {
        // GIVEN-WHEN-THEN
        QuantityIconDrawableResolver(
            99999,
            R.attr.ttivi_valid_test_icon_size,
            R.attr.ttivi_valid_test_color,
            R.attr.ttivi_valid_test_font,
            R.attr.ttivi_valid_test_color_white,
            R.attr.ttivi_valid_test_text_size_h5,
            R.attr.ttivi_valid_test_text_letter_spacing_to_text_size_ratio_h5
        )
    }

    @Test
    fun `equals and hashCode`() {
        EqualsVerifier.forClass(QuantityIconDrawableResolver::class.java).verify()
    }
}
