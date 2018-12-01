package com.redcrew.redcrew

import android.app.Application

/**
 * Created by Gökberk Erüst on 1.12.2018.
 *
 */
class RedCrewApp: Application() {

    companion object {
        const val COUNTER_TIME_KEY = "counterTime"

        var listener: QrReaderActivity.Listener? = null
    }
}