package com.danjdt.pdfviewer.view

import android.app.Activity
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
import com.danjdt.pdfviewer.utils.Utils
import java.io.File
import java.lang.Exception

/**
 * Created by daniel.teixeira on 10/01/19
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PdfViewerRecyclerView(context: Context) :
    RecyclerView(context, null), PdfViewInterface {

    private var mScaleGestureDetector: ScaleGestureDetector

    private var mScaleFactor: Float = 1f

    private var mMaxWidth: Float = 0f

    private var mLastTouchX: Float = 0f

    private var mTouchX: Float = 0f

    private var mWidth: Float = 0f

    private var mIsZoomEnabled: Boolean = true

    private var mMaxZoom: Float = 3f

    private var mMinZoom: Float = 1f

    private var mQuality: Int = 0

    private var mOnPageChangedListener: OnPageChangedListener? = null

    private var mPosition = -1

    init {
        mScaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
        adapter = DefaultPdfPageAdapter(context)
        layoutManager = ExtraSpaceLinearLayoutManager(context)
    }

    override fun setup(file: File) {
        val adapter: PdfPageAdapter<*> = adapter as PdfPageAdapter<*>
        adapter.setup(PdfRendererProxy(file, mQuality), Utils.getScreenWidth(context as Activity))
        adapter.notifyDataSetChanged()
    }

    override fun setQuality(quality: Int) {
        this.mQuality = quality
    }

    override fun setZoomEnabled(isZoomEnabled: Boolean) {
        this.mIsZoomEnabled = isZoomEnabled
    }

    override fun setMaxZoom(maxZoom: Float) {
        this.mMaxZoom = maxZoom
    }

    override fun setOnPageChangedListener(onPageChangedListener: OnPageChangedListener?) {
        this.mOnPageChangedListener = onPageChangedListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mWidth = View.MeasureSpec.getSize(widthMeasureSpec).toFloat()
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
        mScaleGestureDetector.onTouchEvent(ev)
        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                val x = ev.x
                mLastTouchX = x
            }

            MotionEvent.ACTION_MOVE -> {
                val pointerIndex =
                    action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val x = ev.getX(pointerIndex)
                val dx = x - mLastTouchX

                mTouchX += dx

                if (mTouchX > 0f)
                    mTouchX = 0f
                else if (mTouchX < mMaxWidth)
                    mTouchX = mMaxWidth

                mLastTouchX = x
                invalidate()
            }

            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex =
                    action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val newPointerIndex = if (pointerIndex == 0) 1 else 0
                mLastTouchX = ev.getX(newPointerIndex)
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.translate(mTouchX, 0f)
        canvas.scale(mScaleFactor, mScaleFactor)
        canvas.restore()
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.save()

        if (mScaleFactor == mMinZoom) {
            mTouchX = 0f
        }

        canvas.translate(mTouchX, 0f)
        canvas.scale(mScaleFactor, mScaleFactor)

        super.dispatchDraw(canvas)

        canvas.restore()
        invalidate()
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if (mIsZoomEnabled) {
                mScaleFactor *= detector.scaleFactor
                mScaleFactor = Math.max(mMinZoom, Math.min(mScaleFactor, mMaxZoom))
                mMaxWidth = mWidth - mWidth * mScaleFactor
                invalidate()
            }
            return true
        }
    }

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        val position = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        if(position != mPosition && position != -1) {
            mPosition = position
            mOnPageChangedListener?.onPageChanged(mPosition + 1, adapter?.itemCount ?: 0)
        }
    }
}