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
import android.os.Parcelable
import android.util.SparseArray
import androidx.core.util.set
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import kotlinx.android.parcel.Parcelize
import org.junit.Test

class ParcelExtensionsTest : TomTomTestCase() {

    @Parcelize
    data class ParcelableClass(val int: Int, val string: String) : Parcelable

    @Test
    fun `write and create int list`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val list = listOf(1, 2, 3, 42)

        // WHEN
        parcel.writeCollectionOptimized(list) { p, element ->
            p.writeInt(element)
        }

        parcel.setDataPosition(0)
        val createdList = parcel.createListOptimized { p ->
            p.readInt()
        }

        // THEN
        assertEquals(list, createdList)
    }

    @Test
    fun `write and create string list`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val list = listOf("a", "b", "c", "42")

        // WHEN
        parcel.writeCollectionOptimized(list) { p, element ->
            p.writeString(element)
        }

        parcel.setDataPosition(0)
        val createdList = parcel.createListOptimized { p ->
            p.readString()!!
        }

        // THEN
        assertEquals(list, createdList)
    }

    @Test
    fun `write and create parcelable list`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val list = listOf(
            ParcelableClass(
                1,
                "a"
            ),
            ParcelableClass(
                2,
                "b"
            )
        )

        // WHEN
        parcel.writeCollectionOptimized(list) { p, element ->
            p.writeParcelable(element, 0)
        }

        parcel.setDataPosition(0)
        val createdList = parcel.createListOptimized { p ->
            p.readParcelable<ParcelableClass>(
                ParcelExtensionsTest::class.java.classLoader
            )!!
        }

        // THEN
        assertEquals(list, createdList)
    }

    @Test
    fun `write and create null list`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val list: List<Int>? = null

        // WHEN
        parcel.writeCollectionOptimized(list) { p, element ->
            p.writeInt(element)
        }

        parcel.setDataPosition(0)
        val createdList = parcel.createListOptimized { p ->
            p.readInt()
        }

        // THEN
        assertNull(createdList)
    }

    @Test
    fun `write and create empty list`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val list = emptyList<Int>()

        // WHEN
        parcel.writeCollectionOptimized(list) { p, element ->
            p.writeInt(element)
        }

        parcel.setDataPosition(0)
        val createdList = parcel.createListOptimized { p ->
            p.readInt()
        }

        // THEN
        assertNotNull(createdList)
        assertEquals(0, createdList!!.size)
    }

    @Test
    fun `write and create int set`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val set = setOf(1, 2, 3, 42)

        // WHEN
        parcel.writeCollectionOptimized(set) { p, element ->
            p.writeInt(element)
        }

        parcel.setDataPosition(0)
        val createdSet = parcel.createSetOptimized { p ->
            p.readInt()
        }

        // THEN
        assertEquals(set, createdSet)
    }

    @Test
    fun `write and create string set`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val set = setOf("a", "b", "c", "42")

        // WHEN
        parcel.writeCollectionOptimized(set) { p, element ->
            p.writeString(element)
        }

        parcel.setDataPosition(0)
        val createdSet = parcel.createSetOptimized { p ->
            p.readString()!!
        }

        // THEN
        assertEquals(set, createdSet)
    }

    @Test
    fun `write and create parcelable set`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val set = setOf(
            ParcelableClass(
                1,
                "a"
            ),
            ParcelableClass(
                2,
                "b"
            )
        )

        // WHEN
        parcel.writeCollectionOptimized(set) { p, element ->
            p.writeParcelable(element, 0)
        }

        parcel.setDataPosition(0)
        val createdSet = parcel.createSetOptimized { p ->
            p.readParcelable<ParcelableClass>(
                ParcelExtensionsTest::class.java.classLoader
            )!!
        }

        // THEN
        assertEquals(set, createdSet)
    }

    @Test
    fun `write and create null set`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val set: Set<Int>? = null

        // WHEN
        parcel.writeCollectionOptimized(set) { p, element ->
            p.writeInt(element)
        }

        parcel.setDataPosition(0)
        val createdSet = parcel.createSetOptimized { p ->
            p.readInt()
        }

        // THEN
        assertNull(createdSet)
    }

    @Test
    fun `write and create empty set`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val set = emptySet<Int>()

        // WHEN
        parcel.writeCollectionOptimized(set) { p, element ->
            p.writeInt(element)
        }

        parcel.setDataPosition(0)
        val createdSet = parcel.createSetOptimized { p ->
            p.readInt()
        }

        // THEN
        assertNotNull(createdSet)
        assertEquals(0, createdSet!!.size)
    }

    @Test
    fun `write and create sparse int array`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val sparseArray = SparseArray<Int>()
        sparseArray[0] = 1
        sparseArray[42] = -42

        // WHEN
        parcel.writeSparseArrayOptimized(sparseArray) { p, element ->
            p.writeInt(element)
        }

        parcel.setDataPosition(0)
        val createdSparseArray = parcel.createSparseArrayOptimized { p ->
            p.readInt()
        }

        // THEN
        assertNotNull(createdSparseArray)
        assertEquals(sparseArray.size(), createdSparseArray!!.size())
        assertEquals(sparseArray[0], createdSparseArray[0])
        assertEquals(sparseArray[42], createdSparseArray[42])
    }

    @Test
    fun `write and create sparse string array`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val sparseArray = SparseArray<String>()
        sparseArray[0] = "a"
        sparseArray[42] = "42"

        // WHEN
        parcel.writeSparseArrayOptimized(sparseArray) { p, element ->
            p.writeString(element)
        }

        parcel.setDataPosition(0)
        val createdSparseArray = parcel.createSparseArrayOptimized { p ->
            p.readString()!!
        }

        // THEN
        assertNotNull(createdSparseArray)
        assertEquals(sparseArray.size(), createdSparseArray!!.size())
        assertEquals(sparseArray[0], createdSparseArray[0])
        assertEquals(sparseArray[42], createdSparseArray[42])
    }

    @Test
    fun `write and create sparse parcelable array`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val sparseArray = SparseArray<ParcelableClass>()
        sparseArray[0] =
            ParcelableClass(
                0,
                "a"
            )
        sparseArray[42] =
            ParcelableClass(
                -42,
                "42"
            )

        // WHEN
        parcel.writeSparseArrayOptimized(sparseArray) { p, element ->
            p.writeParcelable(element, 0)
        }

        parcel.setDataPosition(0)
        val createdSparseArray = parcel.createSparseArrayOptimized { p ->
            p.readParcelable<ParcelableClass>(
                ParcelExtensionsTest::class.java.classLoader
            )!!
        }

        // THEN
        assertNotNull(createdSparseArray)
        assertEquals(sparseArray.size(), createdSparseArray!!.size())
        assertEquals(sparseArray[0], createdSparseArray[0])
        assertEquals(sparseArray[42], createdSparseArray[42])
    }

    @Test
    fun `write and create null sparse array`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val sparseArray: SparseArray<Int>? = null

        // WHEN
        parcel.writeSparseArrayOptimized(sparseArray) { p, element ->
            p.writeInt(element)
        }

        parcel.setDataPosition(0)
        val createdSparseArray = parcel.createSparseArrayOptimized { p ->
            p.readInt()
        }

        // THEN
        assertNull(createdSparseArray)
    }

    @Test
    fun `write and create empty sparse array`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val sparseArray = SparseArray<Int>()

        // WHEN
        parcel.writeSparseArrayOptimized(sparseArray) { p, element ->
            p.writeInt(element)
        }

        parcel.setDataPosition(0)
        val createdSparseArray = parcel.createSparseArrayOptimized { p ->
            p.readInt()
        }

        // THEN
        assertNotNull(createdSparseArray)
        assertEquals(0, createdSparseArray!!.size())
    }

    @Test
    fun `write and create int to string map`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val map = mapOf(1 to "a", 2 to "b", 3 to "c", 42 to "42")

        // WHEN
        parcel.writeMapOptimized(map) { p, entry ->
            p.writeInt(entry.key)
            p.writeString(entry.value)
        }

        parcel.setDataPosition(0)
        val createdMap = parcel.createMapOptimized { p ->
            Pair(p.readInt(), p.readString()!!)
        }

        // THEN
        assertEquals(map, createdMap)
    }

    @Test
    fun `write and create string to int map`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val map = mapOf("a" to 1, "b" to 2, "c" to 3, "42" to 42)

        // WHEN
        parcel.writeMapOptimized(map) { p, entry ->
            p.writeString(entry.key)
            p.writeInt(entry.value)
        }

        parcel.setDataPosition(0)
        val createdMap = parcel.createMapOptimized { p ->
            Pair(p.readString()!!, p.readInt())
        }

        // THEN
        assertEquals(map, createdMap)
    }

    @Test
    fun `write and create int to parcelable map`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val map = mapOf(
            1 to ParcelableClass(
                0,
                "a"
            ), 2 to ParcelableClass(
                42,
                "42"
            )
        )

        // WHEN
        parcel.writeMapOptimized(map) { p, entry ->
            p.writeInt(entry.key)
            p.writeParcelable(entry.value, 0)
        }

        parcel.setDataPosition(0)
        val createdMap = parcel.createMapOptimized { p ->
            Pair(
                p.readInt(),
                p.readParcelable<ParcelableClass>(
                    ParcelExtensionsTest::class.java.classLoader
                )!!
            )
        }

        // THEN
        assertEquals(map, createdMap)
    }

    @Test
    fun `write and create null map`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val map: Map<Int, String>? = null

        // WHEN
        parcel.writeMapOptimized(map) { p, entry ->
            p.writeInt(entry.key)
            p.writeString(entry.value)
        }

        parcel.setDataPosition(0)
        val createdMap = parcel.createMapOptimized { p ->
            Pair(p.readInt(), p.readString()!!)
        }

        // THEN
        assertNull(createdMap)
    }

    @Test
    fun `write and create empty map`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val map = mapOf<Int, String>()

        // WHEN
        parcel.writeMapOptimized(map) { p, entry ->
            p.writeInt(entry.key)
            p.writeString(entry.value)
        }

        parcel.setDataPosition(0)
        val createdMap = parcel.createMapOptimized { p ->
            Pair(p.readInt(), p.readString()!!)
        }

        // THEN
        assertNotNull(createdMap)
        assertEquals(0, createdMap!!.size)
    }

    @Test
    fun `write unsafe array`() {
        // GIVEN
        val parcel = Parcel.obtain()
        val array = arrayOf(1, "2", null, NotParcelableObject(42))

        // WHEN
        parcel.writeUnsafeArray(array)

        parcel.setDataPosition(0)
        val createdArray = parcel.readArray(ParcelExtensionsTest::class.java.classLoader)

        // THEN
        assertNotNull(createdArray)
        assertEquals(4, createdArray!!.size)
        assertEquals(1, createdArray[0])
        assertEquals("2", createdArray[1])
        assertEquals(null, createdArray[2])
        assertEquals("42", createdArray[3])
    }
}

class NotParcelableObject(private val something: Int) {
    override fun toString(): String {
        return "$something"
    }
}
