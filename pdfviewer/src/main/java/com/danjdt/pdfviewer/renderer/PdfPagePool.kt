package com.danjdt.pdfviewer.renderer

import android.graphics.Bitmap
import java.util.*

class PdfPagePool {

    private val maxPoolSizeInBytes = 100 * 1024 * 1024

    private var poolSizeInBytes = 0

    private val pool: HashMap<Int, Bitmap?> = HashMap()

    private val removeQueue: Queue<Int> = LinkedList<Int>()

    fun put(position: Int, bitmap: Bitmap) {
        if (pool[position] == null) {
            while (poolSizeInBytes > maxPoolSizeInBytes) {
                removeLast()
            }

            removeQueue.offer(position)

            pool[position] = bitmap
            poolSizeInBytes += bitmap.byteCount
        }
    }

    fun get(position: Int) : Bitmap? {
        return pool[position]
    }

    fun exists(position: Int): Boolean {
        return pool[position] != null
    }

    fun clear(){
        pool.clear()
        removeQueue.clear()
        poolSizeInBytes = 0
    }

    private fun removeLast() {
        val removePos = removeQueue.poll()
        poolSizeInBytes -= pool[removePos]!!.byteCount
        pool.remove(removePos)
    }
}