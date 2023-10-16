package com.danjdt.pdfviewer.renderer

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import com.danjdt.pdfviewer.utils.PdfPageQuality
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File

class PdfPageRenderer(
    private val file: File,
    private val quality: PdfPageQuality,
    private val dispatcher: CoroutineDispatcher,
) {
    private val deferredMap = mutableMapOf<Int, Deferred<Result<Bitmap>>>()
    private val mutex = Mutex()

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

    @Suppress("DeferredResultUnused")
    suspend fun render(position: Int): Result<Bitmap> {
        return withContext(dispatcher) {
            getBitmapAsync(position).await().also {
                mutex.withLock {
                    deferredMap.remove(position)
                }
            }
        }
    }

    private suspend fun getBitmapAsync(position: Int): Deferred<Result<Bitmap>> = mutex.withLock {
        deferredMap.getOrPut(position) {
            coroutineScope {
                async {
                    runCatching {
                        renderPage(position)
                    }.onFailure { throwable ->
                        Log.e("PdfPageRenderer", "Page #$position render has failed", throwable)
                    }
                }
            }
        }
    }

    private fun renderPage(position: Int): Bitmap {
        return pdfRenderer.openPage(position).use { page ->
            val width = quality.value
            val height = quality.value * page.height / page.width
            //Create a bitmap with pdf page dimensions
            val bitmap = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )

            //Render the page onto the Bitmap.
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            bitmap
        }
    }
}