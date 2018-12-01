package com.redcrew.redcrew.utils.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import rx.Single

/**
 * Created by Gökberk Erüst on 6.09.2018.
 *
 */
interface APIService {

    @POST("sendSms")
    fun sendSms(@Body requestModel: SendSmsRequestModel) : Single<SendSmsResponseModel>


    companion object {
        private val headers = hashMapOf<String, String>()

        fun addHeader(key: String, value: String) {
            headers.put(key, value)
        }

        fun deleteHeader(key: String) {
            headers.remove(key)
        }

        fun clearHeaders() {
            headers.clear()
        }

        fun create(): APIService {

            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val httpClient = OkHttpClient().newBuilder().addInterceptor(loggingInterceptor)

            val interceptor = Interceptor { chain ->
                val builder = chain.request()?.newBuilder()
                for (key in headers.keys) {
                    headers[key]?.let {  builder?.addHeader(key, it) }
                }
                val request = builder?.build()
                request?.let { chain.proceed(it) }
            }

            httpClient.networkInterceptors().add(interceptor)
            val baseUrl: String = "https://apigateway-dev.vodafone.com.tr/"
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(httpClient.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
            return retrofit.create(APIService::class.java)
        }
    }


}