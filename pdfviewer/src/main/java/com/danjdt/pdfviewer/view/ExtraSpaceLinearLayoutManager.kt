package com.danjdt.pdfviewer.view

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class ExtraSpaceLinearLayoutManager(context: Context) : LinearLayoutManager(context) {

    var isScrollEnabled = true

    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically() && isScrollEnabled
    }
}