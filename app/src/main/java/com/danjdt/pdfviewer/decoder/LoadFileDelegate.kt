package com.danjdt.pdfviewer.decoder

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Created by daniel.teixeira on 23/01/19
 */
class LoadFileDelegate(private val input: InputStream, private val file: File) {

    fun doLoadFile(): File {
        val output = FileOutputStream(file)
        val buffer = ByteArray(BUFFER_SIZE)
        var read: Int = input.read(buffer)

        while ((read) != -1) {
            output.write(buffer, 0, read)
            read = input.read(buffer)
        }

        output.flush()
        return file
    }

    companion object {
        private const val BUFFER_SIZE = 4 * 1024
    }
}