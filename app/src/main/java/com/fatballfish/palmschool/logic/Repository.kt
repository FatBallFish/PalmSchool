package com.fatballfish.palmschool.logic

import androidx.lifecycle.R
import androidx.lifecycle.liveData
import com.fatballfish.palmschool.logic.model.PassLoginRequest
import com.fatballfish.palmschool.logic.model.SmsCaptchaCreateRequest
import com.fatballfish.palmschool.logic.model.SmsCaptchaValidateRequest
import com.fatballfish.palmschool.logic.model.SmsLoginRequest
import com.fatballfish.palmschool.logic.network.PalmSchoolNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {
    fun smsLogin(smsLoginRequest: SmsLoginRequest) = fire(Dispatchers.IO) {
        val userLoginResponse = PalmSchoolNetwork.smsLogin(smsLoginRequest)
        if (userLoginResponse.status == 0) {
            val data = userLoginResponse.data
            Result.success(data)
        } else {
            Result.failure(RuntimeException("response status is ${userLoginResponse.status},message is ${userLoginResponse.message}"))
        }
    }

    fun passLogin(passLoginRequest: PassLoginRequest) = fire(Dispatchers.IO) {
        val userLoginResponse = PalmSchoolNetwork.passLogin(passLoginRequest)
        if (userLoginResponse.status == 0) {
            val data = userLoginResponse.data
            Result.success(data)
        } else {
            Result.failure(RuntimeException("response status is ${userLoginResponse.status},message is ${userLoginResponse.message}"))
        }
    }

    fun smsCaptchaCreate(smsCaptchaCreateRequest: SmsCaptchaCreateRequest) = fire(Dispatchers.IO) {
        val smsCaptchaCreateResponse = PalmSchoolNetwork.createSmsCaptcha(smsCaptchaCreateRequest)
        if (smsCaptchaCreateResponse.status == 0) {
            val data = smsCaptchaCreateResponse.data
            Result.success(data)
        } else {
            Result.failure(RuntimeException("response status is ${smsCaptchaCreateResponse.status},message is ${smsCaptchaCreateResponse.message}"))
        }
    }
    fun smsCaptchaValidata(smsCaptchaValidateRequest: SmsCaptchaValidateRequest) = fire(Dispatchers.IO) {
        val smsCaptchaValidateResponse = PalmSchoolNetwork.validateSmsCaptcha(smsCaptchaValidateRequest)
        if (smsCaptchaValidateResponse.status == 0) {
            val data = smsCaptchaValidateResponse.data
            Result.success(data)
        } else {
            Result.failure(RuntimeException("response status is ${smsCaptchaValidateResponse.status},message is ${smsCaptchaValidateResponse.message}"))
        }
    }


    // 简化请求模式，不用频繁调用try
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }
}