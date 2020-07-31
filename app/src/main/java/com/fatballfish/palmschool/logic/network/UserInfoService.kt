package com.fatballfish.palmschool.logic.network

import com.fatballfish.palmschool.logic.model.user.UserInfoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserInfoService {
    @GET("user/info/get/")
    fun getUserInfo(@Query("token") token: String): Call<UserInfoResponse>

}