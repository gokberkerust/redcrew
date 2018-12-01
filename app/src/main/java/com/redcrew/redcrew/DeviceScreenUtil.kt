package com.redcrew.redcrew

import android.content.Context
import android.view.WindowManager

/**
 * Created by Gökberk Erüst on 1.12.2018.
 *
 */
class DeviceScreenUtil {

    companion object {

        fun getScreenHeight(context: Context): Int {
            val displayMetrics = context.resources.displayMetrics
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.heightPixels
        }

        fun getScreenWidth(context: Context): Int {
            val displayMetrics = context.resources.displayMetrics
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }

    }
}