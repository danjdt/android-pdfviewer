package com.danjdt.pdfviewer.interfaces

import android.graphics.Bitmap

interface PdfRendererListener {

    fun onRender(bitmap: Bitmap?, position: Int)

    fun shouldRender(index: Int) : Boolean
}
