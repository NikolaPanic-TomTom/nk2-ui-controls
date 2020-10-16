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
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Test

class UidTest : TomTomTestCase() {
    @Test
    fun equals() {
        EqualsVerifier.forClass(Uid::class.java)
            .withNonnullFields("uuid")
            .verify()
    }

    @Test
    fun create() {
        val size = 100
        val map: MutableMap<String, Boolean> = HashMap(size)
        for (i in 0 until size) {
            val id = Uid.new<Any>()
            assertNull(map.put(id.toString(), true))
        }
    }

    @Test
    fun `from string`() {
        val a = Uid.fromString<Any>("d32b6789-bfbb-4194-87f3-72ce34609902")
        val s = "d32b6789-bfbb-4194-87f3-72ce34609902"
        val b = Uid.fromString<Any>(s)
        assertEquals(a, b)
    }

    @Test
    fun `to string`() {
        val x = Uid.fromString<Any>("1-2-3-4-5")
        assertEquals("00000001-0002-0003-0004-000000000005", x.toString())
    }

    @Test
    fun `to and from string`() {
        val a = Uid.new<Any>()
        val b = Uid.fromString<Any>(a.toString())
        assertEquals(a, b)
    }

    @Test
    fun `as`() {
        val a = Uid.new<Long>()
        @Suppress("UNCHECKED_CAST")
        val b = a as Uid<Int>
        assertTrue(a == b)
    }

    @Test
    fun parcelization() {
        val inputUid = Uid.new<Long>()
        val parcel = Parcel.obtain()
        parcel.writeParcelable(inputUid, 0)
        parcel.setDataPosition(0)
        val outputUid = parcel.readParcelable<Uid<Long>>(null)
        assertEquals(inputUid, outputUid)
    }
}
