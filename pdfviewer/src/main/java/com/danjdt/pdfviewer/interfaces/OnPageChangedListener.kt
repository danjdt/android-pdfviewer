package com.danjdt.pdfviewer.interfaces

fun interface OnPageChangedListener {

    fun onPageChanged(page : Int, total : Int)
}