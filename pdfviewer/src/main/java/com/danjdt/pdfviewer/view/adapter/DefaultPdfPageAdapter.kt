package com.danjdt.pdfviewer.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.danjdt.pdfviewer.databinding.PdfPageBinding
import com.danjdt.pdfviewer.utils.PdfPageQuality
import java.io.File

class DefaultPdfPageAdapter(
    file: File,
    quality: PdfPageQuality
) : PdfPagesAdapter<DefaultPdfPageViewHolder>(file, quality) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultPdfPageViewHolder {
        val view = PdfPageBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
        return DefaultPdfPageViewHolder(view)
    }

    override fun onBindViewHolder(holder: DefaultPdfPageViewHolder, position: Int) {
        renderPage(position) {
            holder.bind(it)
        }
    }
}