package com.danjdt.pdfviewer.decoder

import android.content.Context
import android.net.Uri
import androidx.annotation.RawRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URL

class FileLoader {

    companion object {

        private const val FILE_NAME = "temp.pdf"

        private fun getTempFile(context: Context): File {
            return File(context.cacheDir, FILE_NAME)
        }

        suspend fun loadFile(context: Context, @RawRes resId: Int): File {
            return withContext(Dispatchers.IO) {
                val input = context.resources
                    .openRawResource(
                        context.resources
                            .getIdentifier(
                                context.resources.getResourceName(resId),
                                context.resources.getResourceTypeName(resId),
                                context.resources.getResourcePackageName(resId)
                            )
                    )

                LoadFileDelegate(input = input, file = getTempFile(context)).doLoadFile()
            }
        }

        suspend fun loadFile(context: Context, url: String): File {
            return withContext(Dispatchers.IO) {
                val imageUrl = URL(url)
                val urlConnection = imageUrl.openConnection()
                val input = urlConnection.getInputStream()
                LoadFileDelegate(input = input, file = getTempFile(context)).doLoadFile()
            }
        }

        suspend fun loadFile(context: Context, input: InputStream): File {
            return withContext(Dispatchers.IO) {
                LoadFileDelegate(input = input, file = getTempFile(context)).doLoadFile()
            }
        }

        suspend fun loadFile(context: Context,  uri: Uri): File {
            return withContext(Dispatchers.IO) {
                val input = context.contentResolver.openInputStream(uri)
                input?.let {
                    LoadFileDelegate(input = input, file = getTempFile(context)).doLoadFile()
                } ?: throw FileNotFoundException()
            }
        }
    }
}