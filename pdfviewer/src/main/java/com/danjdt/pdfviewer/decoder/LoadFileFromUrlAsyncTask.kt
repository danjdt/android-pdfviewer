package com.danjdt.pdfviewer.decoder

import android.os.AsyncTask
import com.danjdt.pdfviewer.interfaces.OnLoadFileListener
import java.io.File
import java.net.URL

/**
 * Created by daniel.teixeira on 23/01/19
 */

class LoadFileFromUrlAsyncTask(private val outFile: File, private val listener: OnLoadFileListener, private val url: String) :
    AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg void: Void?): Void? {
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