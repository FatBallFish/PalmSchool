package com.fatballfish.palmschool.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.PassLoginRequest
import com.fatballfish.palmschool.logic.model.SmsLoginRequest

class SmsLoginViewModel : ViewModel() {
    private val smsLiveData = MutableLiveData<SmsLoginRequest>()
    var username = ""
    var hash = ""
    var enduring = false
    val smsLoginLiveData = Transformations.switchMap(smsLiveData) { smsLoginRequest ->
        Repository.smsLogin(smsLoginRequest)
    }

    fun smsLogin(username: String, hash: String, enduring: Boolean = false) {
        this.username = username
        this.hash = hash
        this.enduring = enduring
        smsLiveData.value = SmsLoginRequest(username, hash, enduring)
    }
}

class PassLoginViewModel : ViewModel() {
    private val smsLiveData = MutableLiveData<PassLoginRequest>()
    var username = ""
    var password = ""
    var enduring = false
    val passLoginLiveData = Transformations.switchMap(smsLiveData) { passLoginRequest ->
        Repository.passLogin(passLoginRequest)
    }

    fun passLogin(username: String, password: String, enduring: Boolean = false) {
        this.username = username
        this.password = password
        this.enduring = enduring
        smsLiveData.value = PassLoginRequest(username, password, enduring)
    }
}