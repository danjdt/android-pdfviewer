package com.danjdt.pdfviewer.view.adapter

import android.app.Activity
import android.graphics.Bitmap
import android.os.Build
import android.util.Size
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.pdfviewer.interfaces.PdfRendererInterface
import com.danjdt.pdfviewer.interfaces.ViewHolderInterface

/**
 * Created by daniel.teixeira on 15/01/19
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
abstract class PdfPageViewHolder(view: View, val pdfRenderer: PdfRendererInterface, val pageSize: Size?) :
    RecyclerView.ViewHolder(view), ViewHolderInterface {

    var mPagePosition = -1

    private val activity = view.context as Activity

    final override fun shouldRender(index: Int): Boolean {
        return this.mPagePosition == index
    }

    final override fun onRender(bitmap: Bitmap?, position: Int) {
        if (this.mPagePosition == position && bitmap != null) {
            pdfRenderer.put(position, bitmap)
            activity.runOnUiThread {
                displayPage(bitmap, position)
            }
        }
    }

    final override fun bind(position: Int) {
        mPagePosition = position
        displayPlaceHolder()
        getPage(mPagePosition)
        resizePage()
    }
}