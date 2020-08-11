package com.fatballfish.palmschool.logic.model.user

data class UserInfoUpdateRequest(
    val nickname: String? = null,
    val email: String? = null,
    val gender: String? = null,
    val birthday: Long? = null
)