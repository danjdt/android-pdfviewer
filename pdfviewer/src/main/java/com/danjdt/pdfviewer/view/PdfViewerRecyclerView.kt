package com.danjdt.pdfviewer.view

import android.content.Context
import android.graphics.Canvas
import android.os.Build

import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.pdfviewer.renderer.PdfRendererProxy
import com.danjdt.pdfviewer.view.adapter.DefaultPdfPageAdapter
import com.danjdt.pdfviewer.view.adapter.PdfPageAdapter
import com.danjdt.pdfviewer.interfaces.OnPageChangedListener
import com.danjdt.pdfviewer.interfaces.PdfViewInterface
import java.io.File
import java.lang.Exception

/**
 * Created by daniel.teixeira on 10/01/19
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PdfViewerRecyclerView(context: Context) :
    RecyclerView(context, null), PdfViewInterface {

    private var scaleGestureDetector: ScaleGestureDetector

    private var scaleFactor: Float = 1f

    private var maxWidth: Float = 0f

    private var lastTouchX: Float = 0f

    private var touchX: Float = 0f

    private var width: Float = 0f

    private var isZoomEnabled: Boolean = true

    private var mMaxZoom: Float = 3f

    private var minZoom: Float = 1f

    private var quality: Int = 0

    private var mOnPageChangedListener: OnPageChangedListener? = null

    init {
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
        adapter = DefaultPdfPageAdapter(context)
        layoutManager = ExtraSpaceLinearLayoutManager(context)
    }

    override fun setup(file: File) {
        val adapter: PdfPageAdapter<*> = adapter as PdfPageAdapter<*>
        adapter.setup(PdfRendererProxy(file, quality))
        adapter.notifyDataSetChanged()
    }

    override fun setQuality(quality: Int) {
        this.quality = quality
    }

    override fun setZoomEnabled(isZoomEnabled: Boolean) {
        this.isZoomEnabled = isZoomEnabled
    }

    override fun setMaxZoom(maxZoom: Float) {
        this.mMaxZoom = maxZoom
    }

    override fun setOnPageChangedListener(onPageChangedListener: OnPageChangedListener?) {
        this.mOnPageChangedListener = onPageChangedListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        width = View.MeasureSpec.getSize(widthMeasureSpec).toFloat()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            return super.onInterceptTouchEvent(ev)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        super.onTouchEvent(ev)
        performClick()
        val action = ev.action
        scaleGestureDetector.onTouchEvent(ev)
        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                val x = ev.x
                lastTouchX = x
            }

            MotionEvent.ACTION_MOVE -> {
                val pointerIndex =
                    action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val x = ev.getX(pointerIndex)
                val dx = x - lastTouchX

                touchX += dx

                if (touchX > 0f)
                    touchX = 0f
                else if (touchX < maxWidth)
                    touchX = maxWidth

                lastTouchX = x
                invalidate()
            }

            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex =
                    action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val newPointerIndex = if (pointerIndex == 0) 1 else 0
                lastTouchX = ev.getX(newPointerIndex)
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.translate(touchX, 0f)
        canvas.scale(scaleFactor, scaleFactor)
        canvas.restore()
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.save()

        if (scaleFactor == minZoom) {
            touchX = 0f
        }

        canvas.translate(touchX, 0f)
        canvas.scale(scaleFactor, scaleFactor)

        super.dispatchDraw(canvas)

        canvas.restore()
        invalidate()
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if (isZoomEnabled) {
                scaleFactor *= detector.scaleFactor
                scaleFactor = Math.max(minZoom, Math.min(scaleFactor, mMaxZoom))
                maxWidth = width - width * scaleFactor
                invalidate()
            }
            return true
        }
    }

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        val position = (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        mOnPageChangedListener?.onPageChanged(position + 1)
    }
}