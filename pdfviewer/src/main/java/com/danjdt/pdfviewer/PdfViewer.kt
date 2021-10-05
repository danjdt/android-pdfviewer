package com.danjdt.pdfviewer

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.annotation.RawRes
import androidx.annotation.RequiresApi
import com.danjdt.pdfviewer.decoder.FileLoader
import com.danjdt.pdfviewer.interfaces.OnLoadFileListener
import com.danjdt.pdfviewer.interfaces.OnErrorListener
import com.danjdt.pdfviewer.interfaces.OnPageChangedListener
import com.danjdt.pdfviewer.interfaces.PdfViewInterface
import com.danjdt.pdfviewer.utils.PdfPageQuality
import com.danjdt.pdfviewer.utils.Utils
import com.danjdt.pdfviewer.view.PdfViewerRecyclerView
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.Exception


/**
 * Created by daniel.teixeira on 10/01/19
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PdfViewer private constructor(private val mRootView: ViewGroup) : OnLoadFileListener {

    private var mContext = mRootView.context

    private lateinit var mView: PdfViewInterface

    private var mOnErrorListener: OnErrorListener? = null

    @MainThread
    private fun display(file: File) {
        try {
            mRootView.addView(mView as View)
            mView.setup(file)

        } catch (e: IOException) {
            mOnErrorListener?.onPdfRendererError(e)

        } catch (e: Exception) {
            mOnErrorListener?.onAttachViewError(e)
        }
    }

    fun load(file: File) {
        display(file)
    }

    fun load(@RawRes resId: Int) {
        FileLoader.loadFile(mContext, this, resId)
    }

    fun load(input: InputStream) {
        FileLoader.loadFile(mContext, this, input)
    }

    fun load(uri: Uri) {
        FileLoader.loadFile(mContext, this, uri)
    }

    fun load(url: String) {
        FileLoader.loadFile(mContext, this, url)
    }

    override fun onFileLoaded(file: File) {
        (mContext as Activity).runOnUiThread {
            display(file)
        }
    }

    override fun onFileLoadError(e: Exception) {
        mOnErrorListener?.onFileLoadError(e)
    }

    class Builder(private val rootView: View) {

        private var context: Context = rootView.context

        private var pdfView: PdfViewInterface = PdfViewerRecyclerView(context)

        private var quality: PdfPageQuality = PdfPageQuality.QUALITY_1080

        private var maxZoom: Float = 3f

        private var isZoomEnabled: Boolean = true

        private var onPageChangedListener: OnPageChangedListener? = null

        private var onErrorListener: OnErrorListener? = null

        fun view(pdfView: PdfViewInterface): PdfViewer.Builder {
            this.pdfView = pdfView
            return this
        }

        fun setZoomEnabled(isEnabled: Boolean): PdfViewer.Builder {
            this.isZoomEnabled = isEnabled
            return this
        }

        fun setMaxZoom(maxZoom: Float): PdfViewer.Builder {
            this.maxZoom = maxZoom
            return this
        }

        fun setOnPageChangedListener(onPageChangedListener: OnPageChangedListener): PdfViewer.Builder {
            this.onPageChangedListener = onPageChangedListener
            return this
        }

        fun quality(quality: PdfPageQuality): PdfViewer.Builder {
            this.quality = quality
            return this
        }

        fun setOnErrorListener(onErrorListener: OnErrorListener): PdfViewer.Builder {
            this.onErrorListener = onErrorListener
            return this
        }

        fun build(): PdfViewer {
            val pdfViewer = PdfViewer(rootView as ViewGroup)
            pdfView.setQuality(quality.value)
            pdfView.setZoomEnabled(isZoomEnabled)
            pdfView.setMaxZoom(maxZoom)
            pdfView.setOnPageChangedListener(onPageChangedListener)
            pdfViewer.mOnErrorListener = onErrorListener
            pdfViewer.mView = pdfView
            return pdfViewer
        }
    }
}