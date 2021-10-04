package com.danjdt.pdfviewer.decoder

import com.danjdt.pdfviewer.interfaces.OnLoadFileListener
import com.example.background.CoroutinesAsyncTask
import java.io.File
import java.net.URL

/**
 * Created by daniel.teixeira on 23/01/19
 */

class LoadFileFromUrlAsyncTask(private val outFile: File, private val listener: OnLoadFileListener, private val url: String) :
    CoroutinesAsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        try {
            val imageUrl = URL(url)
            val urlConnection = imageUrl.openConnection()
            val input = urlConnection.getInputStream()
            listener.onFileLoaded(LoadFileDelegate(input, outFile).doLoadFile())

        } catch (e: Exception) {
            listener.onFileLoadError(e)
        }

        return null
    }
}