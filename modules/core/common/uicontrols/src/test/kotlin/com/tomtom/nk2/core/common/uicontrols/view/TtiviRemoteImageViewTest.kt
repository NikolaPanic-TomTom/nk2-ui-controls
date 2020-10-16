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

// Suppress a deprecation for Glide API `ViewTarget`.
@file:Suppress("DEPRECATION")

package com.tomtom.nk2.core.common.uicontrols.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.ViewTarget
import com.tomtom.nk2.core.common.uicontrols.view.shadows.ShadowAppCompatResources
import com.tomtom.nk2.tools.testing.mock.niceMockk
import com.tomtom.nk2.tools.testing.unit.TomTomTestCase
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.mockkStatic
import io.mockk.slot
import org.junit.Before
import org.junit.Test
import org.robolectric.annotation.Config

@Config(shadows = [ShadowAppCompatResources::class])
class TtiviRemoteImageViewTest : TomTomTestCase() {

    private var isGlideInitialized = false
    private var isImageDrawableSetSynchronously = false
    private val mockGlideRequest: RequestBuilder<*> = mockkClass(RequestBuilder::class)
    private val mockGlideRequestManager = mockkClass(RequestManager::class)
    private val mockGlideCapturedView = slot<TtiviRemoteImageView>()

    @Before
    @Suppress("UNCHECKED_CAST")
    fun initializeMocks() {
        every { mockGlideRequestManager.load(any<Uri>()) } returns
            mockGlideRequest as RequestBuilder<Drawable>
        every { mockGlideRequest.transition(any()) } returns
            mockGlideRequest
        every { mockGlideRequest.into(capture(mockGlideCapturedView)) } answers {
            if (isImageDrawableSetSynchronously) {
                setDrawableOnView()
            }
            niceMockk<ViewTarget<ImageView, Drawable>>()
        }
        every { mockGlideRequest.apply(any()) } returns
            mockGlideRequest
        mockkStatic(Glide::class)
        every { Glide.with(any<Context>()) } answers {
            isGlideInitialized = true
            mockGlideRequestManager
        }
    }

    @Test
    fun `empty initialization`() {
        // GIVEN
        val mockContext = getContextWithResources(null, null)
        val sut = TtiviRemoteImageView(mockContext)

        // THEN
        assertEquals(null, sut.ttiviUri)
        assertEquals(null, sut.ttiviPlaceholderDrawable)
        assertEquals(null, sut.drawable)
        assertEquals(false, isGlideInitialized)
    }

    @Test
    fun `xml uri without placeholder`() {
        // GIVEN
        val mockContext = getContextWithResources(mockUriString, null)
        val sut = TtiviRemoteImageView(mockContext)

        // THEN
        assertEquals(mockUri, sut.ttiviUri)
        assertEquals(null, sut.ttiviPlaceholderDrawable)
        assertEquals(null, sut.drawable)
        assertEquals(true, isGlideInitialized)
    }

    @Test
    fun `xml placeholder without uri`() {
        // GIVEN
        val mockContext = getContextWithResources(null, testPlaceholder)
        val sut = TtiviRemoteImageView(mockContext)

        // THEN
        assertEquals(null, sut.ttiviUri)
        assertEquals(testPlaceholder, sut.ttiviPlaceholderDrawable)
        assertBitmapEquals(testPlaceholder, sut.drawable)
        assertEquals(false, isGlideInitialized)
    }

    @Test
    fun `xml placeholder and uri`() {
        // GIVEN
        val mockContext = getContextWithResources(mockUriString, testPlaceholder)
        val sut = TtiviRemoteImageView(mockContext)

        // THEN
        assertEquals(mockUri, sut.ttiviUri)
        assertEquals(testPlaceholder, sut.ttiviPlaceholderDrawable)
        assertBitmapEquals(testPlaceholder, sut.drawable)
        assertEquals(true, isGlideInitialized)
    }

    @Test
    fun `xml image loading`() {
        // GIVEN a placeholder and uri are set via xml
        val mockContext = getContextWithResources(mockUriString, testPlaceholder)
        val sut = TtiviRemoteImageView(mockContext)
        assertBitmapEquals(testPlaceholder, sut.drawable)

        // WHEN the image gets downloaded
        setDrawableOnView()

        // THEN the data did not change but the drawable itself did
        assertEquals(testPlaceholder, sut.ttiviPlaceholderDrawable)
        assertEquals(mockUri, sut.ttiviUri)
        assertBitmapEquals(testDownloadedImage, sut.drawable)
    }

