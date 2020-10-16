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

package com.tomtom.nk2.api.common.resourceresolution

import com.tomtom.nk2.api.common.resourceresolution.test.R
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import kotlin.test.assertFailsWith
import org.junit.Before
import org.junit.Test

class ContextExtensionsTest : TomTomTestCase() {

    @Before
    fun setTestTheme() {
        context.setTheme(R.style.TtiviTestTheme)
    }

    @Test
    fun `get existing color`() {
        assertEquals(0x12345678, context.getColorByAttr(R.attr.ttivi_valid_test_color))
    }

    @Test
    fun `get unspecified color`() {
        assertFailsWith<IllegalArgumentException> {
            context.getColorByAttr(R.attr.ttivi_only_defined_test_color)
        }
    }

    @Test
    fun `get unknown color`() {
        assertFailsWith<IllegalArgumentException> {
            val unknownColor = 0xA03010A
            context.getColorByAttr(unknownColor)
        }
    }

    @Test
    fun `get existing array color`() {
        assertArrayEquals(
            intArrayOf(
                0xFF7bff46.toInt(),
                0xFF4CD2FF.toInt(),
                0xFF2ACBDC.toInt()
            ), context.getColorArrayByAttr(R.attr.ttivi_valid_test_gradient)
        )
    }

    @Test
    fun `get undefined array color`() {
        assertFailsWith<IllegalArgumentException> {
            context.getColorArrayByAttr(R.attr.ttivi_undefined_test_gradient)
        }
    }

    @Test
    fun `get unknown array color attribute`() {
        assertFailsWith<IllegalArgumentException> {
            val unknownColorArrayAttribute = 0xA03010A
            context.getColorArrayByAttr(unknownColorArrayAttribute)
        }
    }

    @Test
    fun `get font unknown attribute`() {
        assertFailsWith<IllegalArgumentException> {
            val unknownFontAttribute = 0xA03010A
            context.getFontByAttr(unknownFontAttribute)
        }
    }

    @Test
    fun `get font undefined attribute`() {
        assertFailsWith<IllegalArgumentException> {
            context.getFontByAttr(R.attr.ttivi_undefined_test_font)
        }
    }

    @Test
    fun `get font attribute`() {
        assertEquals(
            context.resources.getFont(R.font.font_test),
            context.getFontByAttr(R.attr.ttivi_valid_test_font)
        )
    }
}
