package com.redcrew.redcrew

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.redcrew.redcrew.utils.network.APIService
import com.redcrew.redcrew.utils.network.SendSmsRequestModel
import com.redcrew.redcrew.utils.network.SendSmsRequestPayload
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.HttpException
import rx.Single
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ResultActivity : AppCompatActivity() {

    private lateinit var service: APIService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        initLayout()
    }

    private fun initLayout() {

    }


    companion object {

        private const val QR_CODE_KEY = "qrCode"

        fun newIntent(context: Context, qrCode: QrReaderActivity.QrCodes): Intent {
            val intent = Intent(context, ApprovalActivity::class.java)
            intent.putExtra(QR_CODE_KEY, qrCode)
            return intent
        }
    }
}