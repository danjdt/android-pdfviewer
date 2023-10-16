package com.danjdt.pdfviewer.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class PdfPageViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    abstract fun bind(position: Int)
}