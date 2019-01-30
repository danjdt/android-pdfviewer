package com.danjdt.pdfviewer.renderer

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.Size
import androidx.annotation.RequiresApi
import com.danjdt.pdfviewer.interfaces.PdfRendererInterface
import com.danjdt.pdfviewer.interfaces.PdfRendererListener
import java.io.File
import java.io.IOException


/**
 * Created by daniel.teixeira on 24/01/19
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PdfRendererProxy(file: File, quality: Int) : PdfRendererInterface {

    private val mPool = PdfPagePool()

    private var mSize: Size = Size(0, 0)

    private var mPdfRenderer: PdfRenderer? = null

    init {
        openRenderer(file)

        mPdfRenderer?.let {
            mSize = getPageSize(quality)
        }
    }

    override fun put(position: Int, bitmap: Bitmap) {
        mPool.put(position, bitmap)
    }

    override fun get(listener: PdfRendererListener, position: Int) {
        if (mPool.exists(position)) {
            listener.onRender(mPool.get(position), position)

        } else {
            mPdfRenderer?.let { pdfRenderer ->
                PageRendererAsyncTask(listener, pdfRenderer, position, mSize)
            }
        }
    }

    override fun getPageCount(): Int {
        return mPdfRenderer?.pageCount ?: 0
    }

    override fun getPageSize(width: Int): Size {
        val page = mPdfRenderer?.openPage(0)
        var height = 0

        page?.let {
            height = (page.height * width / page.width)
            page.close()
        }

        return Size(width, height)
    }

    @Throws(IOException::class)
    private fun openRenderer(file: File) {
        //File descriptor of the PDF.
        val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)

        // This is the PdfRenderer we use to render the PDF.
        mPdfRenderer = PdfRenderer(fileDescriptor)
    }
}