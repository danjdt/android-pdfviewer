package com.danjdt.pdfviewer.view.adapter

import android.graphics.pdf.PdfRenderer
import android.view.LayoutInflater
import android.view.ViewGroup
import com.danjdt.pdfviewer.databinding.PdfPageBinding
import com.danjdt.pdfviewer.utils.PdfPageQuality
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

class DefaultPdfPageAdapter(
    pdfRenderer: PdfRenderer,
    quality: PdfPageQuality,
    dispatcher: CoroutineDispatcher,
    private val scope: CoroutineScope,
) : PdfPagesAdapter<DefaultPdfPageViewHolder>(pdfRenderer, quality, dispatcher) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultPdfPageViewHolder {
        val view = PdfPageBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
        return DefaultPdfPageViewHolder(view, scope, ::renderPage)
    }

    override fun onBindViewHolder(holder: DefaultPdfPageViewHolder, position: Int) {
        holder.bind(position)
    }
}