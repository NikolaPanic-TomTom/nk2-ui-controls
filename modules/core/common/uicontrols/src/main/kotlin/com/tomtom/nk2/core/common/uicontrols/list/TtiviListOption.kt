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

import android.content.Context
import android.util.AttributeSet
import com.tomtom.nk2.core.common.uicontrols.R
import com.tomtom.nk2.core.common.uicontrols.databinding.TtiviListOptionItemBinding
import com.tomtom.nk2.core.common.uicontrols.view.TtiviInformationControlViewModel
import com.tomtom.nk2.core.common.uicontrols.view.TtiviListTableLayout

class TtiviListOption(context: Context, attrs: AttributeSet) :
    TtiviListTableLayout<TtiviInformationControlViewModel, TtiviListOptionItemBinding>(
        context,
        attrs,
        R.layout.ttivi_list_option_item
    ) {

    override fun bind(
        binding: TtiviListOptionItemBinding,
        model: TtiviInformationControlViewModel
    ) {
        binding.imageCornerRadius =
            R.attr.ttivi_information_control_rounded_image_corner_radius
        binding.viewModel = model
    }
}
