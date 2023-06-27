package com.danjdt.pdfviewer.view

import android.app.Activity
import android.content.ComponentCallbacks
import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.pdfviewer.utils.Utils

class ScaleLayoutDelegate(
    context: Context,
    private val minZoom: Float,
    private val maxZoom: Float,
    private val onScaleChanged: (isScaling: Boolean) -> Unit,
) {

    private var scaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(context, ScaleListener())

    private var scaleFactor: Float = 1f

    private var isScaling = false
    private var isZoomEnabled: Boolean = true

    private var scaleFocusX = 0f
    private var scaleFocusY = 0f

    private var translationX = 0f
    private var translationY = 0f

    private var focusX = 0f
    private var focusY = 0f

    fun updateCanvas(canvas: Canvas){
        if (!isScaling) {
            decreaseScale()
        }

        translationX = calcTranslationX(canvas)
        translationY = calcTranslationY()

        canvas.scale(scaleFactor, scaleFactor)
        canvas.translate(translationX, translationY)
    }

    fun onTouchEvent(ev: MotionEvent) {
        scaleGestureDetector.onTouchEvent(ev)
    }

    private fun decreaseScale() {
        if (scaleFactor > minZoom) {
            scaleFactor -= 0.1f
            translationX = focusX * ((scaleFactor - minZoom) / (scaleFactor))
            translationY = focusY * ((scaleFactor - minZoom) / (scaleFactor))

            if (scaleFactor < minZoom) {
                translationX = 0f
                translationY = 0f
                scaleFactor = minZoom
                onScaleChanged(false)
            }
        }
    }

    private fun calcTranslationX(canvas: Canvas): Float = (translationX).let {
        if (it > 0) {
            0f
        } else if (it < -canvas.width.toFloat() * ((scaleFactor - minZoom) / scaleFactor)) {
            -canvas.width.toFloat() * ((scaleFactor - minZoom) / scaleFactor)
        } else {
            it
        }
    }

    private fun calcTranslationY(): Float = (translationY).let {
        if (it > 0) {
            0f
        } else {
            it
        }
    }

    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if (isZoomEnabled) {
                focusX = -detector.focusX
                focusY = -detector.focusY
                scaleFactor *= detector.scaleFactor
                scaleFactor = minZoom.coerceAtLeast(scaleFactor.coerceAtMost(maxZoom))
                translationX = focusX * ((scaleFactor - minZoom) / (scaleFactor))
                translationY = focusY * ((scaleFactor - minZoom) / (scaleFactor))
            }

            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            isScaling = false
            if (scaleFactor <= minZoom) {
                onScaleChanged(false)
            }
            super.onScaleEnd(detector)
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            isScaling = true
            onScaleChanged(true)
            scaleFocusX = detector.focusX
            scaleFocusY = detector.focusY
            return super.onScaleBegin(detector)
        }
    }
}

class ExtraSpaceLinearLayoutManager(private val context: Context) : LinearLayoutManager(context) {

    var isScrollEnabled = true

    @Deprecated("Deprecated in Java")
    override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
        return Utils.getScreenHeight(context as Activity)
    }

    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically() && isScrollEnabled
    }
}