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

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import com.tomtom.nk2.tools.testing.mock.niceMockk
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import io.mockk.every
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Test

class ResourceDrawableResolverTest : TomTomTestCase() {
    private val mockDrawable = niceMockk<BitmapDrawable>()

    private val mockContext = niceMockk<Context> {
        every { getDrawable(1) } returns mockDrawable
    }

    @Test
    fun `drawable with resourceId`() {
        // GIVEN
        val tested = ResourceDrawableResolver(1)

        // WHEN and THEN
        assertEquals(tested.get(mockContext), mockDrawable)
    }

    @Test
    fun `equals and hashCode`() {
        EqualsVerifier.forClass(ResourceDrawableResolver::class.java).verify()
    }
}
