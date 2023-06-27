package com.danjdt.pdfviewer.decoder

import android.content.Context
import androidx.annotation.RawRes
import com.danjdt.pdfviewer.interfaces.OnLoadFileListener
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.io.File
import java.io.InputStream
import java.net.URL

class FileLoader {

    companion object {

        private const val FILE_NAME = "temp.pdf"

        private fun getTempFile(context: Context): File {
            return File(context.cacheDir, FILE_NAME)
        }

        suspend fun loadFile(context: Context, @RawRes resId: Int) =
            flow {
                val input = context.resources
                    .openRawResource(
                        context.resources
                            .getIdentifier(
                                context.resources.getResourceName(resId),
                                context.resources.getResourceTypeName(resId),
                                context.resources.getResourcePackageName(resId)
                            )
                    )

                emit(LoadFileDelegate(input = input, file = getTempFile(context)).doLoadFile())
            }

        suspend fun loadFile(context: Context, url: String) = flow {
            val imageUrl = URL(url)
            val urlConnection = imageUrl.openConnection()
            val input = urlConnection.getInputStream()
            emit(LoadFileDelegate(input = input, file = getTempFile(context)).doLoadFile())
        }

        suspend fun loadFile(context: Context, input: InputStream) = flow {
            emit(LoadFileDelegate(input = input, file = getTempFile(context)).doLoadFile())
        }
    }
}