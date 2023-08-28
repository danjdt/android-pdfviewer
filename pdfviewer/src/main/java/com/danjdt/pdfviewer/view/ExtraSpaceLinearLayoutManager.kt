package com.danjdt.pdfviewer.view

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ExtraSpaceLinearLayoutManager(context: Context) : LinearLayoutManager(context) {

    var isScrollEnabled = true

    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically() && isScrollEnabled
    }
}