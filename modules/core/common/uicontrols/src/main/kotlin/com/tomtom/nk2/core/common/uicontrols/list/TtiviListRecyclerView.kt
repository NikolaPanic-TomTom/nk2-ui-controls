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

package com.tomtom.nk2.core.common.uicontrols.list

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import androidx.core.content.res.use
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomtom.nk2.api.common.resourceresolution.getFloatByAttr
import com.tomtom.nk2.core.common.uicontrols.R
import com.tomtom.nk2.core.common.uicontrols.view.requireContainerLifecycleOwner

/**
 * A reusable [RecyclerView] control that implements the List template required by UX.
 * https://app.zeplin.io/project/5d4bd0787d4f119b00da5646/screen/5ea9ddb3671a8d24306ff5e2
 *
 * @param context The display context.
 * @param attrs The attribute set provided in the layout XML file.
 * @param defStyleAttr The default style attribute set.
 */
@TargetApi(Build.VERSION_CODES.Q)
open class TtiviListRecyclerView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    RecyclerView(context, attrs, defStyleAttr) {

    /**
     * Constructor that is called when inflating the control from XML.
     */
    constructor (context: Context, attrs: AttributeSet) : this(
        context,
        attrs,
        R.attr.ttivi_list
    )

    /**
     * Simple constructor to use when creating the control from code.
     */
    constructor (context: Context) : this(context, null, R.attr.ttivi_list)

    var listItems: List<TtiviListItemViewModel>? = null
        set(value) {
            if (field != value) {
                field = value
                if (adapter == null) {
                    adapter = TtiviListViewAdapter(requireContainerLifecycleOwner())
                }
                (adapter as TtiviListViewAdapter).listItemViewModels = value!!
            }
        }

    private val stickyHeaderDecoration: TtiviListStickyHeaderDecoration by lazy {
        TtiviListStickyHeaderDecoration {
            adapter?.getItemViewType(it) == TtiviListItemViewModel.ListItemType.HEADER.ordinal
        }
    }

    private val stickyHeaderDataObserver: AdapterDataObserver by lazy {
        object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                stickyHeaderDecoration.invalidate()
            }
        }
    }

    var stickyHeader: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                if (field) {
                    addItemDecoration(stickyHeaderDecoration)
                    adapter?.registerAdapterDataObserver(stickyHeaderDataObserver)
                } else {
                    adapter?.unregisterAdapterDataObserver(stickyHeaderDataObserver)
                    removeItemDecoration(stickyHeaderDecoration)
                }
            }
        }

    private val topFadingEdgeStrengthFromStyle: Float by lazy {
        context.getFloatByAttr(R.attr.ttivi_fraction_list_top_fading_edge_strength)
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TtiviListRecyclerView,
            0, 0
        ).use {
            stickyHeader = it.getBoolean(R.styleable.TtiviListRecyclerView_ttiviStickyHeader, false)
        }
        layoutManager = LinearLayoutManager(context)
    }

    override fun getTopFadingEdgeStrength(): Float {
        return when {
            // When using sticky headers, the top should never be faded as it doesn't look right.
            stickyHeader -> INVISIBLE_FADING_EDGE_STRENGTH
            else -> super.getTopFadingEdgeStrength() * topFadingEdgeStrengthFromStyle
        }
    }

    /**
     * Fading effect should take into account the padding offset.
     */
    override fun isPaddingOffsetRequired() = true

    override fun getBottomPaddingOffset() = paddingBottom

    override fun onDraw(canvas: Canvas) {
        // When using sticky headers we must prevent the list bring drawn under the sticky header.
        if (stickyHeader) {
            canvas.clipOutRect(stickyHeaderDecoration.boundingRect)
        }
        super.onDraw(canvas)
    }

    companion object {
        /**
         * A fading edge strength of 0 prevents the sticky header from being drawn when scrolled all
         * the way to the bottom. We therefore use a non-zero but still invisible fading edge
         * strength instead to prevent requiring additional logic in [onDraw].
         */
        private const val INVISIBLE_FADING_EDGE_STRENGTH = 0.01f
    }
}
