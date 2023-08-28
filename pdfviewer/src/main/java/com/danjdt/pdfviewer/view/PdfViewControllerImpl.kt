package com.danjdt.pdfviewer.view

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.pdfviewer.view.adapter.DefaultPdfPageAdapter
import com.danjdt.pdfviewer.interfaces.OnPageChangedListener
import com.danjdt.pdfviewer.interfaces.PdfViewController
import com.danjdt.pdfviewer.utils.PdfPageQuality
import java.io.File

class PdfViewControllerImpl(
    context: Context
) : PdfViewController {

    private var view: ZoomableRecyclerView = ZoomableRecyclerView(context)
    private var pageQuality: PdfPageQuality = PdfPageQuality.QUALITY_1080
    private var onPageChangedListener: OnPageChangedListener? = null
    private var lastVisiblePosition = -1

    override fun setup(file: File) {
        file.deleteOnExit()
        view.adapter = DefaultPdfPageAdapter(file, pageQuality)
    }

    override fun setZoomEnabled(isZoomEnabled: Boolean) {
        view.isZoomEnabled = isZoomEnabled
        view.addOnScrollListener(onScrollListener)
    }

    override fun setMaxZoom(maxZoom: Float) {
        view.maxZoom = maxZoom
    }

    override fun setQuality(quality: PdfPageQuality) {
        this.pageQuality = quality
    }

    override fun setOnPageChangedListener(onPageChangedListener: OnPageChangedListener?) {
        this.onPageChangedListener = onPageChangedListener
    }

    override fun goToPosition(position: Int) {
        view.adapter?.run {
            if (position in 0 until itemCount) {
                view.smoothScrollToPosition(position)
            }
        }
    }

    override fun getView(): View = view

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val position =
                (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            if (position != lastVisiblePosition && position != -1) {
                lastVisiblePosition = position
                onPageChangedListener?.onPageChanged(
                    page = lastVisiblePosition + 1,
                    total = recyclerView.adapter?.itemCount ?: 0
                )
            }
        }
    }
}