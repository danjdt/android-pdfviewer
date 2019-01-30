package com.danjdt.pdfviewer.view.adapter

import android.util.Size
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.pdfviewer.interfaces.PdfRendererInterface

/**
 * Created by daniel.teixeira on 10/01/19
 */
abstract class PdfPageAdapter<T : PdfPageViewHolder> : RecyclerView.Adapter<T>() {

    lateinit var mPdfRenderer : PdfRendererInterface

    private var mNumPages: Int = 0

    var mPageSize : Size? = null

    fun setup(pdfRenderer: PdfRendererInterface, pageWidth : Int) {
        this.mPdfRenderer = pdfRenderer
        this.mNumPages = pdfRenderer.getPageCount()
        this.mPageSize = pdfRenderer.getPageSize(pageWidth)
    }

    final override fun getItemCount(): Int {
        return mNumPages
    }
}
