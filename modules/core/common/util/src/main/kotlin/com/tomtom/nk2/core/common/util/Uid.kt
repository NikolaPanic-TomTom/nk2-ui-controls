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
import java.util.Locale
import java.util.UUID

/**
 * Generic immutable unique ID class. Really just an abstraction of UUIDs. Used to uniquely
 * identify things.
 *
 * The class has a generic type T to allow creating typesafe IDs, like Uid<Message> or Uid<Person>.
 *
 * For example, this does not compile:
 *
 * fun compileTimeTypeSafetyExamples() {
 *     var idInt = Uid.new<Int>()
 *     var idLong = Uid.new<Long>()
 *     idInt == idLong              // This is OK.
 *     idInt = idLong               // This does not compile
 * }
 *
 * The class represents UUIDs as Strings internally, to avoid loads of UUID to String conversions
 * all the time. This makes the class considerably faster in use than the regular [UUID] class.
 *
 * @param T Type tag for ID, to make IDs type-safe.
 */
class Uid<T> private constructor(private val uuid: String) : Parcelable {
    /**
     * Returns the string representation of this Uid. Opposite of [fromString].
     *
     * @return String representation of ID.
     */
    override fun toString() = uuid

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Uid<*>
        return uuid == other.uuid
    }

    override fun hashCode() = uuid.hashCode()

    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uuid)
    }

    override fun describeContents() = 0

    companion object {
        private const val UUID_DASH = '-'
        private const val UUID_MIN_LENGTH = 9
        private const val UUID_MAX_LENGTH = 36
        private val UUID_DASH_POS = intArrayOf(8, 13, 18, 23)

        /**
         * Create a new, unique UUID-based ID.
         */
        fun <T> new() = Uid<T>(UUID.randomUUID().toString())

        /**
         * Instantiates a [Uid] with a string. Mainly used when de-serializing existing entities.
         *
         * The format of 'uuid' is checked to comply with a standard UUID format, which is:
         * - Dashes at positions 8, 13, 18, 23 (base 0).
         * - Characters 0-9 and a-f (lowercase only).
         *
         * If this format is used, the creation of the [Uid] is very fast. If an alternative format
         * us used, as accepted by [fromString], the call is much more expensive.
         *
         * @param uuidAsString An existing string representation of a UUID.
         * @throws [IllegalArgumentException] If [uuidAsString] does not conform to the string
         * representation as described in [UUID.toString].
         */
        fun <T> fromString(uuidAsString: String): Uid<T> {
            /**
             * This code has been optimized to NOT just call [UUID.fromString] to convert the
             * UUID-String into a String (and catch an [IllegalArgumentException]).
             *
             * If the UUID does not comply, the expensive call to [UUID.fromString] is made after
             * all.
             */
            val length = uuidAsString.length
            require(length in UUID_MIN_LENGTH..UUID_MAX_LENGTH) {
                "Length of UUID must be [" + UUID_MIN_LENGTH + ", " +
                    UUID_MAX_LENGTH + "], but is " + uuidAsString.length + ", uuid=" + uuidAsString
            }
            // Check dashes.
            val convertedUuidString = if (areDashesAtCorrectPosition(uuidAsString)) {
                uuidAsString.toLowerCase(Locale.ROOT).also {
                    require(onlyContainsValidUuidCharacters(it)) {
                        "Incorrect UUID format, uuid=$uuidAsString"
                    }
                }
            } else {
                UUID.fromString(uuidAsString).toString().toLowerCase(Locale.ROOT)
            }
            return Uid(convertedUuidString)
        }

        /**
         * Return if string contains valid UUID characters only.
         * Must be converted to lowercase already.
         *
         * @param uuid Input UUID. Should be lowercase already.
         * @return True if valid characters only.
         */
        private fun onlyContainsValidUuidCharacters(uuid: String) =
            uuid.toCharArray().all { it in '0'..'9' || it in 'a'..'f' || it == UUID_DASH }

        /**
         * Checks if the dashes are at the right positions for a UUID.
         *
         * @param s Input string.
         * @return True if the dashes are correctly placed.
         */
        private fun areDashesAtCorrectPosition(s: String) =
            UUID_DASH_POS.all { s.length > it && s[it] == '-' }

        @JvmField val CREATOR = object : Parcelable.Creator<Uid<*>> {
            override fun createFromParcel(parcel: Parcel): Uid<*> = Uid<Any>(parcel)
            override fun newArray(size: Int): Array<Uid<*>?> = arrayOfNulls(size)
        }
    }
}
