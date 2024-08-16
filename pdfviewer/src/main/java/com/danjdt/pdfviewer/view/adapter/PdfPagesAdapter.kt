package com.danjdt.pdfviewer.view.adapter

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.danjdt.pdfviewer.renderer.PdfPageRenderer
import com.danjdt.pdfviewer.utils.PdfPageQuality
import kotlinx.coroutines.CoroutineDispatcher

abstract class PdfPagesAdapter<T : PdfPageViewHolder>(
    pdfRenderer: PdfRenderer,
    quality: PdfPageQuality,
    dispatcher: CoroutineDispatcher,
) : ListAdapter<Bitmap, T>(DiffCallback()) {

    private val pdfPageRenderer = PdfPageRenderer(pdfRenderer, quality, dispatcher)

    suspend fun renderPage(position: Int): Result<Bitmap> {
        return pdfPageRenderer.render(position)
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
