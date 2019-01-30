package com.danjdt.pdfviewer.interfaces

/**
 * Created by daniel.teixeira on 22/01/19
 */
interface OnPageChangedListener {

    fun onPageChanged(page : Int, total : Int)
}