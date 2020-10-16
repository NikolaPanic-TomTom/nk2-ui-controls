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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tomtom.nk2.core.common.uicontrols.databinding.TtiviBreadcrumbsBarItemLayoutBinding
import com.tomtom.nk2.core.common.uicontrols.view.requireContainerLifecycleOwner

/**
 * RecyclerView's List adapter for Breadcrumbs Bar view.
 */
class TtiviBreadcrumbsBarAdapter :
    RecyclerView.Adapter<TtiviBreadcrumbsBarAdapter.ItemViewHolder>() {
    var itemViewModels: List<TtiviBreadcrumbsBarItemViewModel> = emptyList()
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            TtiviBreadcrumbsBarItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).apply {
                lifecycleOwner = parent.requireContainerLifecycleOwner()
            }
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.viewModel = itemViewModels.getOrNull(position)
        holder.binding.isFirstItem = position == itemViewModels.indices.first()
        holder.binding.isLastItem = position == itemViewModels.indices.last()
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = itemViewModels.size

    class ItemViewHolder(val binding: TtiviBreadcrumbsBarItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}
