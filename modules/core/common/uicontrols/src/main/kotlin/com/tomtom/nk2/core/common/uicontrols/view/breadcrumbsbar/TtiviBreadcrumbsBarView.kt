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

package com.tomtom.nk2.core.common.uicontrols.view.breadcrumbsbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.tomtom.nk2.api.common.resourceresolution.drawable.DrawableResolver
import com.tomtom.nk2.api.common.string_resource_helper.StringResolver
import com.tomtom.nk2.core.common.uicontrols.R
import com.tomtom.nk2.core.common.uicontrols.databinding.TtiviBreadcrumbsBarLayoutBinding
import com.tomtom.nk2.core.common.uicontrols.view.requireContainerLifecycleOwner

/**
 * A view that shows a back stack in the form of breadcrumbs bar view. It allows clicking items in
 * order to navigate through the back stack. The back button is always displayed, while other
 * components are optional. When there is only one element, it will be presented through it's group
 * label rather than the item's individual label.
 */
class TtiviBreadcrumbsBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.TtiviBreadcrumbsBar
) : LinearLayout(context, attrs, defStyleAttr) {

    private val breadcrumbsBarAdapter = TtiviBreadcrumbsBarAdapter()

    private val binding =
        TtiviBreadcrumbsBarLayoutBinding.inflate(LayoutInflater.from(context), this, true)
            .apply {
                onBackButtonClickListener = OnClickListener { onBackButtonClicked?.invoke() }

                with(ttiviBreadcrumbsRouteList) {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = breadcrumbsBarAdapter
                }
            }

    var items: List<BreadcrumbsBarItem> = emptyList()
        set(value) {
            if (value != field) {
                require(value.map { it.id }.toSet().size == value.size) {
                    "Breadcrumbs item indices must be unique."
                }

                field = value

                val rootItem = value.firstOrNull()
                binding.rootIcon = rootItem?.icon
                binding.rootLabel = rootItem?.groupLabel

                // Do not show breadcrumbs when we only have one element.
                binding.showBreadcrumbs = value.size > 1

                breadcrumbsBarAdapter.itemViewModels =
                    value.map { item ->
                        TtiviBreadcrumbsBarItemViewModel(item.label, item.icon) {
                            onItemClicked?.invoke(item)
                        }
                    }

                binding.executePendingBindings()
            }
        }

    /**
     * Called when an item in the breadcrumbs bar is clicked.
     */
    var onItemClicked: ((BreadcrumbsBarItem) -> Unit)? = null

    /**
     * Handler for the back button's click events.
     */
    var onBackButtonClicked: (() -> Unit)? = null

    init {
        when (id) {
            R.id.ttivi_breadcrumbs_bar -> {
                // ID already set, nothing to be done.
            }
            NO_ID -> {
                id = R.id.ttivi_breadcrumbs_bar
            }
            else -> {
                error(
                    """A breadcrumbs view must have the ID ttivi_breadcrumbs_bar in order to be
                    recognised and populated by the system UI. The ID may be omitted to allow it to 
                    be set automatically, but may not be set to a different value.""".trimIndent()
                )
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (binding.lifecycleOwner == null) {
            binding.lifecycleOwner = requireContainerLifecycleOwner()
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    data class BreadcrumbsBarItem(
        val id: Int,
        val label: LiveData<StringResolver>,
        val groupLabel: LiveData<StringResolver>?,
        val icon: LiveData<DrawableResolver?>?
    )
}
