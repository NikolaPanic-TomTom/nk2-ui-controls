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
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.use
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.tomtom.nk2.api.common.resourceresolution.drawable.DrawableResolver
import com.tomtom.nk2.core.common.uicontrols.R
import kotlin.properties.Delegates.observable

/**
 * A reusable UI control to asynchronously load and display remote images.
 *
 * @param context The display context.
 * @param attributeSet The attribute set provided in the layout XML file.
 * @param defStyleAttr An attribute referencing a resource with default values. Use 0 to not look
 *                     for defaults.
 */
class TtiviRemoteImageView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) :
    AppCompatImageView(context, attributeSet, defStyleAttr) {

    var ttiviUri by observable<Uri?>(null) { _, _, _ -> update() }

    var ttiviPlaceholderDrawable by observable<Drawable?>(null) { _, _, _ -> update() }

    // Flag used to avoid launching multiple updates when initially setting up from XML.
    private var isInitializing = true

    private var isLoading = false

    private val glideOptions: RequestOptions = RequestOptions().apply {
        // This option tells Glide to prefer hardware storage of bitmap data.
        format(DecodeFormat.PREFER_ARGB_8888)
        // This fits the image to the view size while preserving its aspect ratio.
        fitCenter()
    }

    init {
        clipToOutline = true
        context.obtainStyledAttributes(
            attributeSet,
            R.styleable.TtiviRemoteImageView,
            defStyleAttr,
            0
        ).use { attrs ->
            ttiviPlaceholderDrawable =
                attrs.getDrawable(R.styleable.TtiviRemoteImageView_ttiviPlaceholderDrawable)?.also {
                    setImageDrawable(it)
                }
            ttiviUri =
                attrs.getNonResourceString(R.styleable.TtiviRemoteImageView_ttiviUri)?.let {
                    Uri.parse(it)
                }
        }
        isInitializing = false
        update()
    }

    /**
     * Simple constructor to use when creating the control from code.
     *
     * @param context The display context.
     */
    constructor (context: Context) : this(context, null, 0)

    /**
     * Constructor called when inflating the control from XML.
     *
     * @param context The display context.
     * @param attributeSet The attribute set provided in the layout XML file.
     */
    constructor(context: Context, attributeSet: AttributeSet) :
        this(context, attributeSet, 0)

    fun setTtiviPlaceholderDrawable(drawableResolver: DrawableResolver?) {
        ttiviPlaceholderDrawable = drawableResolver?.get(context)
    }

    /**
     * Ensure that if a fallback drawable is set, the view will always show something.
     *
     * @param drawable New drawable to display.
     */
    override fun setImageDrawable(drawable: Drawable?) {
        isLoading = false
        if (drawable != this.drawable) {
            super.setImageDrawable(drawable ?: ttiviPlaceholderDrawable)
        }
    }

    private fun update() {
        if (isInitializing) {
            return
        }

        if (drawable == null) {
            setImageDrawable(ttiviPlaceholderDrawable)
        }

        if (ttiviUri != null && !isLoading) {
            isLoading = true
            Glide.with(context)
                .load(ttiviUri)
                .apply(glideOptions)
                .into(this)
        }
    }
}
