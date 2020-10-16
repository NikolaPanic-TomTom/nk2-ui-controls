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

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.updatePadding
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.AbsSpinnerBindingAdapter
import androidx.databinding.adapters.ListenerUtil
import com.tomtom.nk2.api.common.resourceresolution.drawable.DrawableResolver
import com.tomtom.nk2.api.common.resourceresolution.getDimensionByAttr
import com.tomtom.nk2.api.common.resourceresolution.getFloatByAttr
import com.tomtom.nk2.api.common.string_resource_helper.StringResolver
import kotlin.math.min

/**
 * Updates the width and height of the view. This binding is required because `layout_width` and
 * `layout_height` don't support data binding. `layout_width` and `layout_height` still have to
 * be specified in the layout, though their values will be replaced by this data binding. For
 * performance reasons, they should therefore be set to `0dp` when using this data binding.
 */
@BindingAdapter("size")
fun View.setSize(@AttrRes attr: Int) {
    val size = context.getDimensionByAttr(attr)
    layoutParams = layoutParams.apply {
        height = size.toInt()
        width = size.toInt()
    }
}

/**
 * Updates the `layout_constraintGuide_begin` of the guideline.
 */
@BindingAdapter("layout_constraintGuide_begin")
fun Guideline.setLayoutConstraintGuideBegin(margin: Float) {
    setGuidelineBegin(margin.toInt())
}

/**
 * Updates the `layout_constraintGuide_end` of the guideline.
 */
@BindingAdapter("layout_constraintGuide_end")
fun Guideline.setLayoutConstraintGuideEnd(margin: Float) {
    setGuidelineEnd(margin.toInt())
}

/**
 * Updates a vertical bias for a view inside ConstraintLayout.
 */
@BindingAdapter("layout_constraintVertical_bias")
fun View.setConstraintVerticalBias(bias: Float) {
    require(bias in 0.0F..1.0F) { "Vertical bias must between 0.0 and 1.0." }
    require(parent is ConstraintLayout) { "A view must be a child of ConstraintLayout." }

    val layout = parent as ConstraintLayout

    with(ConstraintSet()) {
        clone(layout)
        setVerticalBias(id, bias)
        applyTo(layout)
    }
}

/**
 * Updates the `paddingHorizontal` of the view.
 */
@BindingAdapter("paddingHorizontal")
fun View.setPaddingHorizontal(padding: Float) {
    updatePadding(left = padding.toInt(), right = padding.toInt())
}

/**
 * Updates the `paddingVertical` of the view.
 */
@BindingAdapter("paddingVertical")
fun View.setPaddingVertical(padding: Float) {
    updatePadding(top = padding.toInt(), bottom = padding.toInt())
}

/**
 * [BindingAdapter] for using a [Bitmap] as a source for an [ImageView]. Use it by adding the
 * following to your [ImageView]:
 *
 *   `<ImageView auto:bitmap="@{viewModel.yourBitmapLiveDataSource}" ... />`
 *
 * Optionally add a default [Drawable] in case the bitmap turns out to be null:
 *
 *   `<ImageView auto:defaultDrawable="@{@drawable/ttivi_ic_fallback_image}" ... />`
 */
@BindingAdapter("bitmap", "defaultDrawable", requireAll = false)
fun ImageView.setBitmap(
    bitmap: Bitmap?,
    defaultDrawable: Drawable = ColorDrawable(Color.TRANSPARENT)
) {
    // To adapt to the styled view's shape.
    clipToOutline = true

    if (bitmap != null && bitmap.width > 0 && bitmap.height > 0) {
        // Scaling the bitmap down to the size we need for our ImageView. This is the only way to
        // limit artifacts from the scaling.
        val scaleX = width.toFloat() / bitmap.width
        val scaleY = height.toFloat() / bitmap.height
        val scale = min(scaleX, scaleY)

        // Create a matrix for the scaling and add the scaling data.
        val matrix = Matrix()
        matrix.postScale(scale, scale)

        val scaledBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        setImageBitmap(scaledBitmap)
    } else {
        setImageDrawable(defaultDrawable)
    }
}

