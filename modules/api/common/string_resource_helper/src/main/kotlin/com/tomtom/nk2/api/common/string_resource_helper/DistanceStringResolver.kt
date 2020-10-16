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
import android.icu.text.DecimalFormat
import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import com.tomtom.nk2.core.common.geography.CountryId
import com.tomtom.nk2.core.common.math.roundToNearest
import java.util.Locale
import javax.measure.quantity.Length
import javax.measure.unit.NonSI.FOOT
import javax.measure.unit.NonSI.MILE
import javax.measure.unit.NonSI.YARD
import javax.measure.unit.SI.KILOMETER
import javax.measure.unit.SI.METER
import kotlinx.android.parcel.Parcelize
import org.jscience.physics.amount.Amount

/**
 * A [StringResolver] that takes a distance expressed in meters and resolves it to the appropriate
 * [String] representation according to the given [Context].
 */
@Parcelize
data class DistanceStringResolver(private val meters: Int) : StringResolver {
    override fun get(context: Context): String {
        val currentLocale = context.resources.configuration.locales[0]
        val countryId = CountryId.getCountryId(currentLocale.isO3Country)

        val distanceInMeters = Amount.valueOf(meters.toDouble(), METER)
        val distance = when {
            countryId == null -> distanceInMeters.toMetersOrKilometers()
            countryId.usesMilesAndFeet() -> distanceInMeters.toMilesOrFeet()
            countryId.usesMilesAndYards() -> distanceInMeters.toMilesOrYards()
            else -> distanceInMeters.toMetersOrKilometers()
        }

        return formatDistance(distance, currentLocale)
    }

    private fun Amount<Length>.toMetersOrKilometers(): Amount<Length> =
        when {
            this < TEN_METER_STEPS_MIN_VALUE -> Amount.valueOf(0, METER)
            // 10 meter steps
            roundToNearest(10.0).lessThanWithEpsilon(HUNDRED_METER_STEPS_MIN_VALUE) ->
                roundToNearest(10.0)
            // 100 meter steps
            roundToNearest(100.0).lessThanWithEpsilon(TENTH_KM_STEPS_MIN_VALUE) ->
                roundToNearest(100.0)
            // 0.1 km steps
            this.lessThanWithEpsilon(KM_STEPS_MIN_VALUE) ->
                roundToNearest(100.0).to(KILOMETER)
            // 1 km steps
            else -> roundToNearest(1000.0).to(KILOMETER)
        }

    private fun Amount<Length>.toMilesOrFeet() = toFeet() ?: toMiles()

    private fun Amount<Length>.toMilesOrYards() = toYards() ?: toMiles()

    private fun Amount<Length>.toYards(): Amount<Length>? =
        to(YARD).let {
            when {
                it.lessThanWithEpsilon(TEN_YARD_STEPS_FOR_YARDS_MIN_VALUE) ->
                    Amount.valueOf(0.0, YARD)
                // 10 yard steps
                it.lessThanWithEpsilon(HUNDRED_YARD_STEPS_FOR_YARDS_MIN_VALUE) ->
                    it.roundToNearest(10.0)
                // 100 yard steps
                it.lessThanWithEpsilon(MILE_STEPS_FOR_YARDS_MIN_VALUE) ->
                    it.roundToNearest(100.0)
                else ->
                    null
            }
        }

    private fun Amount<Length>.toFeet(): Amount<Length>? =
        to(FOOT).let {
            when {
                it.lessThanWithEpsilon(TEN_FOOT_STEPS_FOR_FEET_MIN_VALUE) ->
                    Amount.valueOf(0.0, FOOT)
                it.lessThanWithEpsilon(HUNDRED_FOOT_STEPS_FOR_FEET_MIN_VALUE) ->
                    it.roundToNearest(10.0)
                else -> it.roundToNearest(100.0)
                    .takeIf { it.lessThanWithEpsilon(MILE_STEPS_FOR_FEET_MIN_VALUE) }
            }
        }

    private fun Amount<Length>.toMiles(): Amount<Length> {
        val distanceInMiles = to(MILE)
        return when {
            // 0.1 mile steps
            distanceInMiles.lessThanWithEpsilon(SHOW_DECIMALS_FOR_MILES_MAX_VALUE) ->
                distanceInMiles.roundToNearest(0.1)
            // 1 mile steps
            else -> distanceInMiles.roundToNearest(1.0)
        }
    }

