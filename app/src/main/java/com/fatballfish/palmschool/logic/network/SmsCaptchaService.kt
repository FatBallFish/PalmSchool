package com.fatballfish.palmschool.logic.network

import com.fatballfish.palmschool.logic.model.SmsCaptchaCreateRequest
import com.fatballfish.palmschool.logic.model.SmsCaptchaCreateResponse
import com.fatballfish.palmschool.logic.model.SmsCaptchaValidateRequest
import com.fatballfish.palmschool.logic.model.SmsCaptchaValidateResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SmsCaptchaService {
    @POST("captcha/sms/create/")
    fun createSmsCaptcha(@Body smsCaptchaCreateRequest: SmsCaptchaCreateRequest): Call<SmsCaptchaCreateResponse>

    @POST("captcha/sms/validate/")
    fun validateSmsCaptcha(@Body smsCaptchaValidateRequest: SmsCaptchaValidateRequest): Call<SmsCaptchaValidateResponse>
}