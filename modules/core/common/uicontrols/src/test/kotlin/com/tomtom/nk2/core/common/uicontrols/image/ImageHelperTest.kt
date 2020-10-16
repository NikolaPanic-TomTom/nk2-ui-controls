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

package com.tomtom.nk2.core.common.uicontrols.image

import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import org.junit.Test

class ImageHelperTest : TomTomTestCase() {

    private val object1 = "A"
    private val object2Same1 = "A"
    private val object3 = "B"

    @Test
    fun `empty args`() {
        // GIVEN

        // WHEN THEN
        generatePlaceholderImage()
    }

    @Test
    fun `twice same object`() {
        // GIVEN

        // WHEN
        val drawable1 = generatePlaceholderImage(object1)
        val drawable2 = generatePlaceholderImage(object1)

        // THEN
        assertEquals(drawable1, drawable2)
    }

    @Test
    fun `object with same hash`() {
        // GIVEN

        // WHEN
        val drawable1 = generatePlaceholderImage(object1)
        val drawable2 = generatePlaceholderImage(object2Same1)

        // THEN
        assertEquals(drawable1, drawable2)
    }

    @Test
    fun `different object hash`() {
        // GIVEN

        // WHEN
        val drawable1 = generatePlaceholderImage(object1)
        val drawable2 = generatePlaceholderImage(object3)

        // THEN
        assertNotEquals(drawable1, drawable2)
    }

    @Test
    fun `same object with different combination`() {
        // GIVEN

        // WHEN
        val drawable1 = generatePlaceholderImage(object1, object1)
        val drawable2 = generatePlaceholderImage(object1)

        // THEN
        assertNotEquals(drawable1, drawable2)
    }
}
