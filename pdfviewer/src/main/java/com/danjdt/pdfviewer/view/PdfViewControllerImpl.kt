package com.danjdt.pdfviewer.view

import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.pdfviewer.interfaces.OnPageChangedListener
import com.danjdt.pdfviewer.interfaces.PdfViewController
import com.danjdt.pdfviewer.utils.PdfPageQuality
import com.danjdt.pdfviewer.view.adapter.DefaultPdfPageAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class PdfViewControllerImpl(
    context: Context,
    private val scope: CoroutineScope,
) : PdfViewController {

    private var view: ZoomableRecyclerView = ZoomableRecyclerView(context)
    private var pageQuality: PdfPageQuality = PdfPageQuality.QUALITY_1080
    private var onPageChangedListener: OnPageChangedListener? = null
    private var dispatcher: CoroutineDispatcher = Dispatchers.IO
    private var lastVisiblePosition = -1

    @Throws(IOException::class, FileNotFoundException::class)
    private fun getPdfRenderer(file: File): PdfRenderer {
        //File descriptor of the PDF.
        val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)

        // This is the PdfRenderer we use to render the PDF.
        return PdfRenderer(fileDescriptor)
    }

    override fun setup(file: File) {
        file.deleteOnExit()
        val pdfRenderer = getPdfRenderer(file)
        view.adapter = DefaultPdfPageAdapter(pdfRenderer, pageQuality, dispatcher, scope)
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

    override fun setDispatcher(dispatcher: CoroutineDispatcher) {
        this.dispatcher = dispatcher
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