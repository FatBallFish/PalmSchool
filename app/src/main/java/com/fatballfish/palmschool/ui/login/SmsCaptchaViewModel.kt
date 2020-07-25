package com.fatballfish.palmschool.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.SmsCaptchaCreateRequest
import com.fatballfish.palmschool.logic.model.SmsCaptchaValidateRequest
import com.fatballfish.palmschool.logic.util.MD5Utils

class SmsCaptchaCreateViewModel : ViewModel() {
    private val requestLiveData = MutableLiveData<SmsCaptchaCreateRequest>()
    var phone = ""
    val smsCaptchaCreateLiveData =
        Transformations.switchMap(requestLiveData) { smsCaptchaCreateRequest ->
            Repository.smsCaptchaCreate(smsCaptchaCreateRequest)
        }

    fun smsCaptchaCreate(phone: String) {
        this.phone = phone
        requestLiveData.value = SmsCaptchaCreateRequest(phone)
    }
}

class SmsCaptchaValidateViewModel : ViewModel() {
    private val requestLiveData = MutableLiveData<SmsCaptchaValidateRequest>()
    var hash = ""
    val smsCaptchaValidateLiveData =
        Transformations.switchMap(requestLiveData) { smsCaptchaValidateRequest ->
            Repository.smsCaptchaValidata(smsCaptchaValidateRequest)
        }

    fun smsCaptchaValidate(code: String, rand: String) {
        val hash = MD5Utils.stringToMD5(code, rand)
        this.hash = hash
        requestLiveData.value = SmsCaptchaValidateRequest(hash)
    }
}