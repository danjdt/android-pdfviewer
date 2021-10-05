package com.danjdt.pdfviewer.decoder

import android.content.Context
import android.net.Uri
import androidx.annotation.RawRes
import com.danjdt.pdfviewer.interfaces.OnLoadFileListener
import java.io.File
import java.io.InputStream

/**
 * Created by daniel.teixeira on 23/01/19
 */
class FileLoader {

    companion object {

        private const val FILE_NAME = "pdfView.pdf"

        private fun getTempFile(context : Context) : File {
            return File(context.cacheDir, FILE_NAME)
        }

        fun loadFile(context: Context, listener: OnLoadFileListener, @RawRes resId: Int) {
            val input = context.resources
                .openRawResource(
                    context.resources
                        .getIdentifier(
                            context.resources.getResourceName(resId),
                            context.resources.getResourceTypeName(resId),
                            context.resources.getResourcePackageName(resId)
                        )
                )

            LoadFileFromInputStreamAsyncTask(getTempFile(context), listener, input).execute()
        }

        fun loadFile(context: Context, listener: OnLoadFileListener, url: String) {
            LoadFileFromUrlAsyncTask(getTempFile(context), listener, url).execute()
        }

        fun loadFile(context: Context, listener: OnLoadFileListener, uri: Uri) {
            val input = context.contentResolver.openInputStream(uri)
            input?.let { LoadFileFromInputStreamAsyncTask(getTempFile(context), listener, input).execute() }
        }

        fun loadFile(context: Context, listener: OnLoadFileListener, input: InputStream) {
            LoadFileFromInputStreamAsyncTask(getTempFile(context), listener, input).execute()
        }
    }
}