    @Test
    fun `runtime uri selection`() {
        // GIVEN an empty initialized view
        val mockContext = getContextWithResources(null, null)
        val sut = TtiviRemoteImageView(mockContext)

        // WHEN an uri is selected
        assertEquals(null, sut.ttiviUri)
        sut.ttiviUri = mockUri
        setDrawableOnView()

        // THEN an image is downloaded
        assertEquals(null, sut.ttiviPlaceholderDrawable)
        assertEquals(mockUri, sut.ttiviUri)
        assertBitmapEquals(testDownloadedImage, sut.drawable)
    }

    @Test
    fun `runtime placeholder selection`() {
        // GIVEN an empty initialized view
        val mockContext = getContextWithResources(null, null)
        val sut = TtiviRemoteImageView(mockContext)
        assertEquals(null, sut.ttiviPlaceholderDrawable)

        // WHEN a placeholder is selected
        sut.ttiviPlaceholderDrawable = testPlaceholder

        // THEN the placeholder is visible
        assertBitmapEquals(testPlaceholder, sut.drawable)
    }

    @Test
    fun `synchronous image setting`() {
        // GIVEN
        isImageDrawableSetSynchronously = true
        val mockContext = getContextWithResources(null, null)
        val sut = TtiviRemoteImageView(mockContext)

        // WHEN
        sut.ttiviUri = mockUri

        // THEN
        assertBitmapEquals(testDownloadedImage, sut.drawable)

        // WHEN
        sut.ttiviUri = mockUri2

        // THEN
        assertBitmapEquals(testDownloadedImage2, sut.drawable)
    }

    private fun setDrawableOnView() {
        assertTrue(isGlideInitialized)

        val sut = mockGlideCapturedView.captured
        when (sut.ttiviUri) {
            mockUri -> sut.setImageDrawable(testDownloadedImage)
            mockUri2 -> sut.setImageDrawable(testDownloadedImage2)
            else -> fail("No valid URI set")
        }
    }

    private fun getContextWithResources(uriString: String?, drawable: Drawable?): Context =
        niceMockk {
            every { obtainStyledAttributes(any(), any(), any(), any()) } returns
                niceMockk {
                    every { getDrawable(any()) } returns drawable
                    every { getNonResourceString(any()) } returns uriString
                }
        }

    private fun assertBitmapEquals(expected: Drawable, actual: Drawable) {
        assertEquals(expected.toBitmap(), actual.toBitmap())
    }

    companion object {
        private const val PLACEHOLDER_BITMAP_SIZE = 42
        private const val DOWNLOADED_BITMAP_SIZE = 43

        private val testPlaceholder = BitmapDrawable(Bitmap.createBitmap(
            IntArray(PLACEHOLDER_BITMAP_SIZE * PLACEHOLDER_BITMAP_SIZE) { Color.GREEN },
            PLACEHOLDER_BITMAP_SIZE,
            PLACEHOLDER_BITMAP_SIZE,
            Bitmap.Config.ARGB_8888
        ))
        private val testDownloadedImage = BitmapDrawable(Bitmap.createBitmap(
            IntArray(DOWNLOADED_BITMAP_SIZE * DOWNLOADED_BITMAP_SIZE) { Color.RED },
            DOWNLOADED_BITMAP_SIZE,
            DOWNLOADED_BITMAP_SIZE,
            Bitmap.Config.RGB_565
        ))
        private val testDownloadedImage2 = BitmapDrawable(Bitmap.createBitmap(
            IntArray(DOWNLOADED_BITMAP_SIZE * DOWNLOADED_BITMAP_SIZE) { Color.BLUE },
            DOWNLOADED_BITMAP_SIZE,
            DOWNLOADED_BITMAP_SIZE,
            Bitmap.Config.RGB_565
        ))

        private const val mockUriString = "some://uri"
        private val mockUri = Uri.parse(mockUriString)
        private const val mockUriString2 = "some://uri2"
        private val mockUri2 = Uri.parse(mockUriString2)
    }
}
