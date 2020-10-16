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

package com.tomtom.nk2.core.common.animation.view

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup

/**
 * A [DraggedViewPositionUpdater] provides horizontal view animation.
 *
 * @param dismissalStrategy Define dismissal behaviour.
 * @param view View to animate.
 * @param fadingEnabled Whether fading effect is enabled.
 * @param onDismissalStarted Function to call after triggering dismissal. This function is executed
 * along animation is in motion.
 * @param onDismissalCompleted Function to call after the animation finishes.
 */
class ViewMotionEvents private constructor(
    private val view: View,
    private val dismissalStrategy: DismissalStrategy,
    private val fadingEnabled: Boolean,
    private val onDismissalStarted: (() -> Unit)?,
    private val onDismissalCompleted: (() -> Unit)?
) : View.OnTouchListener {
    private var horizontalDirectionTracker = HorizontalDirectionTracker()
    private var velocityTracker: VelocityTracker? = null

    private var startDraggingPositionPx = 0.0F

    private val draggedViewPositionUpdater: DraggedViewPositionUpdater =
        DraggedViewPositionUpdater(view, fadingEnabled)

    val animation = HorizontalMovementSpringAnimation(view, fadingEnabled)

    @Suppress("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent?): Boolean {
        return when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // Ask parent to keep forwarding all touch events to a view even if a view is
                // outside of parent area, so this view will not receive ACTION_CANCEL while it is
                // being dragged out of parent's area.
                // This request is valid for current gesture only.
                view.parent.requestDisallowInterceptTouchEvent(true)

                velocityTracker?.clear()

                // Velocity tracker is recycled at ACTION_UP event.
                @SuppressLint("Recycle")
                velocityTracker = (velocityTracker ?: VelocityTracker.obtain()).apply {
                    addMovement(event)
                }

                horizontalDirectionTracker.clear()
                horizontalDirectionTracker.updateDirection(event.rawX)

                startDraggingPositionPx = event.rawX
                true
            }

            MotionEvent.ACTION_MOVE -> {
                velocityTracker!!.addMovement(event)

                horizontalDirectionTracker.updateDirection(event.rawX)

                // It is simplified dragging that does not support series of drag events,
                // then a view is dragged, released and then dragged again before animation
                // completes, so it should be dragged even further.
                // Instead, all drag events are considered to start from default view position.
                // It gives more consistent effect because ongoing spring animation is not cancelled
                // when new dragging has started.
                val draggedDistance = event.rawX - startDraggingPositionPx
                if (draggedDistance in dismissalStrategy.getMovableArea(view)) {
                    draggedViewPositionUpdater.onMoved(draggedDistance)
                }

                true
            }

            MotionEvent.ACTION_UP -> {
                velocityTracker!!.apply {
                    val pointerId: Int = event.getPointerId(event.actionIndex)
                    addMovement(event)
                    computeCurrentVelocity(VELOCITY_UNIT_PIXELS_PER_X_MS)
                    val horizontalVelocityPxPerSec = getXVelocity(pointerId)
                    recycle()

                    val movableArea = dismissalStrategy.getMovableArea(view)
                    when (dismissalStrategy.getDismissDirection(
                        view,
                        horizontalVelocityPxPerSec,
                        horizontalDirectionTracker.lastRecentDirection
                    )
                        ) {
                        DismissalStrategy.DismissDirection.LEFT -> {
                            onDismissalStarted?.invoke()

                            animation.animateToPosition(
                                -view.width.toFloat(),
                                horizontalVelocityPxPerSec,
                                movableArea
                            ) { onDismissalCompleted?.invoke() }
                        }
                        DismissalStrategy.DismissDirection.RIGHT -> {
                            onDismissalStarted?.invoke()

                            animation.animateToPosition(
                                view.width.toFloat(),
                                horizontalVelocityPxPerSec,
                                movableArea
                            ) { onDismissalCompleted?.invoke() }
                        }
                        null -> {
                            animation.animateToPosition(
                                0.0F,
                                horizontalVelocityPxPerSec,
                                movableArea
                            )
                        }
                    }
                }
                velocityTracker = null
                true
            }

            else -> false
        }
    }

    companion object {
        /**
         * The unit velocity should be calculated in. A value of 1 provides pixels per millisecond,
         * 1000 provides pixels per second, etc.
         */
        private const val VELOCITY_UNIT_PIXELS_PER_X_MS = 1000

        /**
         * Enables drag dismissal functionality for view container.
         *
         * @param container Container including view.
         * @param dismissalStrategy Define dismissal behaviour.
         * @param onDismissalStarted Function to call after triggering dismissal. This function is
         * executed along animation is in motion.
         * @param onDismissalCompleted Function to call after the animation finishes.
         */
        fun enableDragDismissal(
            container: ViewGroup,
            dismissalStrategy: DismissalStrategy,
            fadingEnabled: Boolean,
            onDismissalStarted: (() -> Unit)? = null,
            onDismissalCompleted: (() -> Unit)? = null
        ) {
            // TODO(IVI-780): Motion events should evaluated directly on container.
            container.setOnTouchListener(
                ViewMotionEvents(
                    container as View,
                    dismissalStrategy,
                    fadingEnabled,
                    onDismissalStarted,
                    onDismissalCompleted
                )
            )
        }
    }
}
