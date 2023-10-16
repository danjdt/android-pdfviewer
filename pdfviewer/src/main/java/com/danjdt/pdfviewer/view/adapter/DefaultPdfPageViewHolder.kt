package com.danjdt.pdfviewer.view.adapter

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import com.danjdt.pdfviewer.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

class DefaultPdfPageViewHolder(
    view: View,
    private val scope: CoroutineScope,
    private val renderBlock: suspend (position: Int) -> Result<Bitmap>,
) : PdfPageViewHolder(view) {
    private val imageView: ImageView = itemView.findViewById(R.id.image)

    private var renderJob: Job? = null

    override fun bind(position: Int) {
        renderJob?.cancel()
        renderJob = scope.launch {
            val renderResult = renderBlock(position)
            ensureActive()
            renderResult.onSuccess { page ->
                imageView.layoutParams.height =  (page.height.toDouble() * imageView.width / page.width).toInt()
                imageView.setImageBitmap(page)
            }
        }
    }
}