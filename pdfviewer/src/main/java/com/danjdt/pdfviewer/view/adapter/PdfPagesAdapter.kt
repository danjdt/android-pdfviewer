package com.danjdt.pdfviewer.view.adapter

import android.graphics.Bitmap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.danjdt.pdfviewer.renderer.PdfPageRenderer
import com.danjdt.pdfviewer.utils.PdfPageQuality
import java.io.File

abstract class PdfPagesAdapter<T : PdfPageViewHolder>(
    private val pdfFile: File,
    private val quality: PdfPageQuality
) : ListAdapter<Bitmap, T>(DiffCallback()) {

    private val pdfPageRenderer: PdfPageRenderer by lazy {
        PdfPageRenderer(pdfFile, quality)
    }

    fun renderPage(position: Int, onPageRendered: (Bitmap) -> Unit) {
        pdfPageRenderer.render(position, onPageRendered)
    }

    override fun getItemCount(): Int {
        return pdfPageRenderer.pageCount
    }

    class DiffCallback: DiffUtil.ItemCallback<Bitmap>() {
        override fun areItemsTheSame(oldItem: Bitmap, newItem: Bitmap): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Bitmap, newItem: Bitmap): Boolean {
           return oldItem.hashCode() == newItem.hashCode()
        }
    }
}
