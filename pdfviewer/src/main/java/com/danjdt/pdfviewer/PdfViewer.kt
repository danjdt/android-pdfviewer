package com.danjdt.pdfviewer

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.RawRes
import com.danjdt.pdfviewer.decoder.FileLoader
import com.danjdt.pdfviewer.interfaces.OnErrorListener
import com.danjdt.pdfviewer.interfaces.OnPageChangedListener
import com.danjdt.pdfviewer.interfaces.PdfViewController
import com.danjdt.pdfviewer.utils.PdfPageQuality
import com.danjdt.pdfviewer.view.PdfViewControllerImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.Exception

class PdfViewer private constructor(
    pdfViewController: PdfViewController,
    rootView: ViewGroup,
    private val errorListener: OnErrorListener? = null
) : PdfViewController by pdfViewController {

    private val context: Context by lazy { rootView.context }

    init {
        try {
            rootView.addView(getView())
        } catch (e: IOException) {
            errorListener?.onAttachViewError(e)
        }
    }

    private fun display(file: File) {
        try {
            setup(file)
        } catch (e: IOException) {
            errorListener?.onPdfRendererError(e)
        } catch (e: Exception) {
            errorListener?.onAttachViewError(e)
        }
    }

    fun load(file: File) {
        display(file)
    }

    fun load(@RawRes resId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                FileLoader.loadFile(context, resId)
            }.onFailure {
                errorListener?.onFileLoadError(Exception())
            }.onSuccess {
                display(it)
            }
        }
    }

    fun load(input: InputStream) {
        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                FileLoader.loadFile(context, input)
            }.onFailure {
                errorListener?.onFileLoadError(Exception())
            }.onSuccess {
                display(it)
            }
        }
    }

    fun load(url: String) {
        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                FileLoader.loadFile(context, url)
            }.onFailure {
                errorListener?.onFileLoadError(Exception())
            }.onSuccess {
                display(it)
            }
        }
    }

    class Builder(private val rootView: ViewGroup) {

        private val context: Context = rootView.context

        private var pdfViewController: PdfViewController = PdfViewControllerImpl(context)

        private var quality: PdfPageQuality = PdfPageQuality.QUALITY_1080

        private var maxZoom: Float = 3f

        private var isZoomEnabled: Boolean = true

        private var onPageChangedListener: OnPageChangedListener? = null

        private var onErrorListener: OnErrorListener? = null

        fun controller(controller: PdfViewController): Builder {
            this.pdfViewController = controller
            return this
        }

        fun setZoomEnabled(isEnabled: Boolean): Builder {
            this.isZoomEnabled = isEnabled
            return this
        }

        fun setMaxZoom(maxZoom: Float): Builder {
            this.maxZoom = maxZoom
            return this
        }

        fun setOnPageChangedListener(onPageChangedListener: OnPageChangedListener): Builder {
            this.onPageChangedListener = onPageChangedListener
            return this
        }

        fun quality(quality: PdfPageQuality): Builder {
            this.quality = quality
            return this
        }

        fun setOnErrorListener(onErrorListener: OnErrorListener): Builder {
            this.onErrorListener = onErrorListener
            return this
        }

        fun build(): PdfViewer {
            val pdfViewer = PdfViewer(pdfViewController, rootView, onErrorListener)
            pdfViewController.setQuality(quality)
            pdfViewController.setZoomEnabled(isZoomEnabled)
            pdfViewController.setMaxZoom(maxZoom)
            pdfViewController.setOnPageChangedListener(onPageChangedListener)
            return pdfViewer
        }
    }
}