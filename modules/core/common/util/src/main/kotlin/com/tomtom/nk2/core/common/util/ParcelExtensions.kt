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
import android.util.ArraySet
import android.util.SparseArray
import androidx.core.util.set
import java.lang.RuntimeException

/** Public required to allow `inline` extensions functions. */
const val NULL_VALUE = -1

/**
 * Optimized way to write a [Collection] to a parcel.
 *
 * [Parcel.writeList] uses [Parcel.writeValue] which determines the list element type at runtime
 * and adds this type for each list element to the parcel. This results in doubling the size of
 * a parcel for an int list. An alternative is to convert the list type to an array. This comes with
 * the price of copying the whole list twice (instead of only once). [Parcel.writeTypedList] is
 * only capable of writing Parcelable elements.
 *
 * This variant writes a collection and uses a lambda to write the elements. As such the list can
 * have any element type which is supported by [Parcel].
 *
 * @param T The list element type.
 * @param collection The collection to write to the parcel or null.
 * @param elementWriter Invoked for each element in the list to write the element to the parcel.
 */
inline fun <T> Parcel.writeCollectionOptimized(
    collection: Collection<T>?,
    elementWriter: (Parcel, T) -> Unit
) {
    if (collection == null) {
        writeInt(NULL_VALUE)
        return
    }
    writeInt(collection.size)
    collection.forEach { element ->
        elementWriter(this, element)
    }
}

/**
 * Creates and reads a list with element type [T] from a parcel.
 *
 * The list must have been written by [Parcel.writeCollectionOptimized].
 * Returns `null` when the `collection` argument of [Parcel.writeCollectionOptimized] was `null`.
 *
 * @param T The list element type.
 * @param elementReader Invoked for each element in the list to read the element from the parcel.
 * @return The created list or `null`.
 */
inline fun <T> Parcel.createListOptimized(elementReader: (Parcel) -> T): List<T>? {
    val size = readInt()
    if (size == NULL_VALUE) {
        return null
    }
    require(size >= 0)
    val list = ArrayList<T>(size)
    repeat(size) {
        list.add(elementReader(this))
    }
    return list
}

/**
 * Creates and reads a set with element type [T] from a parcel.
 *
 * The set must have been written by [Parcel.writeCollectionOptimized].
 * Returns `null` when the `collection` argument of [Parcel.writeCollectionOptimized] was `null`.
 *
 * @param T The set element type.
 * @param elementReader Invoked for each element in the set to read the element from the parcel.
 * @return The created set or `null`.
 */
inline fun <T> Parcel.createSetOptimized(elementReader: (Parcel) -> T): Set<T>? {
    val size = readInt()
    if (size == NULL_VALUE) {
        return null
    }
    require(size >= 0)
    val set = ArraySet<T>(size)
    repeat(size) {
        set.add(elementReader(this))
    }
    return set
}

/**
 * Same as [Parcel.writeCollectionOptimized] except for a SparseArray.
 */
inline fun <T> Parcel.writeSparseArrayOptimized(
    sparseArray: SparseArray<T>?,
    elementWriter: (Parcel, T) -> Unit
) {
    if (sparseArray == null) {
        writeInt(NULL_VALUE)
        return
    }
    val size = sparseArray.size()
    writeInt(size)
    for (i in 0 until size) {
        writeInt(sparseArray.keyAt(i))
        elementWriter(this, sparseArray.valueAt(i))
    }
}

/**
 * Same as [Parcel.createListOptimized] except for a  SparseArray.
 */
inline fun <T> Parcel.createSparseArrayOptimized(elementReader: (Parcel) -> T): SparseArray<T>? {
    val size = readInt()
    if (size == NULL_VALUE) {
        return null
    }
    require(size >= 0)
    val sparseArray = SparseArray<T>(size)
    repeat(size) {
        sparseArray[readInt()] = elementReader(this)
    }
    return sparseArray
}

/**
 * Optimized and more future rich way to write a map to a parcel.
 *
 * [Parcel.writeMap] uses [Parcel.writeValue] which determines the map value type at runtime
 * and adds this type for each map value to the parcel. This results in doubling the size of the
 * parcel when writing an int map. Next both [Parcel.writeMap] and [Parcel.writeBundle] are limited
 * to maps with a string as keys.
 *
 * This variant writes a map and uses a lambda to write the map entries. As such the map can have
 * any key and value type which are supported by [Parcel].
 *
 * @param K The map key type.
 * @param V The map value type.
 * @param map The map to write to the parcel or null.
 * @param entryWriter Invoked for each entry in the map to write the entry to the parcel.
 */
inline fun <K, V> Parcel.writeMapOptimized(
    map: Map<K, V>?,
    entryWriter: (Parcel, Map.Entry<K, V>) -> Unit
) {
    if (map == null) {
        writeInt(NULL_VALUE)
        return
    }
    writeInt(map.size)
    map.forEach { entry ->
        entryWriter(this, entry)
    }
}

/**
 * Creates and reads a map with key type [K] and value type [V] from a parcel.
 *
 * The map must have been written by [Parcel.writeMapOptimized].
 * Returns `null` when the `map` argument of [Parcel.writeMapOptimized] was `null`.
 *
 * @param K The map key type.
 * @param V The map value type.
 * @param entryReader Invoked for each entry in the map to read the entry from the parcel.
 * @return The created map or `null`.
 */
inline fun <K, V> Parcel.createMapOptimized(entryReader: (Parcel) -> Pair<K, V>): Map<K, V>? {
    val size = readInt()
    if (size == NULL_VALUE) {
        return null
    }
    require(size >= 0)
    val list = HashMap<K, V>(size)
    repeat(size) {
        val pair = entryReader(this)
        list[pair.first] = pair.second
    }
    return list
}

/**
 * Writes [array] to the parcel, like [Parcel.writeArray]. Except this function will not throw
 * a [RuntimeException] when the array contains an element that is not supported by
 * [Parcel.writeValue].
 *
 * When an element is not supported the `toString()` value of the element is written to the parcel
 * instead.
 *
 * @note Only use this method when the types are unknown at runtime. For type-safe arrays use
 *     [Parcel.writeCollectionOptimized] instead.
 *
 * @param array The array to write to the parcel.
 * @return `true` when all elements are supported, `false` when at least one element is not
 *     supported.
 */
fun Parcel.writeUnsafeArray(array: Array<Any?>?): Boolean {
    if (array == null) {
        writeInt(NULL_VALUE)
        return true
    }
    writeInt(array.size)
    var allSupported = true
    array.forEach { element ->
        try {
            writeValue(element)
        } catch (e: RuntimeException) {
            writeValue(element.toString())
            allSupported = false
        }
    }
    return allSupported
}
