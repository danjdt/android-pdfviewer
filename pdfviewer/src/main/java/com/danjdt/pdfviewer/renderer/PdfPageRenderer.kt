package com.danjdt.pdfviewer.renderer

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import com.danjdt.pdfviewer.utils.PdfPageQuality
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PdfPageRenderer(private val file: File, private val quality: PdfPageQuality) {

    private val pdfRenderer: PdfRenderer by lazy {
        openRenderer(file)
    }

    val pageCount: Int by lazy {
        pdfRenderer.pageCount
    }

    private fun openRenderer(file: File): PdfRenderer {
        //File descriptor of the PDF.
        val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)

        // This is the PdfRenderer we use to render the PDF.
        return PdfRenderer(fileDescriptor)
    }

    fun render(position: Int, onPageRendered: (Bitmap) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                renderPage(position)
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    onPageRendered(it)
                }
            }.onFailure {
                // LOG ERROR
            }
        }
    }

    private fun renderPage(position: Int) : Bitmap {
        //Current opened pdf page
        val page = pdfRenderer.openPage(position)

        val width = quality.value
        val height = quality.value*page.height/page.width
        //Create a bitmap with pdf page dimensions
        val bitmap = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )

        //Render the page onto the Bitmap.
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

        //Close pdf page
        page.close()
        return bitmap
    }
}