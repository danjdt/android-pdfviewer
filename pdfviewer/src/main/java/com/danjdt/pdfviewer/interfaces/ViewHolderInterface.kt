package com.danjdt.pdfviewer.interfaces

import android.graphics.Bitmap

/**
 * Created by daniel.teixeira on 15/01/19
 */
interface ViewHolderInterface : PdfRendererListener {

    fun bind(position: Int)

    fun displayPage(bitmap: Bitmap, position: Int)

    fun resizePage()

    fun getPage(position: Int)

    fun displayPlaceHolder()
}