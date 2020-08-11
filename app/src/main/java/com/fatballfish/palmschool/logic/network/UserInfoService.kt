package com.fatballfish.palmschool.logic.network

import com.fatballfish.palmschool.logic.model.user.UserInfoResponse
import com.fatballfish.palmschool.logic.model.user.UserInfoUpdateRequest
import com.fatballfish.palmschool.logic.model.user.UserInfoUpdateResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserInfoService {
    @GET("user/info/get/")
    fun getUserInfo(@Query("token") token: String): Call<UserInfoResponse>

    @POST("user/info/update/")
    fun updateUserInfo(
        @Query("token") token: String,
        @Body data: UserInfoUpdateRequest
    ): Call<UserInfoUpdateResponse>
}