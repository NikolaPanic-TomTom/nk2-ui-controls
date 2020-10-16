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

import android.os.Parcel
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import org.junit.Test

class ParcelableEnumTest : TomTomTestCase() {

    enum class TestEnum {
        TEST1,
        TEST2
    }

    @Test
    fun equals() {
        val sut1 = ParcelableEnum(TestEnum.TEST1)
        val sut2 = ParcelableEnum(TestEnum.TEST1)

        assertEquals(sut1, sut2)
    }

    @Test
    fun `not equals`() {
        val sut1 = ParcelableEnum(TestEnum.TEST1)
        val sut2 = ParcelableEnum(TestEnum.TEST2)

        assertNotEquals(sut1, sut2)
    }

    @Test
    fun `Write and read enum TEST1 to and from parcel`() {
        // GIVEN
        val sut = ParcelableEnum(TestEnum.TEST1)
        val parcel = Parcel.obtain()

        // WHEN
        parcel.writeValue(sut)
        parcel.setDataPosition(0)
        val classLoader = ParcelableEnumTest::class.java.classLoader!!
        val createParcelableEnum = parcel.readValue(classLoader) as ParcelableEnum
        val enum = createParcelableEnum.toEnum(classLoader)

        // THEN
        assertEquals(TestEnum.TEST1, enum)
    }

    @Test
    fun `Write and read enum TEST2 to and from parcel`() {
        // GIVEN
        val sut = ParcelableEnum(TestEnum.TEST2)
        val parcel = Parcel.obtain()

        // WHEN
        parcel.writeValue(sut)
        parcel.setDataPosition(0)
        val classLoader = ParcelableEnumTest::class.java.classLoader!!
        val createParcelableEnum = parcel.readValue(classLoader) as ParcelableEnum
        val enum = createParcelableEnum.toEnum(classLoader)

        // THEN
        assertEquals(TestEnum.TEST2, enum)
    }
}
