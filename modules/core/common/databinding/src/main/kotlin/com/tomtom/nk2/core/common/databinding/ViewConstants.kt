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

package com.tomtom.nk2.core.common.databinding

import android.view.View

/**
 * This class provides access to hidden constants in [View].
 * These are useful when such hidden constants are needed to be set based on data bindings inside
 * XML files.
 */
class ViewConstants private constructor() {
    companion object {
        const val FADING_EDGE_NONE = 0
        const val FADING_EDGE_HORIZONTAL = 1
        const val FADING_EDGE_VERTICAL = 2
    }
}
