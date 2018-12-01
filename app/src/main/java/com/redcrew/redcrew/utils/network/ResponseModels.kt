package com.redcrew.redcrew.utils.network

import com.google.gson.annotations.SerializedName

/**
 * Created by Gökberk Erüst on 9.09.2018.
 *
 */
data class GenericResponseModel(
    @SerializedName("resultCode") val resultCode: String,
    @SerializedName("resultDescription") val resultDescription: String
)

data class SendSmsResponseModel(
    @SerializedName("sendSmsResponse") val sendSmsResponse: GenericResponseModel
)

data class GetSmsResponsePayload(
    @SerializedName("resultCode") val resultCode: String,
    @SerializedName("resultDescription") val resultDescription: String,
    @SerializedName("numberOfMessagesInThisBatch") val numberOfMessagesInThisBatch: Int,
    @SerializedName("totalNumberOfPendingMessages") val totalNumberOfPendingMessages: Int,
    @SerializedName("smsList") val smsList: SmsModel
)

data class SmsModel(
    @SerializedName("dateTime") val dateTime: Long,
    @SerializedName("destinationAddress") val destinationAddress: String,
    @SerializedName("messageId") val messageId: String,
    @SerializedName("message") val message: String,
    @SerializedName("capexSubscriberId") val capexSubscriberId: SubscriberIdModel


    )

data class SubscriberIdModel(
    @SerializedName("endUserId") val endUserId: String,
    @SerializedName("usageId") val usageId: String
)

data class GetSmsResponseModel(
    @SerializedName("getSmsResponse") val getSmsResponse: GetSmsResponsePayload

)