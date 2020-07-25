package com.fatballfish.palmschool.logic.network

import com.fatballfish.palmschool.logic.model.PassLoginRequest
import com.fatballfish.palmschool.logic.model.SmsLoginRequest
import com.fatballfish.palmschool.logic.model.UserLoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserLoginService {
//    @Headers("content-type:application/json")
    @POST("user/login/sms/")
    fun smsLogin(@Body data: SmsLoginRequest): Call<UserLoginResponse>

//    @Headers("content-type:application/json")
    @POST("user/login/pass/")
    fun passLogin(@Body data: PassLoginRequest): Call<UserLoginResponse>
}