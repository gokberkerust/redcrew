package com.redcrew.redcrew

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.redcrew.redcrew.utils.network.APIService
import com.redcrew.redcrew.utils.network.SendSmsRequestModel
import com.redcrew.redcrew.utils.network.SendSmsRequestPayload
import kotlinx.android.synthetic.main.activity_approval.*
import kotlinx.android.synthetic.main.activity_qr.*
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.HttpException
import rx.Single
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
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


    companion object {

        private const val QR_CODE_KEY = "qrCode"

        fun newIntent(context: Context, qrCode: QrReaderActivity.QrCodes): Intent {
            val intent = Intent(context, ResultActivity::class.java)
            intent.putExtra(QR_CODE_KEY, qrCode)
            return intent
        }
    }
}