package com.danjdt.pdfviewer.view

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import androidx.recyclerview.widget.RecyclerView

open class ZoomableRecyclerView(
    context: Context
) : RecyclerView(context, null), OnScaleGestureListener {

    var isZoomEnabled: Boolean = true
    var minZoom: Float = 1f
    var maxZoom: Float = 3f

    private var scaleFactor: Float = 1f

    private var isScaling = false

    private var scaleFocusX = 0f
    private var scaleFocusY = 0f

    private var translationX = 0f
    private var translationY = 0f

    private var focusX = 0f
    private var focusY = 0f

    private val linearLayoutManager by lazy { ExtraSpaceLinearLayoutManager(context) }
    private val scaleDetector: ScaleGestureDetector by lazy { ScaleGestureDetector(context, this) }

    init {
        layoutManager = linearLayoutManager.apply { orientation = VERTICAL }
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        super.onTouchEvent(ev)
        performClick()
        scaleDetector.onTouchEvent(ev)
        return true
    }

    override fun dispatchDraw(canvas: Canvas) {
        updateCanvas(canvas)
        super.dispatchDraw(canvas)
        invalidate()
    }

    private fun updateCanvas(canvas: Canvas) {
        if (!isScaling) {
            decreaseScale()
        }

        translationX = calcTranslationX(canvas)
        translationY = calcTranslationY()

        canvas.scale(scaleFactor, scaleFactor)
        canvas.translate(translationX, translationY)
    }

    private fun onScaleChanged() {
        linearLayoutManager.isScrollEnabled = !isScaling
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
                onScaleChanged()
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

    override fun onScale(detector: ScaleGestureDetector) : Boolean {
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

    override fun onScaleBegin(detector: ScaleGestureDetector) : Boolean {
        isScaling = true
        onScaleChanged()
        scaleFocusX = detector.focusX
        scaleFocusY = detector.focusY
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {
        isScaling = false
        if (scaleFactor <= minZoom) {
            onScaleChanged()
        }
    }
}