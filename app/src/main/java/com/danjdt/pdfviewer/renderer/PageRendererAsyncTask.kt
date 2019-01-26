package com.danjdt.pdfviewer.renderer

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.AsyncTask
import android.os.Build
import android.util.Size
import androidx.annotation.RequiresApi

import com.danjdt.pdfviewer.interfaces.PdfRendererListener
import java.io.IOException

/**
 * Created by daniel.teixeira on 10/01/19
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PageRendererAsyncTask(
    private val listener: PdfRendererListener,
    private val pdfRenderer: PdfRenderer,
    private val position: Int,
    private val size: Size
) : AsyncTask<Void, Void, Bitmap>() {

    init {
        execute()
    }

    @Throws(IOException::class)
    private fun renderPDFPage(pdfRenderer: PdfRenderer, index: Int): Bitmap {
        //Current opened pdf page
        val page = pdfRenderer.openPage(index)

        //Create a bitmap with pdf page dimensions
        val bitmap = Bitmap.createBitmap(
            size.width,
            size.height,
            Bitmap.Config.ARGB_8888
        )

        //Render the page onto the Bitmap.
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

        //Close pdf page
        page.close()
        return bitmap
    }

    override fun doInBackground(vararg v: Void): Bitmap? {
        //Sleep thread for a while then asks if the ViewHolder position is still the same
        //If true render de pdf page, else cancel the async task
        //This is a workaround to render only the necessary bitmaps
        Thread.sleep(32)
        if (listener.shouldRender(position)) {
            return renderPDFPage(pdfRenderer, position)

        } else {
            cancel(true)
        }

        return null
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        listener.onRender(result, position)
    }
}