/**
 * [BindingAdapter] for changing [View] visibility based on wether a referenced [TextView]'s text is
 * empty.
 *
 * The [View] must have an ID in order to observe the [TextView]'s text.
 *
 * <TextView android:id="@+id/text_view_id" />
 * <ImageView auto:hideIfEmpty="@{textViewId} />
 */
@BindingAdapter("hideIfEmpty")
fun View.setHideIfEmpty(textView: TextView) {
    val newTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            visibility = if (textView.text.isEmpty()) View.GONE else View.VISIBLE
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    require(id != View.NO_ID)
    val oldTextWatcher = ListenerUtil.trackListener(textView, newTextWatcher, id)
    oldTextWatcher?.let { textView.removeTextChangedListener(oldTextWatcher) }
    textView.addTextChangedListener(newTextWatcher)
}

/**
 * A helper data binding adapter for setting visibility of a view based on whether a condition is
 * satisfied.
 * When the condition is satisfied (`true`), the visibility will be [View.VISIBLE]. Otherwise, it
 * will be [View.GONE].
 *
 * This adapter allows replacing
 * ```
 * android:visibility="@{viewModel.condition ? View.VISIBLE : View.GONE}"
 * ```
 * with
 * ```
 * auto:visibleIf="@{viewModel.condition}"
 * ```
 */
@BindingAdapter("visibleIf")
fun View.setVisibleIf(condition: Boolean) {
    visibility = when (condition) {
        true -> View.VISIBLE
        false -> View.GONE
    }
}

/**
 * A helper data binding adapter for setting visibility of a view based on whether a condition is
 * satisfied.
 * When the condition is unsatisfied (`false`), the visibility will be [View.VISIBLE]. Otherwise, it
 * will be [View.GONE].
 *
 * This adapter allows replacing
 * ```
 * android:visibility="@{viewModel.condition ? View.GONE : View.VISIBLE}"
 * ```
 * with
 * ```
 * auto:visibleIfNot="@{viewModel.condition}"
 * ```
 */
@BindingAdapter("visibleIfNot")
fun View.setVisibleIfNot(condition: Boolean) = setVisibleIf(!condition)

/**
 * A helper data binding adapter for setting visibility of a view based on whether an instance is
 * `null`.
 * When the instance is not `null`, the visibility will be [View.VISIBLE]. When the instance is
 * `null`, it will be [View.GONE].
 *
 * This adapter allows replacing
 * ```
 * android:visibility="@{viewModel.instance != null ? View.VISIBLE : View.GONE}"
 * ```
 * with
 * ```
 * auto:visibleIfNotNull="@{viewModel.instance}"
 * ```
 */
@BindingAdapter("visibleIfNotNull")
fun View.setVisibleIfNotNull(instance: Any?) = setVisibleIf(instance != null)

/**
 * A helper data binding adapter for setting visibility of a view based on whether an instance is
 * `null`.
 * When the instance is `null`, the visibility will be [View.VISIBLE]. When the instance is not
 * `null`, it will be [View.GONE].
 *
 * This adapter allows replacing
 * ```
 * android:visibility="@{viewModel.instance == null ? View.VISIBLE : View.GONE}"
 * ```
 * with
 * ```
 * auto:visibleIfNull="@{viewModel.instance}"
 * ```
 */
@BindingAdapter("visibleIfNull")
fun View.setVisibleIfNull(instance: Any?) = setVisibleIf(instance == null)

/**
 * Single method interface, used as the `action` parameter type for the
 * [setTriggeredAction] binding adapter.
 */
interface TriggeredActionInterface {
    fun invoke(view: View)
}

