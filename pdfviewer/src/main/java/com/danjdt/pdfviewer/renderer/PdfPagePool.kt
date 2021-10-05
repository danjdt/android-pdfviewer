package com.danjdt.pdfviewer.renderer

import android.graphics.Bitmap
import java.util.*

class PdfPagePool {

    private val mMaxPoolSizeInBytes = 100 * 1024 * 1024

    private var mPoolSizeInBytes = 0

    private val mPool: HashMap<Int, Bitmap?> = HashMap()

    private val mRemoveQueue: Queue<Int> = LinkedList()

    fun put(position: Int, bitmap: Bitmap) {
        if (mPool[position] == null) {
            while (mPoolSizeInBytes > mMaxPoolSizeInBytes) {
                removeLast()
            }

            mRemoveQueue.offer(position)

            mPool[position] = bitmap
            mPoolSizeInBytes += bitmap.byteCount
        }
    }

    fun get(position: Int) : Bitmap? {
        return mPool[position]
    }

    fun exists(position: Int): Boolean {
        return mPool[position] != null
    }

    private fun removeLast() {
        mRemoveQueue.poll()?.let { removePos ->
            mPoolSizeInBytes -= mPool[removePos]!!.byteCount
            mPool.remove(removePos)
        }
    }
}