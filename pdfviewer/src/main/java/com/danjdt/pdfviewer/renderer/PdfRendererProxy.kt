package com.danjdt.pdfviewer.renderer

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.Size
import androidx.annotation.RequiresApi
import com.danjdt.pdfviewer.interfaces.PdfRendererListener
import com.danjdt.pdfviewer.utils.PdfPageQuality
import java.io.File
import java.io.IOException


/**
 * Created by daniel.teixeira on 24/01/19
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PdfRendererProxy(file: File, quality: Int) {

    private val pool = PdfPagePool()

    lateinit var pdfRenderer: PdfRenderer

    var size: Size

    var pageCount: Int = 0

    init {
        openRenderer(file)
        pageCount = pdfRenderer.pageCount
        size = calcPageSize(quality)
    }

    fun put(position: Int, bitmap: Bitmap) {
        pool.put(position, bitmap)
    }

    fun get(listener: PdfRendererListener, position: Int) {
        if (pool.exists(position)) {
            listener.onRender(pool.get(position), position)

        } else {
            PageRendererAsyncTask(listener, pdfRenderer, position, size)
        }
    }

    private fun calcPageSize(quality: Int): Size {
        val page = pdfRenderer.openPage(0)
        val width = if (quality < PdfPageQuality.QUALITY_1440.value!!) quality else PdfPageQuality.QUALITY_1440.value
        val height = (page.height * width / page.width)
        page.close()
        return Size(width, height)
    }

    @Throws(IOException::class)
    private fun openRenderer(file: File) {
        //File descriptor of the PDF.
        val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)

        // This is the PdfRenderer we use to render the PDF.
        pdfRenderer = PdfRenderer(fileDescriptor)
    }
}