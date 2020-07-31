package com.fatballfish.palmschool.logic.model.user

import com.google.gson.annotations.SerializedName

data class SmsLoginRequest(val username: String, val hash: String, val enduring: Boolean = false)

data class PassLoginRequest(
    val username: String,
    val password: String,
    val enduring: Boolean = false
)