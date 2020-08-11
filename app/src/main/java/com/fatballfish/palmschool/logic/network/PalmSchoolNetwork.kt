package com.fatballfish.palmschool.logic.network

import com.fatballfish.palmschool.logic.model.lesson.LessonTemplateListRequest
import com.fatballfish.palmschool.logic.model.lesson.LessonTemplateService
import com.fatballfish.palmschool.logic.model.realAuth.RealAuthCreateRequest
import com.fatballfish.palmschool.logic.model.realAuth.RealAuthUpdateRequest
import com.fatballfish.palmschool.logic.model.template.CurrentTemplateUpdateRequest
import com.fatballfish.palmschool.logic.model.user.*
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

    // 课程服务
    val lessonTemplateService = ServiceCreator.create<LessonTemplateService>()

    // 获取个人课程列表
    suspend fun getLessonTemplateList(lessonTemplateListRequest: LessonTemplateListRequest) =
        lessonTemplateService.getLessonTemplateList(
            lessonTemplateListRequest.token,
            lessonTemplateListRequest.tid
        ).await()

    // 模版服务
    val templateService = ServiceCreator.create<TemplateService>()
    suspend fun getTemplateList(token: String, map: Map<String, String>) =
        templateService.getTemplateList(token, map).await()

    // 获取当前用户所使用的模板
    suspend fun getCurrentTemplateId(token: String) =
        templateService.getCurrentTemplateId(token).await()

    // 更新当前用户所使用的模版
    suspend fun updateCurrentTemplateId(token: String, tid: CurrentTemplateUpdateRequest) =
        templateService.updateCurrentTemplateId(token, tid).await()

    // 用户信息服务
    val userInfoService = ServiceCreator.create<UserInfoService>()

    // 获取用户信息
    suspend fun getUserInfo(token: String) = userInfoService.getUserInfo(token).await()

    // 更新用户信息
    suspend fun updateUserInfo(token: String, data: UserInfoUpdateRequest) =
        userInfoService.updateUserInfo(token, data).await()

    // 头像服务
    val portraitService = ServiceCreator.create<PortraitService>()

    // 头像上传
    suspend fun uploadPortrait(token: String, data: PortraitUploadRequest) =
        portraitService.uploadPortrait(token, data).await()

    // 实名认证服务
    val realAuthService = ServiceCreator.create<RealAuthService>()

    // 实名信息更新
    suspend fun updateRealAuth(token: String, data: RealAuthUpdateRequest) =
        realAuthService.updateRealAuth(token, data).await()

    // 实名信息创建
    suspend fun createRealAuth(token: String, data: RealAuthCreateRequest) =
        realAuthService.createRealAuth(token, data).await()

    // 学校服务
    val schoolService = ServiceCreator.create<SchoolService>()

    // 获取学校列表
    suspend fun getSchoolList(token: String, map: Map<String, String>) =
        schoolService.getSchoolList(token, map).await()

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