package com.fatballfish.palmschool.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.user.SmsCaptchaCreateRequest
import com.fatballfish.palmschool.logic.model.user.SmsCaptchaValidateRequest
import com.fatballfish.palmschool.logic.util.MD5Utils
import com.fatballfish.palmschool.ui.TokenViewModel

class SmsCaptchaCreateViewModel @ViewModelInject constructor(repository: Repository) :
    TokenViewModel(repository) {
    private val requestLiveData = MutableLiveData<SmsCaptchaCreateRequest>()
    var phone = ""
    val smsCaptchaCreateLiveData =
        Transformations.switchMap(requestLiveData) { smsCaptchaCreateRequest ->
            repository.smsCaptchaCreate(smsCaptchaCreateRequest)
        }

    fun smsCaptchaCreate(phone: String) {
        this.phone = phone
        requestLiveData.value =
            SmsCaptchaCreateRequest(
                phone
            )
    }
}

class SmsCaptchaValidateViewModel @ViewModelInject constructor(repository: Repository) :
    TokenViewModel(repository) {
    private val requestLiveData = MutableLiveData<SmsCaptchaValidateRequest>()
    var hash = ""
    val smsCaptchaValidateLiveData =
        Transformations.switchMap(requestLiveData) { smsCaptchaValidateRequest ->
            repository.smsCaptchaValidata(smsCaptchaValidateRequest)
        }

    fun smsCaptchaValidate(code: String, rand: String) {
        val hash = MD5Utils.stringToMD5(code, rand)
        this.hash = hash
        requestLiveData.value = SmsCaptchaValidateRequest(hash)
    }
}