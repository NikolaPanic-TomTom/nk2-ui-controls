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

package com.tomtom.nk2.core.common.uicontrols.progressbar

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import com.tomtom.nk2.core.common.animation.TimeTransition
import com.tomtom.nk2.core.common.animation.update

/**
 * A reusable smooth progress bar control that implements the progress bar required by UX.
 * https://app.zeplin.io/project/5d4bd0787d4f119b00da5646/screen/5ef99efcef873380676daf78
 *
 * @param context The display context.
 * @param attrs The attribute set provided in the layout XML file.
 */
class TtiviSmoothProgressBar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ProgressBar(context, attrs, defStyleAttr) {

    /**
     * Constructor that is called when inflating the control from XML.
     */
    constructor (context: Context, attrs: AttributeSet) : this(
        context,
        attrs,
        0
    )

    /**
     * Simple constructor to use when creating the control from code.
     */
    constructor (context: Context) : this(context, null, 0)

    private var progressAnimator = ObjectAnimator.ofInt(this, "progress", progress).apply {
        setAutoCancel(true)
        interpolator = LinearInterpolator()
    }

    var ttiviProgress: TimeTransition = TimeTransition(0.0, 0.0, 0, 0)
        set(value) {
            if (field != value) {
                field = value
                progressAnimator.update(
                    value,
                    fractionToProgress(ttiviProgress.startFraction),
                    fractionToProgress(ttiviProgress.endFraction)
                )
            }
        }

    private fun fractionToProgress(fraction: Double): Int {
        return min + (fraction * (max - min)).toInt()
    }
}
