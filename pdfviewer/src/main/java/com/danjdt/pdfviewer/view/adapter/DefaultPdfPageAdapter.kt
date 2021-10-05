package com.danjdt.pdfviewer.view.adapter

import android.content.Context
import android.os.Build
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView

import androidx.annotation.RequiresApi

/**
 * Created by daniel.teixeira on 15/01/19
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class DefaultPdfPageAdapter(private val context: Context) :
    PdfPageAdapter<DefaultPdfPageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultPdfPageViewHolder {
        val view = ImageView(context).apply {
            tag = TAG
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setPadding(PADDING, PADDING, PADDING, PADDING)
        }
        return DefaultPdfPageViewHolder(view, mPdfRenderer, mPageSize)
    }

    override fun onBindViewHolder(holder: DefaultPdfPageViewHolder, position: Int) {
        holder.bind(position)
    }

    companion object {
        private const val PADDING = 4
        const val DEFAULT_COLOR = 0xFFFFFF
        const val TAG = "DefaultPdfPageAdapterContainer"
    }
}