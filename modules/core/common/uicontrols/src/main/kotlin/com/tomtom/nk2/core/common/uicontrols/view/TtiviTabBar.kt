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
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Observer
import com.tomtom.nk2.api.common.resourceresolution.getDimensionByAttr
import com.tomtom.nk2.core.common.animation.view.MovementSpringAnimation
import com.tomtom.nk2.core.common.uicontrols.R
import com.tomtom.nk2.core.common.uicontrols.databinding.TtiviTabBarBinding
import com.tomtom.nk2.core.common.uicontrols.databinding.TtiviTabBinding
import kotlin.math.roundToInt
import kotlin.properties.Delegates.observable

/**
 * This is a tab bar. It is used when you have a list of tabs and want to react to clicks on each
 * tab. To know when the tab selection changes, observe [TtiviTabBarViewModel.selectedTabIndex].
 *
 * Zeplin spec:
 * https://app.zeplin.io/project/5d4bd0787d4f119b00da5646/screen/5ea9ddb4c7760fbe772f8a4a
 */
class TtiviTabBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var ttiviTabType by observable(TtiviTab.TabType.TEXT_AND_ICON) { _, _, _ -> update() }

    var ttiviViewModel: TtiviTabBarViewModel?
        set(value) {
            binding.viewModel = value
            update()
        }
        get() = binding.viewModel

    /**
     * Handler for the tab switching events.
     */
    var onTabsSwitched: (() -> Unit)? = null

    private val binding: TtiviTabBarBinding

    private var widthPx by observable<Float?>(null) { _, oldValue, _ ->
        updateSelection(ttiviViewModel?.selectedTabIndex?.value ?: 0, oldValue != null)
    }

    init {
        val layoutInflater = context.getSystemService(LayoutInflater::class.java)
        binding = TtiviTabBarBinding.inflate(layoutInflater, this, true)

        context.obtainStyledAttributes(
            attrs, R.styleable.TtiviTabBar, defStyleAttr, 0
        ).use {
            ttiviTabType = TtiviTab.TabType.values()[
                it.getInteger(
                    R.styleable.TtiviTabBar_ttiviTabType,
                    TtiviTab.TabType.TEXT_AND_ICON.ordinal
                )
            ]
        }
    }

    private fun update() {
        // NOTE: Better for the TabBar to always have the proper height independently
        //  of whether it has tabs or not. Sometimes it will temporarily have zero
        //  tabs then we will see it get squished and back which is not nice.
        //  Plus other components might want to base their height upon the TabBar height
        //  such as in the Media Player. In that case they will get temporarily squished
        //  as well. We avoid that by making sure the TabBar always has the proper
        //  height here.
        binding.root.updateLayoutParams {
            this.height = when (ttiviTabType) {
                TtiviTab.TabType.ICON_ONLY,
                TtiviTab.TabType.TEXT_ONLY ->
                    context.getDimensionByAttr(
                        R.attr.ttivi_size_tab_bar_text_only_or_icon_only_height
                    ).toInt()
                TtiviTab.TabType.TEXT_AND_ICON ->
                    context.getDimensionByAttr(
                        R.attr.ttivi_size_task_panel_top_bar_height
                    ).toInt()
            }
        }

        val tabsContainer = binding.ttiviTabContainer
        tabsContainer.removeAllViews()
        ttiviViewModel?.apply {
            val lifecycleOwner = requireContainerLifecycleOwner()
            tabViewModels.observe(lifecycleOwner, Observer { tabs ->
                tabsContainer.removeAllViews()
                if (tabs.isNotEmpty()) {
                    binding.lifecycleOwner = lifecycleOwner

                    val tabWeight = 1.0f / tabs.size
                    val layoutInflater = context.getSystemService(LayoutInflater::class.java)
                    for ((tabIndex, tabViewModel) in tabs.withIndex()) {
                        TtiviTabBinding.inflate(layoutInflater).also { binding ->
                            binding.ttiviTab.let { tabView ->
                                tabView.ttiviTabType = ttiviTabType
                                tabView.setOnClickListener { selectTab(tabIndex) }

                                tabsContainer.addView(tabView,
                                    LayoutParams(0, LayoutParams.WRAP_CONTENT)
                                        .apply { weight = tabWeight }
                                )
                            }
                            binding.lifecycleOwner = lifecycleOwner
                            binding.viewModel = tabViewModel
                        }
                    }

                    selectedTabIndex.observe(lifecycleOwner, Observer { tabIndex ->
                        updateSelection(tabIndex, true)
                    })
                }
            })
        }
    }

    private fun updateSelection(tabIndex: Int, animate: Boolean) {
        ttiviViewModel?.apply {
            tabViewModels.value?.apply {
                map { it.isSelected.value = false }
                if (isNotEmpty()) {
                    get(tabIndex).isSelected.value = true
                }
            }

            widthPx?.let {
                val padding = context.getDimensionByAttr(R.attr.ttivi_size_tab_bar_padding)
                val contentWidthPx = it - 2 * padding

                val indicator = binding.ttiviTabIndicator

                val tabCount = tabViewModels.value!!.size
                if (tabCount > 0) {
                    val indicatorWidthPx = contentWidthPx / tabCount.toFloat()

                    val indicatorWidthPxRounded = indicatorWidthPx.roundToInt()
                    if (indicator.width != indicatorWidthPxRounded) {
                        indicator.updateLayoutParams {
                            this.width = indicatorWidthPxRounded
                        }
                    }

                    val finalPositionPx = tabIndex * indicatorWidthPx + padding
                    if (animate && indicator.x > 0) {
                        MovementSpringAnimation(
                            indicator,
                            MovementSpringAnimation.Direction.HORIZONTAL,
                            false
                        ).animateToPosition(finalPositionPx)
                    } else {
                        indicator.x = finalPositionPx
                    }
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthPx = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        if (this.widthPx != widthPx && widthPx > 0) {
            this.widthPx = widthPx
        }
    }

    fun selectTab(tabIndex: Int) {
        if (ttiviViewModel?.selectedTabIndex?.value != tabIndex) {
            onTabsSwitched?.invoke()
        }
        ttiviViewModel?.apply { selectedTabIndex.value = tabIndex }
    }
}
