package com.fatballfish.palmschool.logic.model.user

data class UserInfoResponse(val id: Int, val status: Int, val message: String, val data: Data) {
    data class Data(
        val username: String,
        val nickname: String,
        val email: String,
        val portrait: String,
        val phone: String,
        val gender: String,
        val birthday: Long,
        val real_auth: RealAuth
    )

    data class RealAuth(
        val id: Int,
        val name: String,
        val school: String,
        val dept: String,
        val major: String,
        val cls: String
    )
}