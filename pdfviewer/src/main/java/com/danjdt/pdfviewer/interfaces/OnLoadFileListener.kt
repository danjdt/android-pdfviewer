package com.danjdt.pdfviewer.interfaces

import java.io.File
import java.lang.Exception

interface OnLoadFileListener {

    fun onFileLoaded(file: File)

    fun onFileLoadError(e: Exception)
}