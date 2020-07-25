package com.fatballfish.palmschool.logic.network

import com.fatballfish.palmschool.logic.model.PassLoginRequest
import com.fatballfish.palmschool.logic.model.SmsCaptchaCreateRequest
import com.fatballfish.palmschool.logic.model.SmsCaptchaValidateRequest
import com.fatballfish.palmschool.logic.model.SmsLoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object PalmSchoolNetwork {
    // 登录服务
    private val loginService = ServiceCreator.create<UserLoginService>()

    // 短信登录
    suspend fun smsLogin(smsLoginRequest: SmsLoginRequest) =
        loginService.smsLogin(smsLoginRequest).await()

    // 密码登录
    suspend fun passLogin(passLoginRequest: PassLoginRequest) =
        loginService.passLogin(passLoginRequest).await()

    // 验证码服务
    private val smsCaptchaService = ServiceCreator.create<SmsCaptchaService>()

    // 创建短信验证码
    suspend fun createSmsCaptcha(smsCaptchaCreateRequest: SmsCaptchaCreateRequest) =
        smsCaptchaService.createSmsCaptcha(smsCaptchaCreateRequest).await()

    // 检验短信验证码
    suspend fun validateSmsCaptcha(smsCaptchaValidateRequest: SmsCaptchaValidateRequest) =
        smsCaptchaService.validateSmsCaptcha(smsCaptchaValidateRequest).await()
    // 注册服务


    // 高级拓展函数，实现请求回调
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}