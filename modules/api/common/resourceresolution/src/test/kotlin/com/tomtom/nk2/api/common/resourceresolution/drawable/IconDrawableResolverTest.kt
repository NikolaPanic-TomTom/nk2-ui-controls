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
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Icon
import com.tomtom.nk2.tools.testing.mock.niceMockk
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import io.mockk.every
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Test

class IconDrawableResolverTest : TomTomTestCase() {

    private val bitmapBlack: Bitmap = Bitmap.createBitmap(
        IntArray(IMAGE_SIZE * IMAGE_SIZE) { Color.BLACK },
        IMAGE_SIZE,
        IMAGE_SIZE,
        Bitmap.Config.ARGB_8888
    )
    private val iconBlack: Icon = Icon.createWithBitmap(bitmapBlack)

    private val bitmapBlue: Bitmap = Bitmap.createBitmap(
        IntArray(IMAGE_SIZE * IMAGE_SIZE) { Color.BLUE },
        IMAGE_SIZE,
        IMAGE_SIZE,
        Bitmap.Config.ARGB_8888
    )
    private val iconBlue: Icon = Icon.createWithBitmap(bitmapBlue)

    private val mockDrawable = niceMockk<BitmapDrawable>()

    private val mockIcon = niceMockk<Icon>() {
        every { loadDrawable(any()) } returns mockDrawable
    }

    @Test
    fun `drawable with resourceId`() {
        // GIVEN
        val tested = IconDrawableResolver(mockIcon)

        // WHEN and THEN
        assertEquals(tested.get(context), mockDrawable)
    }

    @Test
    fun `equals and hashCode`() {
        EqualsVerifier.forClass(IconDrawableResolver::class.java).withPrefabValues(
            Icon::class.java,
            iconBlue,
            iconBlack
        ).verify()
    }

    companion object {
        private const val IMAGE_SIZE = 100
    }
}
