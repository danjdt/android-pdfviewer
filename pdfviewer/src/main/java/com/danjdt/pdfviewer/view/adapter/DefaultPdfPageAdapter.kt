package com.danjdt.pdfviewer.view.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.danjdt.pdfviewer.R

/**
 * Created by daniel.teixeira on 15/01/19
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class DefaultPdfPageAdapter(private val context: Context) :
    PdfPageAdapter<DefaultPdfPageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultPdfPageViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.pdf_page, parent, false)
        return DefaultPdfPageViewHolder(view, mPdfRenderer, mPageSize)
    }

    override fun onBindViewHolder(holder: DefaultPdfPageViewHolder, position: Int) {
        holder.bind(position)
    }
}