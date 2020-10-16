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
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.res.use
import com.tomtom.nk2.core.common.uicontrols.R
import com.tomtom.nk2.core.common.uicontrols.databinding.TtiviTabViewBinding
import com.tomtom.nk2.core.common.uicontrols.databinding.TtiviTabViewIconOnlyBinding
import com.tomtom.nk2.core.common.uicontrols.databinding.TtiviTabViewTextOnlyBinding
import kotlin.properties.Delegates.observable

/**
 * This is a tab. It is used by [TtiviTabBar].
 * It is intended to be used through [TtiviTabBar] and/or [TtiviTabFragmentPager] and setting data
 * via their view-models and not for direct use.
 */
class TtiviTab @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    /**
     * [TabType] reflects the attribute enum [ttiviTabType] in the file attrs.xml. Keep this
     * file up to date when changing this enum.
     */
    enum class TabType {
        TEXT_AND_ICON,
        ICON_ONLY,
        TEXT_ONLY,
    }

    var ttiviTabType by observable(TabType.TEXT_AND_ICON) { _, _, _ -> update() }

    var ttiviViewModel by observable<TtiviTabViewModel?>(null) { _, _, _ -> update() }

    init {
        context.obtainStyledAttributes(
            attrs, R.styleable.TtiviTab, defStyleAttr, 0
        ).use {
            ttiviTabType = TabType.values()[
                it.getInteger(
                    R.styleable.TtiviTab_ttiviTabType,
                    TabType.TEXT_AND_ICON.ordinal
                )
            ]
        }
    }

    private fun update() {
        removeAllViews()
        ttiviViewModel?.let {
            val layoutInflater = context.getSystemService(LayoutInflater::class.java)

            val tabBinding = when (ttiviTabType) {
                TabType.TEXT_ONLY -> TtiviTabViewTextOnlyBinding
                    .inflate(layoutInflater, this, true)
                    .apply { viewModel = it }
                TabType.ICON_ONLY -> TtiviTabViewIconOnlyBinding
                    .inflate(layoutInflater, this, true)
                    .apply { viewModel = it }
                TabType.TEXT_AND_ICON -> TtiviTabViewBinding
                    .inflate(layoutInflater, this, true)
                    .apply { viewModel = it }
            }

            tabBinding.lifecycleOwner = requireContainerLifecycleOwner()
        }
    }
}
