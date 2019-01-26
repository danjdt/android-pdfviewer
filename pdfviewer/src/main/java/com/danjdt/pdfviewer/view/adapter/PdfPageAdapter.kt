package com.danjdt.pdfviewer.view.adapter

import android.util.Size
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.pdfviewer.renderer.PdfRendererProxy

/**
 * Created by daniel.teixeira on 10/01/19
 */
abstract class PdfPageAdapter<T : PdfPageViewHolder> : RecyclerView.Adapter<T>() {

    lateinit var pdfRenderer : PdfRendererProxy

    private var numPages: Int = 0

    var pageSize : Size? = null

    fun setup(pdfRenderer: PdfRendererProxy) {
        this.pdfRenderer = pdfRenderer
        this.numPages = pdfRenderer.pageCount
        this.pageSize = pdfRenderer.size
    }

    final override fun getItemCount(): Int {
        return numPages
    }
}
