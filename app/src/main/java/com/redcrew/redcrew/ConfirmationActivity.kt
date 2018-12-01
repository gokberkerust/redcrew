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

/**
 * Created by Gökberk Erüst on 1.12.2018.
 *
 */
class ConfirmationActivity : AppCompatActivity() {

    private lateinit var service: APIService

    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initApiServices()
        initLayout()
    }

    private fun initLayout() {
        mainActivityText.text = (intent.getSerializableExtra(QR_CODE_KEY) as QrReaderActivity.QrCodes).text
        sendSms()
    }

    private fun sendSms(){
        request(
            service.sendSms(
                SendSmsRequestModel(
                    SendSmsRequestPayload(
                        endUserId = 905423872518,
                        message = "Test message"
                    )
                )
            ), {
                println("response: ${it?.sendSmsResponse?.resultCode}")
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
            val intent = Intent(context, ConfirmationActivity::class.java)
            intent.putExtra(QR_CODE_KEY, qrCode)
            return intent
        }
    }
}