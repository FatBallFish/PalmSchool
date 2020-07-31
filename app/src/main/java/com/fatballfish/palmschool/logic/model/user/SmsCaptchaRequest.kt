package com.fatballfish.palmschool.logic.model.user

import com.google.gson.annotations.SerializedName

data class SmsCaptchaCreateRequest(
    val phone: String,
    @SerializedName("command_type") val commandType: Int = 3
)

data class SmsCaptchaValidateRequest(val hash: String)