    /**
     * Helper function to perform less than operation on [Double] values.
     * Values are considered equal if the difference between them is at most [epsilon].
     * For instance, 9.99999 is considered to be equal to 10.
     */
    private fun Amount<Length>.lessThanWithEpsilon(
        other: Amount<Length>,
        epsilon: Double = 0.0001
    ): Boolean {
        val otherValue = other.to(unit).estimatedValue
        return (estimatedValue - otherValue) < -epsilon
    }

    private fun formatDistance(distance: Amount<Length>, locale: Locale): String {
        if (distance.estimatedValue == 0.0) {
            return ""
        }

        return when (distance.unit) {
            METER -> formatMeters(distance, locale)
            KILOMETER -> formatKilometers(distance, locale)
            MILE -> formatMiles(distance, locale)
            YARD -> formatYards(distance, locale)
            FOOT -> formatFeet(distance, locale)
            else -> throw Exception("Unexpected unit: ${distance.unit}")
        }
    }

    private fun formatKilometers(distance: Amount<Length>, locale: Locale): String =
        formatWithMaximumFractionDigits(
            locale,
            distance.estimatedValue,
            MeasureUnit.KILOMETER,
            if (distance < SHOW_DECIMALS_FOR_KM_MAX_VALUE) 1 else 0
        )

    private fun formatMeters(distance: Amount<Length>, locale: Locale): String =
        formatWithMaximumFractionDigits(
            locale,
            distance.estimatedValue,
            MeasureUnit.METER
        )

    private fun formatFeet(distance: Amount<Length>, locale: Locale): String =
        formatWithMaximumFractionDigits(
            locale,
            distance.estimatedValue,
            MeasureUnit.FOOT
        )

    private fun formatYards(distance: Amount<Length>, locale: Locale): String =
        formatWithMaximumFractionDigits(
            locale,
            distance.estimatedValue,
            MeasureUnit.YARD
        )

    private fun formatMiles(distance: Amount<Length>, locale: Locale): String =
        formatWithMaximumFractionDigits(
            locale,
            distance.estimatedValue,
            MeasureUnit.MILE,
            if (distance < SHOW_DECIMALS_FOR_MILES_MAX_VALUE) 1 else 0
        )

    private fun formatWithMaximumFractionDigits(
        locale: Locale,
        value: Double,
        measureUnit: MeasureUnit,
        numberOfFractionDigits: Int = 0
    ): String {
        val numberFormat = DecimalFormat.getNumberInstance(locale).apply {
            maximumFractionDigits = numberOfFractionDigits
        }
        val formatter =
            MeasureFormat.getInstance(locale, MeasureFormat.FormatWidth.SHORT, numberFormat)
        return formatter.format(Measure(value, measureUnit))
    }

    private fun Amount<Length>.roundToNearest(step: Double): Amount<Length> =
        Amount.valueOf(roundToNearest(estimatedValue, step), unit)
}

private val TEN_METER_STEPS_MIN_VALUE = Amount.valueOf(10, METER)
private val HUNDRED_METER_STEPS_MIN_VALUE = Amount.valueOf(500, METER)
private val TENTH_KM_STEPS_MIN_VALUE = Amount.valueOf(1000, METER)
private val KM_STEPS_MIN_VALUE = Amount.valueOf(10000, METER)

private val TEN_YARD_STEPS_FOR_YARDS_MIN_VALUE = Amount.valueOf(10.0, YARD)
private val HUNDRED_YARD_STEPS_FOR_YARDS_MIN_VALUE = Amount.valueOf(500.0, YARD)
private val MILE_STEPS_FOR_YARDS_MIN_VALUE = Amount.valueOf(850.0, YARD)

private val TEN_FOOT_STEPS_FOR_FEET_MIN_VALUE = Amount.valueOf(30, FOOT)
private val HUNDRED_FOOT_STEPS_FOR_FEET_MIN_VALUE = Amount.valueOf(500, FOOT)
private val MILE_STEPS_FOR_FEET_MIN_VALUE = Amount.valueOf(1000, FOOT)

private val SHOW_DECIMALS_FOR_MILES_MAX_VALUE = Amount.valueOf(10, MILE)

private val SHOW_DECIMALS_FOR_KM_MAX_VALUE = Amount.valueOf(10, KILOMETER)
