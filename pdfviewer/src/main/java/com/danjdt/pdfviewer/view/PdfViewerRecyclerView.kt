package com.danjdt.pdfviewer.view

import android.content.Context
import android.graphics.Canvas

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.pdfviewer.view.adapter.DefaultPdfPageAdapter
import com.danjdt.pdfviewer.interfaces.OnPageChangedListener
import com.danjdt.pdfviewer.interfaces.PdfViewInterface
import com.danjdt.pdfviewer.utils.PdfPageQuality
import java.io.File

class PdfViewerRecyclerView(context: Context) :
    RecyclerView(context, null), PdfViewInterface {

    private val linearLayoutManager = ExtraSpaceLinearLayoutManager(context)
    private val scaleLayoutDelegate: ScaleLayoutDelegate by lazy {
        ScaleLayoutDelegate(
            context = context,
            minZoom = minZoom,
            maxZoom = maxZoom.takeIf { isZoomEnabled } ?: minZoom,
            onScaleChanged = { linearLayoutManager.isScrollEnabled = !it },
        )
    }

    private var isZoomEnabled: Boolean = true
    private var minZoom: Float = 1f
    private var maxZoom: Float = 3f
    private var pageQuality: PdfPageQuality = PdfPageQuality.QUALITY_1080
    private var onPageChangedListener: OnPageChangedListener? = null
    private var lastVisiblePosition = -1

    init {
        layoutManager = linearLayoutManager.apply {
            orientation = VERTICAL
        }
    }

    override fun setup(file: File) {
        adapter = DefaultPdfPageAdapter(file, pageQuality)
    }

    override fun setQuality(quality: PdfPageQuality) {
        this.pageQuality = quality
    }

    override fun setZoomEnabled(isZoomEnabled: Boolean) {
        this.isZoomEnabled = isZoomEnabled
    }

    override fun setMaxZoom(maxZoom: Float) {
        this.maxZoom = maxZoom
    }

    override fun setOnPageChangedListener(onPageChangedListener: OnPageChangedListener?) {
        this.onPageChangedListener = onPageChangedListener
    }

    override fun getView(): View = this

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        super.onTouchEvent(ev)
        performClick()
        scaleLayoutDelegate.onTouchEvent(ev)
        return true
    }

    override fun dispatchDraw(canvas: Canvas) {
        scaleLayoutDelegate.updateCanvas(canvas)
        super.dispatchDraw(canvas)
        invalidate()
    }

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        val position =
            (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
        if (position != lastVisiblePosition && position != -1) {
            lastVisiblePosition = position
            onPageChangedListener?.onPageChanged(lastVisiblePosition + 1, adapter?.itemCount ?: 0)
        }
    }
}