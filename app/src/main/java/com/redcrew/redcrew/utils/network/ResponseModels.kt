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