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

package com.tomtom.nk2.core.common.math

import java.time.Duration

private const val INTEGER_BASED_ROUNDING_MAXIMUM = Int.MAX_VALUE
private const val INTEGER_BASED_ROUNDING_MINIMUM = 0
private val DOUBLE_BASED_ROUNDING_MAXIMUM = Double.MAX_VALUE
private const val DOUBLE_BASED_ROUNDING_MINIMUM = 0.0

/**
 * A helper function to round integer [value] given a [step] size.
 * For example rounding 40 with step 60 would round up and return 60.
 */
fun roundToNearest(value: Int, step: Int): Int {
    if (value == INTEGER_BASED_ROUNDING_MINIMUM) {
        return INTEGER_BASED_ROUNDING_MINIMUM
    }

    require(value in INTEGER_BASED_ROUNDING_MINIMUM..INTEGER_BASED_ROUNDING_MAXIMUM)
    require(step > INTEGER_BASED_ROUNDING_MINIMUM)

    val remainder: Int = value % step
    val rounding = if (2 * remainder >= step) step else 0
    return value - remainder + rounding
}

/**
 * A helper function to round double [value] given a [step] size.
 * For example rounding 1.45 with step 0.1 would round up and return 1.5.
 */
fun roundToNearest(value: Double, step: Double): Double {
    if (value == DOUBLE_BASED_ROUNDING_MINIMUM) {
        return DOUBLE_BASED_ROUNDING_MINIMUM
    }

    require(value in DOUBLE_BASED_ROUNDING_MINIMUM..DOUBLE_BASED_ROUNDING_MAXIMUM)
    require(step > DOUBLE_BASED_ROUNDING_MINIMUM)

    val bigValue = value.toBigDecimal()
    val bigStep = step.toBigDecimal()

    val remainder = bigValue.remainder(bigStep).toDouble()
    val rounding = if (2 * remainder >= step) step else 0.0
    return value - remainder + rounding
}

/**
 * A helper function to round duration [value] given a [step] size.
 * For example rounding 1 hour 30 minutes with step 1 hour would round up and return 2 hours.
 */
fun roundToNearest(value: Duration, step: Duration): Duration {
    if (value == Duration.ZERO) {
        return Duration.ZERO
    }

    require(value >= Duration.ZERO)
    require(step >= Duration.ofMillis(1))

    val remainder = Duration.ofMillis(value.toMillis() % step.toMillis())
    val rounding = if (remainder.multipliedBy(2) >= step) step else Duration.ZERO
    return value - remainder + rounding
}
