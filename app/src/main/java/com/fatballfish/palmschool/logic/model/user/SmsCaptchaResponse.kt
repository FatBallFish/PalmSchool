package com.fatballfish.palmschool.logic.model.user

data class SmsCaptchaCreateResponse(
    val id: Int,
    val status: Int,
    val message: String,
    val data: Data
) {
    data class Data(val rand: String)
}

data class SmsCaptchaValidateResponse(
    val id: Int,
    val status: Int,
    val message: String,
    val data: Data
) {
    class Data
}