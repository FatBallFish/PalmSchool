package com.fatballfish.palmschool.logic

import androidx.lifecycle.liveData
import com.fatballfish.palmschool.logic.dao.TemplateDao
import com.fatballfish.palmschool.logic.dao.TokenDao
import com.fatballfish.palmschool.logic.model.lesson.LessonTemplateListRequest
import com.fatballfish.palmschool.logic.model.realAuth.RealAuthCreateRequest
import com.fatballfish.palmschool.logic.model.realAuth.RealAuthUpdateRequest
import com.fatballfish.palmschool.logic.model.template.CurrentTemplateUpdateRequest
import com.fatballfish.palmschool.logic.model.user.*
import com.fatballfish.palmschool.logic.network.PalmSchoolNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {
    // 短信登录
    fun smsLogin(smsLoginRequest: SmsLoginRequest) = fire(Dispatchers.IO) {
        val userLoginResponse = PalmSchoolNetwork.smsLogin(smsLoginRequest)
        if (userLoginResponse.status == 0) {
            val data = userLoginResponse.data
            Result.success(data)
        } else {
            Result.failure(RuntimeException("response status is ${userLoginResponse.status},message is ${userLoginResponse.message}"))
        }
    }

    // 密码登录
    fun passLogin(passLoginRequest: PassLoginRequest) = fire(Dispatchers.IO) {
        val userLoginResponse = PalmSchoolNetwork.passLogin(passLoginRequest)
        if (userLoginResponse.status == 0) {
            val data = userLoginResponse.data
            Result.success(data)
        } else {
            Result.failure(RuntimeException("response status is ${userLoginResponse.status},message is ${userLoginResponse.message}"))
        }
    }

    // 短信验证码创建
    fun smsCaptchaCreate(smsCaptchaCreateRequest: SmsCaptchaCreateRequest) = fire(Dispatchers.IO) {
        val smsCaptchaCreateResponse = PalmSchoolNetwork.createSmsCaptcha(smsCaptchaCreateRequest)
        if (smsCaptchaCreateResponse.status == 0) {
            val data = smsCaptchaCreateResponse.data
            Result.success(data)
        } else {
            Result.failure(RuntimeException("response status is ${smsCaptchaCreateResponse.status},message is ${smsCaptchaCreateResponse.message}"))
        }
    }

    // 短信验证码检验
    fun smsCaptchaValidata(smsCaptchaValidateRequest: SmsCaptchaValidateRequest) =
        fire(Dispatchers.IO) {
            val smsCaptchaValidateResponse =
                PalmSchoolNetwork.validateSmsCaptcha(smsCaptchaValidateRequest)
            if (smsCaptchaValidateResponse.status == 0) {
                val data = smsCaptchaValidateResponse.data
                Result.success(data)
            } else {
                Result.failure(RuntimeException("response status is ${smsCaptchaValidateResponse.status},message is ${smsCaptchaValidateResponse.message}"))
            }
        }

    // 个人课表列表获取
    fun getLessonTemplateList(lessonTemplateListRequest: LessonTemplateListRequest) =
        fire(Dispatchers.IO) {
            val lessonListResponse =
                PalmSchoolNetwork.getLessonTemplateList(lessonTemplateListRequest)
            if (lessonListResponse.status == 0) {
                val data = lessonListResponse.data
                Result.success(data)
            } else {
                Result.failure(RuntimeException("response status is ${lessonListResponse.status},message is ${lessonListResponse.message}"))
            }
        }

    // 模版列表获取
    fun getTemplateList(token: String, map: Map<String, String>) = fire(Dispatchers.IO) {
        val templateListResponse = PalmSchoolNetwork.getTemplateList(token, map)
        if (templateListResponse.status == 0) {
            val data = templateListResponse.data
            Result.success(data)
        } else {
            Result.failure(RuntimeException("response status is ${templateListResponse.status},message is ${templateListResponse.message}"))
        }
    }

    // 获取用户当前所用模版id
    fun getCurrentTemplateId(token: String) = fire(Dispatchers.IO) {
        val currentTemplateResponse = PalmSchoolNetwork.getCurrentTemplateId(token)
        if (currentTemplateResponse.status == 0) {
            val data = currentTemplateResponse.data
            Result.success(data)
        } else {
            Result.failure(RuntimeException("response status is ${currentTemplateResponse.status},message is ${currentTemplateResponse.message}"))
        }
    }

    // 更新用户当前所用模版id
    fun updateCurrentTemplateId(token: String, tid: CurrentTemplateUpdateRequest) =
        fire(Dispatchers.IO) {
            val currentTemplateUpdateResponse =
                PalmSchoolNetwork.updateCurrentTemplateId(token, tid)
            if (currentTemplateUpdateResponse.status == 0) {
                val data = currentTemplateUpdateResponse.data
                Result.success(data)
            } else {
                Result.failure(RuntimeException("response status is ${currentTemplateUpdateResponse.status},message is ${currentTemplateUpdateResponse.message}"))
            }
        }

    // 用户信息获取
    fun getUserInfo(token: String) = fire(Dispatchers.IO) {
        val userInfoResponse = PalmSchoolNetwork.getUserInfo(token)
        if (userInfoResponse.status == 0) {
            val data = userInfoResponse.data
            Result.success(data)
        } else {
            Result.failure(RuntimeException("response status is ${userInfoResponse.status},message is ${userInfoResponse.message}"))
        }
    }

    // 用户信息更新
    fun updateUserInfo(token: String, data: UserInfoUpdateRequest) = fire(Dispatchers.IO) {
        val userInfoUpdateResponse = PalmSchoolNetwork.updateUserInfo(token, data)
        if (userInfoUpdateResponse.status == 0) {
            val data = userInfoUpdateResponse.data
            Result.success(data)
        } else {
            Result.failure(RuntimeException("response status is ${userInfoUpdateResponse.status},message is ${userInfoUpdateResponse.message}"))
        }
    }

    // 头像上传
    fun uploadPortrait(token: String, data: PortraitUploadRequest) = fire(Dispatchers.IO) {
        val portraitUploadResponse = PalmSchoolNetwork.uploadPortrait(token, data)
        if (portraitUploadResponse.status == 0) {
            val data = portraitUploadResponse.data
            Result.success(data)
        } else {
            Result.failure(RuntimeException("response status is ${portraitUploadResponse.status},message is ${portraitUploadResponse.message}"))
        }
    }

    // 实名信息更新
    fun updateRealAuth(token: String, data: RealAuthUpdateRequest) = fire(Dispatchers.IO) {
        val realAuthUpdateResponse = PalmSchoolNetwork.updateRealAuth(token, data)
        if (realAuthUpdateResponse.status == 0) {
            val data = realAuthUpdateResponse.data
            Result.success(data)
        } else {
            Result.failure(RuntimeException("response status is ${realAuthUpdateResponse.status},message is ${realAuthUpdateResponse.message}"))
        }
    }

    // 实名信息创建
    fun createRealAuth(token: String, data: RealAuthCreateRequest) = fire(Dispatchers.IO) {
        val realAuthCreateResponse = PalmSchoolNetwork.createRealAuth(token, data)
        if (realAuthCreateResponse.status == 0) {
            val data = realAuthCreateResponse.data
            Result.success(data)
        } else {
            Result.failure(RuntimeException("response status is ${realAuthCreateResponse.status},message is ${realAuthCreateResponse.message}"))
        }
    }

    // 获取学校列表
    fun getSchoolList(token: String, map: Map<String, String>) = fire(Dispatchers.IO) {
        val schoolListResponse = PalmSchoolNetwork.getSchoolList(token, map)
        if (schoolListResponse.status == 0) {
            val data = schoolListResponse.data
            Result.success(data)
        } else {
            Result.failure(RuntimeException("response status is ${schoolListResponse.status},message is ${schoolListResponse.message}"))
        }
    }

    // 本地存储方法，可以简化，但建议依旧写成异步的模式
    fun saveToken(token: String) = TokenDao.saveToken(token)
    fun removeToken() = TokenDao.removeToken()
    fun getToken() = TokenDao.getToken()
    fun isTokenSaved() = TokenDao.isTokenSaved()

    fun saveTemplateID(token: String, tid: Int) = TemplateDao.saveTemplateID(token, tid)
    fun removeTemplateID(token: String) = TemplateDao.removeTemplateID(token)
    fun getTemplateID(token: String) = TemplateDao.getTemplateID(token)
    fun isTemplateIDSaved(token: String) = TemplateDao.isTemplateIDSaved(token)

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