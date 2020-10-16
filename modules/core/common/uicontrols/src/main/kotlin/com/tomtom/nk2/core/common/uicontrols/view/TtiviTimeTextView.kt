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

package com.tomtom.nk2.core.common.uicontrols.view

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.core.content.res.use
import com.tomtom.nk2.api.common.string_resource_helper.MinuteSecondStringResolver
import com.tomtom.nk2.core.common.animation.TimeTransition
import com.tomtom.nk2.core.common.animation.update
import com.tomtom.nk2.core.common.uicontrols.R
import com.tomtom.nk2.core.common.uicontrols.view.TtiviTimeTextView.TimeDisplayMode.DURATION
import com.tomtom.nk2.core.common.uicontrols.view.TtiviTimeTextView.TimeDisplayMode.ELAPSED
import com.tomtom.nk2.core.common.uicontrols.view.TtiviTimeTextView.TimeDisplayMode.REMAINING
import java.time.Duration
import kotlin.math.roundToLong

/**
 * A reusable UI control that is used for displaying time text labels with different
 * [ttiviTimeDisplayMode].
 *
 * @param context The display context.
 * @param attrs The attribute set provided in the layout XML file.
 */
class TtiviTimeTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    TtiviTextView(context, attrs, defStyleAttr) {

    /**
     * Constructor that is called when inflating the control from XML.
     */
    constructor(context: Context, attributeSet: AttributeSet) : this(
        context, attributeSet, 0
    )

    /**
     * Simple constructor to use when creating the control from code.
     */
    constructor (context: Context) : this(context, null, 0)

    /**
     * [TimeDisplayMode] reflects the attribute enum [ttiviTimeDisplayMode] in the file attrs.xml.
     * Keep this file up to date when changing this enum.
     *
     * [ELAPSED] mode display the elapsed time.
     * [REMAINING] mode display the remaining time.
     * [DURATION] mode display the total duration time.
     */
    enum class TimeDisplayMode {
        ELAPSED,
        REMAINING,
        DURATION
    }

    /**
     * Total time duration in milliseconds.
     */
    var ttiviTimeDurationMs = 0L
        set(value) {
            if (field != value) {
                field = value
                valueAnimator.update(
                    ttiviProgress,
                    fractionToTime(ttiviProgress.startFraction),
                    fractionToTime(ttiviProgress.endFraction)
                )
            }
        }

    /**
     * Time display mode see [TimeDisplayMode].
     */
    var ttiviTimeDisplayMode = ELAPSED
        set(value) {
            if (field != value) {
                field = value
                valueAnimator.update(
                    ttiviProgress,
                    fractionToTime(ttiviProgress.startFraction),
                    fractionToTime(ttiviProgress.endFraction)
                )
            }
        }

    /**
     * Time progress.
     */
    var ttiviProgress: TimeTransition = TimeTransition(0.0, 1.0, 0, 0)
        set(value) {
            if (field != value) {
                field = value
                valueAnimator.update(
                    field,
                    fractionToTime(ttiviProgress.startFraction),
                    fractionToTime(ttiviProgress.endFraction)
                )
            }
        }

    private var valueAnimator = ValueAnimator.ofObject(LongEvaluator(), 0L, 0L).apply {
        addUpdateListener {
            updateDisplayedTime()
        }
        interpolator = LinearInterpolator()
    }

    private class LongEvaluator : TypeEvaluator<Long> {
        override fun evaluate(fraction: Float, startValue: Long, endValue: Long): Long {
            return startValue + ((endValue - startValue) * fraction).roundToLong()
        }
    }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.TtiviTimeTextView, defStyleAttr, 0).use {
            ttiviTimeDisplayMode = TimeDisplayMode.values()[
                it.getInteger(
                    R.styleable.TtiviTimeTextView_ttiviTimeDisplayMode,
                    ELAPSED.ordinal
                )
            ]
        }
    }

    private fun updateDisplayedTime() {
        val elapsedTimeMs = (valueAnimator.animatedValue ?: 0L) as Long
        val displayTimeMs = when (ttiviTimeDisplayMode) {
            ELAPSED -> elapsedTimeMs
            REMAINING -> ttiviTimeDurationMs - elapsedTimeMs
            DURATION -> ttiviTimeDurationMs
        }
        text = MinuteSecondStringResolver(Duration.ofMillis(displayTimeMs)).get(context)
    }

    private fun fractionToTime(fraction: Double) = (fraction * ttiviTimeDurationMs).roundToLong()
}
