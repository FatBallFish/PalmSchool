package com.fatballfish.palmschool.logic.model

import com.google.gson.annotations.SerializedName

data class UserLoginResponse(val id: Int, val status: Int, val message: String, val data: Data) {
    data class Data(
        val token: String,
        @SerializedName("login_type") val loginType: String
    )
}

