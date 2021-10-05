package com.danjdt.pdfviewer.view.adapter

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Size
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.danjdt.pdfviewer.interfaces.PdfRendererInterface

/**
 * Created by daniel.teixeira on 25/01/19
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class DefaultPdfPageViewHolder(view: View, pdfRenderer: PdfRendererInterface, pageSize: Size?) :
    PdfPageViewHolder(view, pdfRenderer, pageSize) {

    private val placeHolder = ColorDrawable(DefaultPdfPageAdapter.DEFAULT_COLOR)
    private val image: ImageView = itemView.findViewWithTag(DefaultPdfPageAdapter.TAG)

    override fun displayPage(bitmap: Bitmap, position: Int) {
        image.setImageBitmap(bitmap)
    }

    override fun resizePage() {
        pageSize?.let { pageSize ->
            image.layoutParams.width = pageSize.width
            image.layoutParams.height = pageSize.height
        }
    }

    override fun getPage(position: Int) {
        pdfRenderer.get(this, mPagePosition)
    }

    override fun displayPlaceHolder() {
        image.setImageDrawable(placeHolder)
    }
}