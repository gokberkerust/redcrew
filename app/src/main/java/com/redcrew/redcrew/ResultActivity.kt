package com.redcrew.redcrew

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.redcrew.redcrew.utils.network.APIService
import kotlinx.android.synthetic.main.activity_result.*
import java.util.*

class ResultActivity : AppCompatActivity() {

    private lateinit var service: APIService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        initLayout()
    }

    private fun initLayout() {

        val qrCode = intent.getSerializableExtra(ResultActivity.QR_CODE_KEY) as QrReaderActivity.QrCodes
        when (qrCode) {
            QrReaderActivity.QrCodes.Donate -> {
                charityResultLayout.visibility = View.VISIBLE
            }
            QrReaderActivity.QrCodes.Tariff -> {
                tariffResultLayout.visibility = View.VISIBLE
            }
            QrReaderActivity.QrCodes.SMS -> {
                smsResultLayout.visibility = View.VISIBLE
            }
            QrReaderActivity.QrCodes.Internet1, QrReaderActivity.QrCodes.Internet2, QrReaderActivity.QrCodes.Internet3 -> {
                val mbList = listOf("100 MB", "250 MB", "500 MB")
                val randIndex = Random().nextInt(2)
                internetMbAmount.text = mbList[randIndex]
                internetResultLayout.visibility = View.VISIBLE
            }
            else -> {
                finish()
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }

    companion object {

        private const val QR_CODE_KEY = "qrCode"

        fun newIntent(context: Context, qrCode: QrReaderActivity.QrCodes): Intent {
            val intent = Intent(context, ResultActivity::class.java)
            intent.putExtra(QR_CODE_KEY, qrCode)
            return intent
        }
    }
}