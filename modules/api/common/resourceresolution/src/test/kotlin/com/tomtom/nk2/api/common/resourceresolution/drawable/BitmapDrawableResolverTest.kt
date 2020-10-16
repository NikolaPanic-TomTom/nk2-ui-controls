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

import android.graphics.Bitmap
import android.graphics.Color
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Test

class BitmapDrawableResolverTest : TomTomTestCase() {
    private val bitmapBlack: Bitmap = Bitmap.createBitmap(
        IntArray(IMAGE_SIZE * IMAGE_SIZE) { Color.BLACK },
        IMAGE_SIZE,
        IMAGE_SIZE,
        Bitmap.Config.ARGB_8888
    )

    private val bitmapBlue: Bitmap = Bitmap.createBitmap(
        IntArray(IMAGE_SIZE * IMAGE_SIZE) { Color.BLUE },
        IMAGE_SIZE,
        IMAGE_SIZE,
        Bitmap.Config.ARGB_8888
    )

    @Test
    fun `bitmap get`() {
        // GIVEN
        val tested = BitmapDrawableResolver(bitmapBlack)

        // WHEN and THEN
        verifyGet(tested, bitmapBlack)
    }

    @Test
    fun `equals and hashCode`() {
        EqualsVerifier.forClass(BitmapDrawableResolver::class.java).withPrefabValues(
            Bitmap::class.java,
            bitmapBlack,
            bitmapBlue
        ).verify()
    }

    /**
     * Verifies the [BitmapDrawableResolver.get] functions returns the [expectedValue]
     */
    private fun verifyGet(tested: BitmapDrawableResolver, expectedValue: Bitmap) {
        // WHEN
        val drawable = tested.get(context)

        // THEN
        assertEquals(expectedValue, drawable.bitmap)
    }

    companion object {
        private const val IMAGE_SIZE = 100
    }
}
