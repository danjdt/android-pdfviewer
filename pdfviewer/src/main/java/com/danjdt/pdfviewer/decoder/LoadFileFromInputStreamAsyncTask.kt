package com.danjdt.pdfviewer.decoder

import android.os.AsyncTask
import com.danjdt.pdfviewer.interfaces.OnLoadFileListener
import java.io.File
import java.io.InputStream
import java.lang.Exception

/**
 * Created by daniel.teixeira on 23/01/19
 */
class LoadFileFromInputStreamAsyncTask(private val outFile: File, private val listener: OnLoadFileListener, private val input: InputStream) : AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg void: Void?): Void? {
        try {
            listener.onFileLoaded(LoadFileDelegate(input, outFile).doLoadFile())

        } catch (e : Exception) {
            listener.onFileLoadError(e)
        }
        return null
    }
}