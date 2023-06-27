package com.danjdt.pdfviewer.interfaces

import android.view.View
import com.danjdt.pdfviewer.utils.PdfPageQuality
import java.io.File

interface PdfViewInterface {

    fun getView(): View

    fun setup(file: File)

    fun setZoomEnabled(isZoomEnabled: Boolean)

    fun setMaxZoom(maxZoom: Float)

    fun setQuality(quality: PdfPageQuality)

    fun setOnPageChangedListener(onPageChangedListener: OnPageChangedListener?)
}