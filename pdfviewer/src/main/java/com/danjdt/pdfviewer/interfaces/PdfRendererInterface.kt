package com.danjdt.pdfviewer.interfaces

import android.graphics.Bitmap
import android.util.Size

/**
 * Created by daniel.teixeira on 28/01/19
 */
interface PdfRendererInterface {

    fun put(position: Int, bitmap: Bitmap)

    fun get(listener: PdfRendererListener, position: Int)

    fun getPageCount() : Int

    fun getPageSize(width : Int) : Size

}