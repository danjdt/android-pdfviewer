@file:Suppress("unused")

package com.danjdt.pdfviewer.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ZoomableLinearLayoutManager : LinearLayoutManager {
    private var recyclerView: ZoomableRecyclerView? = null

    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout,
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        if (view is ZoomableRecyclerView) {
            this.recyclerView = view
        }
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?,
    ): Int {
        val scrollAmount = this.recyclerView?.calculateScrollAmount(dy) ?: dy
        return super.scrollVerticallyBy(scrollAmount, recycler, state)
    }
}