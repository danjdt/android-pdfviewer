package com.danjdt.pdfviewer.decoder

import com.danjdt.pdfviewer.interfaces.OnLoadFileListener
import com.example.background.CoroutinesAsyncTask
import java.io.File
import java.io.InputStream
import java.lang.Exception

/**
 * Created by daniel.teixeira on 23/01/19
 */
class LoadFileFromResAsyncTask(private val outFile: File, private val listener: OnLoadFileListener, private val input: InputStream) : CoroutinesAsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        try {
            listener.onFileLoaded(LoadFileDelegate(input, outFile).doLoadFile())

        } catch (e : Exception) {
            listener.onFileLoadError(e)
        }
        return null
    }
}