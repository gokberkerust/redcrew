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
import retrofit2.HttpException
import rx.Single
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Gökberk Erüst on 1.12.2018.
 *
 */
class ApprovalActivity : AppCompatActivity() {

    private lateinit var service: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approval)
        initApiServices()
        initLayout()
    }

    private fun initLayout() {
        val qrCode = intent.getSerializableExtra(QR_CODE_KEY) as QrReaderActivity.QrCodes
        when (qrCode) {

            QrReaderActivity.QrCodes.Donate -> {
                charityLayout.visibility = View.VISIBLE
            }
            QrReaderActivity.QrCodes.Internet1, QrReaderActivity.QrCodes.Internet2, QrReaderActivity.QrCodes.Internet3 -> {
                startActivity(ResultActivity.newIntent(applicationContext, qrCode))
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
                finish()
            }
            QrReaderActivity.QrCodes.Tariff -> {
                tariffLayout.visibility = View.VISIBLE
            }
            QrReaderActivity.QrCodes.SMS -> {
                smsLayout.visibility = View.VISIBLE
            }
            else -> {
                finish()
            }
        }
        approveButton.setOnClickListener {
            startActivity(ResultActivity.newIntent(applicationContext, qrCode))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }
    }

    private fun sendSms() {
        request(
            service.sendSms(
                SendSmsRequestModel(
                    SendSmsRequestPayload(
                        endUserId = 905423872239,
                        message = "Test message"
                    )
                )
            ), {
            }, {

            })
    }

    private fun initApiServices() {
        APIService.addHeader("apikey", "l7xxe51c6d131fc34a5caccbef3e35d045678")
        APIService.addHeader("userIp", "172.24.10.62")
        service = APIService.create()
    }

    private fun <T : Any> request(
        request: Single<T>,
        responseHandler: (response: T?) -> Unit,
        errorHandler: (error: HttpException) -> Unit
    ): Subscription {

        val singleObject = request.subscribeOn(Schedulers.io())
        return singleObject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    responseHandler(result)
                },
                { error ->
                    if (error is HttpException) {
                        errorHandler(error)
                    }
                })

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