package com.redcrew.redcrew

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener { view ->
            startActivity(QrReaderActivity.newIntent(applicationContext))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }
    }
}
