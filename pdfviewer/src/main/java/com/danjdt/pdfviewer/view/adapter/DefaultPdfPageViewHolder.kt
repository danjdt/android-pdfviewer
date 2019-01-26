package com.danjdt.pdfviewer.view.adapter

import android.graphics.Bitmap
import android.os.Build
import android.util.Size
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.danjdt.pdfviewer.R
import com.danjdt.pdfviewer.renderer.PdfRendererProxy

/**
 * Created by daniel.teixeira on 25/01/19
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class DefaultPdfPageViewHolder(view: View, pdfRenderer: PdfRendererProxy, pageSize: Size?) :
    PdfPageViewHolder(view, pdfRenderer, pageSize) {

    private val image: ImageView = itemView.findViewById(R.id.image)

    override fun displayPage(bitmap: Bitmap, position: Int) {
        image.setImageBitmap(bitmap)
    }

    override fun resizePage() {
        pageSize?.let {pageSize ->
            image.layoutParams.width = pageSize.width
            image.layoutParams.height = pageSize.height
        }
    }

    override fun getPage(position: Int) {
        pdfRenderer.get(this, pagePosition)
    }

    override fun displayPlaceHolder() {
        image.setImageDrawable(image.context.getDrawable(R.drawable.blank_pdf_page))
    }
}