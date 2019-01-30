package com.danjdt.pdfviewer.view

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danjdt.pdfviewer.utils.Utils

/**
 * Created by daniel.teixeira on 20/01/19
 */
class ExtraSpaceLinearLayoutManager(private val context: Context?) : LinearLayoutManager(context) {

    override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
        return Utils.getScreenHeight(context as Activity)
    }
}