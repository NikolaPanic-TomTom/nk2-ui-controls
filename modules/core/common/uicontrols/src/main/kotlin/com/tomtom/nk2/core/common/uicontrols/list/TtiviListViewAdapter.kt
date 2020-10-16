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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.tomtom.nk2.core.common.uicontrols.R
import com.tomtom.nk2.core.common.uicontrols.databinding.TtiviInformationControlLayoutBinding
import com.tomtom.nk2.core.common.uicontrols.databinding.TtiviListHeaderBinding

/**
 * A [RecyclerView.Adapter] used by [TtiviListRecyclerView] to create item Views and bind them to
 * the [TtiviListItemViewModel] depending on the item type:
 * - For content area [ContentAreaViewHolder] is created.
 * - For headers [HeaderViewHolder] is created.
 */
class TtiviListViewAdapter(
    private var lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listItemViewModels: List<TtiviListItemViewModel>? = null
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (TtiviListItemViewModel.ListItemType.values()[viewType]) {
            TtiviListItemViewModel.ListItemType.CONTENT -> ContentAreaViewHolder(
                TtiviInformationControlLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            TtiviListItemViewModel.ListItemType.HEADER -> HeaderViewHolder(
                TtiviListHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContentAreaViewHolder -> with(listItemViewModels!![position]) {
                if (this is TtiviListItemContentAreaViewModel) {
                    holder.binding.viewModel = this
                    holder.binding.imageCornerRadius =
                        R.attr.ttivi_information_control_rounded_image_corner_radius
                    holder.binding.lifecycleOwner = lifecycleOwner
                }
            }
            is HeaderViewHolder -> with(listItemViewModels!![position]) {
                if (this is TtiviListItemHeaderViewModel) {
                    holder.binding.viewModel = this
                    holder.binding.lifecycleOwner = lifecycleOwner
                }
            }
        }
    }

    override fun getItemCount() = listItemViewModels?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        return listItemViewModels!![position].type.ordinal
    }

    abstract class AbstractViewHolder<T : ViewDataBinding>(val binding: T) :
        RecyclerView.ViewHolder(binding.root)

    inner class HeaderViewHolder(binding: TtiviListHeaderBinding) :
        AbstractViewHolder<TtiviListHeaderBinding>(binding)

    inner class ContentAreaViewHolder(binding: TtiviInformationControlLayoutBinding) :
        AbstractViewHolder<TtiviInformationControlLayoutBinding>(binding)
}
