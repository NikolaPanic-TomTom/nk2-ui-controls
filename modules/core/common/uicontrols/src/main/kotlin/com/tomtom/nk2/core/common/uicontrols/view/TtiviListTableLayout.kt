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
import android.widget.TableLayout
import androidx.annotation.LayoutRes
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * A generic [TableLayout] that can present UI elements which are dynamically added via a
 * view model. By inheriting this class, custom logic related to the manipulation of items inside
 * the layout, can be stored inside the dedicated class which makes it possible to test the logic in
 * isolation.
 * Note: The binding of this class need to be set in order to set the life cycle owner for the view
 * items in the list.
 * @param T The type of view model which we need to visualize the items in the [TableLayout].
 * @param Binding The specialization of the [ViewDataBinding] for inflating the view with a
 * viewmodel [T].
 * @param context The application context.
 * @param attrs The attribute set provided in the layout XML file.
 * @param layoutId The identifier of the layout for a single item in the list.
 */
abstract class TtiviListTableLayout<T, Binding : ViewDataBinding>(
    context: Context,
    attrs: AttributeSet?,
    @LayoutRes private val layoutId: Int
) :
    TableLayout(context, attrs) {

    /**
     * A generic store for holding the view models related to the content of the [TableLayout].
     */
    var contents: List<T>? = emptyList()
        set(value) {
            if (field != value) {
                field = value
                value?.let {
                    if (childCount != it.size) {
                        rebuildLayout(it)
                    } else {
                        rebindViews(it)
                    }
                } ?: removeAllViews()
            }
        }

    private fun rebuildLayout(value: List<T>) {
        removeAllViews()
        value.forEach { viewModel ->
            buildView(viewModel)
        }
    }

    private fun rebindViews(value: List<T>) {
        children.forEachIndexed { index, view ->
            bind(DataBindingUtil.findBinding<Binding>(view)!!, value[index])
        }
    }

    /**
     * Create and bind a new view for the given [model].
     */
    private fun buildView(model: T) {
        DataBindingUtil.inflate<Binding>(LayoutInflater.from(context), layoutId, this, true)
            .let { bindingNewViewEntry ->
                bind(bindingNewViewEntry, model)
                bindingNewViewEntry.lifecycleOwner =
                    DataBindingUtil.findBinding<ViewDataBinding>(this)!!.lifecycleOwner
            }
    }

    /**
     * Binds the [model] to the given [binding].
     * Needs to be implemented by the specialization which knows the members of the dedicated binder
     * class.
     */
    abstract fun bind(binding: Binding, model: T)
}
