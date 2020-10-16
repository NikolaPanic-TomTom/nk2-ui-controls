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

package com.tomtom.nk2.api.common.string_resource_helper

import android.content.Context
import android.content.res.Resources
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.PluralsRes

/**
 * A [StringResolver] that retrieves pluralised strings from the given [Context].
 * For more information on the returned values see [Resources.getQuantityString].
 */
// The `@Parcelize` annotation cannot be used here due to the type of [formatArguments].
class PluralResourceStringResolver : StringResolver {

    @PluralsRes
    private val resourceId: Int
    private val quantity: Int
    private val formatArguments: Array<Any>

    /**
     * @param pluralRes An Android resource ID.
     * @param quantity The number used to get the correct string for the language from the context
     *      used to resolving the string.
     * @param formatArguments Optional format arguments. Each argument can be of any type supported
     *      by [Parcel.writeValue].
     */
    constructor(@PluralsRes pluralRes: Int, quantity: Int, vararg formatArguments: Any) {
        this.resourceId = pluralRes
        this.quantity = quantity
        this.formatArguments = arrayOf(*formatArguments)
    }

    private constructor(parcel: Parcel) {
        resourceId = parcel.readInt()
        quantity = parcel.readInt()
        formatArguments = readFormattedArgumentsFromParcel(parcel)
    }

    override fun get(context: Context): String = when {
        formatArguments.isEmpty() -> context.resources.getQuantityString(resourceId, quantity)
        else -> context.resources.getQuantityString(
            resourceId,
            quantity,
            *formatArguments.map { (it as? StringResolver)?.get(context) ?: it }.toTypedArray()
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PluralResourceStringResolver

        if (resourceId != other.resourceId) return false
        if (quantity != other.quantity) return false
        if (!formatArguments.contentEquals(other.formatArguments)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = resourceId
        result = 31 * result + quantity
        result = 31 * result + formatArguments.contentHashCode()
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(resourceId)
        parcel.writeInt(quantity)
        parcel.writeInt(formatArguments.size)
        formatArguments.forEach {
            parcel.writeValue(it)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PluralResourceStringResolver> {
        override fun createFromParcel(parcel: Parcel): PluralResourceStringResolver {
            return PluralResourceStringResolver(parcel)
        }

        override fun newArray(size: Int): Array<PluralResourceStringResolver?> {
            return arrayOfNulls(size)
        }

        private fun readFormattedArgumentsFromParcel(parcel: Parcel): Array<Any> {
            val size = parcel.readInt()
            return Array(size) {
                parcel.readValue(PluralResourceStringResolver::class.java.classLoader)!!
            }
        }
    }
}
