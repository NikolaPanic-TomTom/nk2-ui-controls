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

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import com.tomtom.nk2.core.common.uicontrols.R

/**
 * A reusable [ScrollView] control that implements a scrollable view UX specification with fading
 * bottom edge.
 * https://zpl.io/2p64El8
 *
 * @param context The display context.
 * @param attrs The attribute set provided in the layout XML file.
 * @param defStyleAttr The default style attribute set.
 */
class TtiviScrollView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ScrollView(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null, R.attr.ttivi_scroll_view)
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.attr.ttivi_scroll_view
    )

    /**
     * Disables fading for top edge.
     */
    override fun getTopFadingEdgeStrength() = 0.0F
}
