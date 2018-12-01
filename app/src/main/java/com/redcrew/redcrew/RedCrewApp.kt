package com.redcrew.redcrew

import android.app.Application

/**
 * Created by Gökberk Erüst on 1.12.2018.
 *
 */
class RedCrewApp: Application() {

    companion object {
        const val COUNTER_TIME_KEY = "counterTime"
        const val COUNTER_TIME_HOUR_LIMIT = 12

        var listener: QrReaderActivity.Listener? = null
    }
}