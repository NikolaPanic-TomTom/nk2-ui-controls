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

package com.tomtom.nk2.core.common.animation

import android.view.View
import android.view.animation.Animation
import androidx.databinding.BindingAdapter
import com.tomtom.nk2.core.common.util.setAnimationEndListener

/**
 * Animates changes to visibility. For this to work, both [enterAnimation] and [exitAnimation] must
 * be specified. When the view becomes [View.VISIBLE], [enterAnimation] is played. When the view
 * becomes [View.INVISIBLE] or [View.GONE], [exitAnimation] is played.
 *
 * Example usage:
 * ```
 * <View
 *     android:visibility="@{viewModel.isViewVisible ? View.VISIBLE : View.GONE}"
 *     auto:enterAnimation="@{@anim/ttivi_enter_slideup}"
 *     auto:exitAnimation="@{@anim/ttivi_exit_slidedown}" />
 * ```
 */
@BindingAdapter("android:visibility", "enterAnimation", "exitAnimation")
fun View.animateVisibility(
    newVisibility: Int,
    enterAnimation: Animation,
    exitAnimation: Animation
) {
    val previouslyTaggedVisibility = getTag(R.id.ttivi_animate_visibility_tag)
    setTag(R.id.ttivi_animate_visibility_tag, newVisibility)

    // Don't animate the very first time we're setting a value.
    if (previouslyTaggedVisibility == null) {
        visibility = newVisibility
        return
    }

    when (newVisibility) {
        previouslyTaggedVisibility -> {
            // Don't do anything when the visibility didn't change.
            return
        }
        View.VISIBLE -> {
            visibility = newVisibility
            startAnimation(enterAnimation)
        }
        else -> {
            if (visibility != View.VISIBLE) {
                // No need to animate the view if it wasn't visible to begin with.
                visibility = newVisibility
                return
            }
            exitAnimation.setAnimationEndListener { visibility = newVisibility }
            startAnimation(exitAnimation)
        }
    }
}

/**
 * Animates changes to visibility that includes helper logic for setting visibility of a view based
 * on whether a condition is satisfied. For this to work, both [enterAnimation] and [exitAnimation]
 * must be specified. When the condition is satisfied, the visibility will be [View.VISIBLE] and
 * [enterAnimation] is played. When the condition is no longer satisfied, [exitAnimation] is played
 * and the visibility will be set to [View.GONE].
 *
 * Example usage:
 * ```
 * <View
 *     auto:visibleIf="@{viewModel.condition}"
 *     auto:enterAnimation="@{@anim/ttivi_enter_slideup}"
 *     auto:exitAnimation="@{@anim/ttivi_exit_slidedown}" />
 * ```
 */
@BindingAdapter("visibleIf", "enterAnimation", "exitAnimation")
fun View.animateVisibleIf(
    condition: Boolean,
    enterAnimation: Animation,
    exitAnimation: Animation
) = animateVisibility(
    when (condition) {
        true -> View.VISIBLE
        false -> View.GONE
    },
    enterAnimation,
    exitAnimation
)

/**
 * Animates changes to visibility that includes helper logic for setting visibility of a view based
 * on whether a condition is satisfied. For this to work, both [enterAnimation] and [exitAnimation]
 * must be specified. When the condition is unsatisfied, the visibility will be [View.VISIBLE] and
 * [enterAnimation] is played. When the condition becomes satisfied, [exitAnimation] is played and
 * the visibility will be set to [View.GONE].
 *
 * Example usage:
 * ```
 * <View
 *     auto:visibleIfNot="@{viewModel.condition}"
 *     auto:enterAnimation="@{@anim/ttivi_enter_slideup}"
 *     auto:exitAnimation="@{@anim/ttivi_exit_slidedown}" />
 * ```
 */
@BindingAdapter("visibleIfNot", "enterAnimation", "exitAnimation")
fun View.animateVisibleIfNot(
    condition: Boolean,
    enterAnimation: Animation,
    exitAnimation: Animation
) = animateVisibleIf(!condition, enterAnimation, exitAnimation)

/**
 * Animates changes to visibility that includes helper logic for setting visibility of a view based
 * on whether an instance is `null`. For this to work, both [enterAnimation] and [exitAnimation]
 * must be specified. When the instance is not `null`, the visibility will be [View.VISIBLE] and
 * [enterAnimation] is played. When the instance is `null`, [exitAnimation] is played and the
 * visibility will be set to [View.GONE].
 *
 * Example usage:
 * ```
 * <View
 *     auto:visibleIfNotNull="@{viewModel.instance}"
 *     auto:enterAnimation="@{@anim/ttivi_enter_slideup}"
 *     auto:exitAnimation="@{@anim/ttivi_exit_slidedown}" />
 * ```
 */
@BindingAdapter("visibleIfNotNull", "enterAnimation", "exitAnimation")
fun View.animateVisibleIfNotNull(
    instance: Any?,
    enterAnimation: Animation,
    exitAnimation: Animation
) = animateVisibleIf(instance != null, enterAnimation, exitAnimation)

/**
 * Animates changes to visibility that includes helper logic for setting visibility of a view based
 * on whether an instance is `null`. For this to work, both [enterAnimation] and [exitAnimation]
 * must be specified. When the instance is `null`, the visibility will be [View.VISIBLE] and
 * [enterAnimation] is played. When the instance is not `null`, [exitAnimation] is played and the
 * visibility will be set to [View.GONE].
 *
 * Example usage:
 * ```
 * <View
 *     auto:visibleIfNull="@{viewModel.instance}"
 *     auto:enterAnimation="@{@anim/ttivi_enter_slideup}"
 *     auto:exitAnimation="@{@anim/ttivi_exit_slidedown}" />
 * ```
 */
@BindingAdapter("visibleIfNull", "enterAnimation", "exitAnimation")
fun View.animateVisibleIfNull(
    instance: Any?,
    enterAnimation: Animation,
    exitAnimation: Animation
) = animateVisibleIf(instance == null, enterAnimation, exitAnimation)
