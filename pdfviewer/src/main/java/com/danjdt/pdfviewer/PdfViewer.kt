package com.danjdt.pdfviewer

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.annotation.RawRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.pdfviewer.decoder.FileLoader
import com.danjdt.pdfviewer.interfaces.OnLoadFileListener
import com.danjdt.pdfviewer.interfaces.OnErrorListener
import com.danjdt.pdfviewer.interfaces.OnPageChangedListener
import com.danjdt.pdfviewer.interfaces.PdfViewInterface
import com.danjdt.pdfviewer.utils.PdfPageQuality
import com.danjdt.pdfviewer.view.PdfViewerRecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.Exception

class PdfViewer private constructor(
    private val rootView: ViewGroup,
    private val pdfView: PdfViewInterface,
    private val errorListener: OnErrorListener? = null
) : OnLoadFileListener {

    private val context: Context by lazy { rootView.context }

    private fun display(file: File) {
        try {
            rootView.addView(pdfView.getView())
            pdfView.setup(file)
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
            FileLoader.loadFile(context, resId)
                .flowOn(Dispatchers.IO)
                .catch {
                    errorListener?.onFileLoadError(Exception())
                }
                .collect {
                    display(it)
                }
        }
    }

    fun load(input: InputStream) {
        CoroutineScope(Dispatchers.Main).launch {
            FileLoader.loadFile(context, input)
                .flowOn(Dispatchers.IO)
                .catch {
                    errorListener?.onFileLoadError(Exception())
                }
                .collect {
                    display(it)
                }
        }
    }

    fun load(url: String) {
        CoroutineScope(Dispatchers.Main).launch {
            FileLoader.loadFile(context, url)
                .flowOn(Dispatchers.IO)
                .catch {
                    errorListener?.onFileLoadError(Exception())
                }
                .collect {
                    display(it)
                }
        }
    }

    override fun onFileLoaded(file: File) {
        (context as Activity).runOnUiThread {
            display(file)
        }
    }

    override fun onFileLoadError(e: Exception) {
        errorListener?.onFileLoadError(e)
    }

    class Builder(private val rootView: ViewGroup) {

        private val context: Context = rootView.context

        private var pdfView: PdfViewInterface = PdfViewerRecyclerView(context)

        private var quality: PdfPageQuality = PdfPageQuality.QUALITY_1080

        private var maxZoom: Float = 3f

        private var isZoomEnabled: Boolean = true

        private var onPageChangedListener: OnPageChangedListener? = null

        private var onErrorListener: OnErrorListener? = null

        fun view(pdfView: PdfViewInterface): Builder {
            this.pdfView = pdfView
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
            val pdfViewer = PdfViewer(rootView, pdfView, onErrorListener)
            pdfView.setQuality(quality)
            pdfView.setZoomEnabled(isZoomEnabled)
            pdfView.setMaxZoom(maxZoom)
            pdfView.setOnPageChangedListener(onPageChangedListener)
            return pdfViewer
        }
    }
}