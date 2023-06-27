package com.danjdt.pdfviewer.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point

/**
 * Created by daniel.teixeira on 24/01/19
 */
class Utils {

    companion object {

        fun getScreenHeight(activity: Activity): Int {
            val display = activity.windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.y
        }
    }
}