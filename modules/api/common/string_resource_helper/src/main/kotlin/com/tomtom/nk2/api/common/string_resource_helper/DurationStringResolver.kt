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
import java.time.Duration
import kotlinx.android.parcel.Parcelize

/**
 * A [StringResolver] that takes a duration and resolves that to a [String] representation of the
 * duration (e.g. `h:mm:ss` for English) in which the hour component is only added when it is
 * non-zero.
 */
@Parcelize
data class DurationStringResolver(private val duration: Duration) : StringResolver {

    override fun get(context: Context): String {
        val hours = duration.toHours()
        val minutes = duration.minusHours(hours).toMinutes()
        val seconds = duration.minusHours(hours).minusMinutes(minutes).seconds

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds)
        }

        return String.format("%02d:%02d", minutes, seconds)
    }
}
