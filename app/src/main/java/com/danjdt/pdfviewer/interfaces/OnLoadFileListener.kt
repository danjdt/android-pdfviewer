package com.danjdt.pdfviewer.interfaces

import java.io.File
import java.lang.Exception

/**
 * Created by daniel.teixeira on 23/01/19
 */
interface OnLoadFileListener {

    fun onFileLoaded(file: File)

    fun onFileLoadError(e: Exception)
}