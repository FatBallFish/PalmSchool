package com.fatballfish.palmschool.logic.model.user

data class UserInfoUpdateResponse(
    val id: Int,
    val status: Int,
    val message: String,
    val data: Data
) {
    class Data()
}