/**
 * [BindingAdapter] for executing an [action] when a defined [Boolean] [actionTrigger]
 * becomes true. Use it by adding the following to your [View]:
 *
 * ```
 * auto:action="@{viewModel::yourViewModelAction}"
 * auto:actionTrigger="@{viewModel.yourViewModelTriggerProperty}"
 * ```
 *
 * @note The auto:action attribute can only be set to a method reference, and not to a lambda
 *     expression. Even though there are official binding adapters that work with lambda
 *     expressions, in this case it doesn't seem to work, even when when using the exact same
 *     signature as the aforementioned official adapters (e.g. [ViewBindingAdapter.setOnClick]).
 */
@BindingAdapter("action", "actionTrigger", requireAll = true)
fun View.setTriggeredAction(action: TriggeredActionInterface, actionTrigger: Boolean) {
    if (actionTrigger) {
        action.invoke(this)
    }
}

/**
 * A helper data binding that allows passing a [StringResolver] without manually resolving the value
 * through the context.
 */
@BindingAdapter("android:text")
fun TextView.setText(stringResolver: StringResolver?) {
    text = stringResolver?.get(context)
}

/**
 * A helper data binding that allows passing a [DrawableResolver] without manually resolving the
 * value through the context.
 */
@BindingAdapter("android:src")
fun ImageView.setSrc(drawableResolver: DrawableResolver?) {
    setImageDrawable(drawableResolver?.get(context))
}

/**
 * A helper data binding that allows passing a [DrawableResolver] without manually resolving the
 * value through the context.
 */
@BindingAdapter("android:background")
fun ImageView.setBackground(drawableResolver: DrawableResolver?) {
    background = drawableResolver?.get(context)
}

/**
 * A helper data binding that allows passing a [DrawableResolver] without manually resolving the
 * value through the context.
 */
@BindingAdapter("android:foreground")
fun ImageView.setForeground(drawableResolver: DrawableResolver?) {
    foreground = drawableResolver?.get(context)
}

/**
 * A data binding that exposes the setter for [ImageView.isSelected].
 */
@BindingAdapter("selected")
fun ImageView.setSelectedThroughBinding(selected: Boolean) {
    isSelected = selected
}

/**
 * A helper data binding that allows passing a [ColorInt] to set the tint.
 */
@BindingAdapter("tint")
fun AppCompatImageView.setTint(@ColorInt color: Int) {
    setColorFilter(color)
}

/**
 * TODO(IVI-1907): Remove this binding adapter because of performance issue.
 * Do not make further use of this adapter.
 *
 * A helper data binding that allows passing an [AttrRes] that describes alpha without manually
 * resolving the value through the context.
 */
@BindingAdapter("android:alpha")
fun AppCompatImageView.setImageViewAlpha(@AttrRes attr: Int) {
    alpha = context.getFloatByAttr(attr)
}

/**
 * Updates the radius of the [CardView].
 * This binding is required because `cardCornerRadius` doesn't support data binding.
 */
@BindingAdapter("cardCornerRadius")
fun CardView.cardCornerRadius(@AttrRes attr: Int) {
    radius = context.getDimensionByAttr(attr)
}

/**
 * Updates the progress of the [ProgressBar].
 * This binding is required because `progress` doesn't support Double.
 */
@BindingAdapter("android:progress")
fun ProgressBar.setProgress(fraction: Double) {
    progress = min + (fraction * (max - min)).toInt()
}

/**
 * A helper data binding that allows passing a list of [StringResolver]s without manually resolving
 * the values through the context.
 */
// Restricted API used because the implementation is rather complex and thus undesirable to
// maintain, and the binding is a stable public API anyway.
@SuppressLint("RestrictedApi")
@BindingAdapter("android:entries")
fun Spinner.setEntries(entries: List<*>) {
    AbsSpinnerBindingAdapter.setEntries(
        this,
        entries.map {
            when (it) {
                is StringResolver -> it.get(context)
                else -> it
            }
        }
    )
}

@BindingAdapter("displayedChild")
fun ViewAnimator.displayedChild(childIndex: Int) {
    displayedChild = childIndex
}
