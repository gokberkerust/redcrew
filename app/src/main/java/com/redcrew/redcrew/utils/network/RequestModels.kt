package com.redcrew.redcrew.utils.network

import com.google.gson.annotations.SerializedName

/**
 * Created by Gökberk Erüst on 14.09.2018.
 *
 */
data class SendSmsRequestPayload(
    @SerializedName("endUserId") val endUserId: Long,
    @SerializedName("message") val message: String,
    @SerializedName("senderAddress") val senderAddress: String = "795375"
)

data class SendSmsRequestModel(
    @SerializedName("sendSmsRequest") val sendSmsRequest: SendSmsRequestPayload
)