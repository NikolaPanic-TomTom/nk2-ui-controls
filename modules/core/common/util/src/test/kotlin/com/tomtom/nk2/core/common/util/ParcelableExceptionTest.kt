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

package com.tomtom.nk2.core.common.util

import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import org.junit.Test

class ParcelableExceptionTest : TomTomTestCase() {

    @Test
    fun `construct ParcelableException through extension function`() {
        // GIVEN
        val illegalStateException = IllegalStateException("42")

        // WHEN
        val parcelableException = illegalStateException.toParcelable()

        // THEN
        assertEquals(IllegalStateException::class.qualifiedName, parcelableException.exceptionType)
        assertEquals("42", parcelableException.exceptionMessage)
    }

    @Test
    fun `construct ParcelableException manually`() {
        // WHEN
        val parcelableException = ParcelableException(IllegalStateException::class, "42")

        // THEN
        assertEquals(IllegalStateException::class.qualifiedName, parcelableException.exceptionType)
        assertEquals("42", parcelableException.exceptionMessage)
    }
}
