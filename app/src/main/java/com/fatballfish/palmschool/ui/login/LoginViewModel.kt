package com.fatballfish.palmschool.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.user.PassLoginRequest
import com.fatballfish.palmschool.logic.model.user.SmsLoginRequest
import com.fatballfish.palmschool.ui.TokenViewModel

class SmsLoginViewModel @ViewModelInject constructor(val repository: Repository) : ViewModel() {
    private val smsLiveData = MutableLiveData<SmsLoginRequest>()
    var username = ""
    var hash = ""
    var enduring = false
    val smsLoginLiveData = Transformations.switchMap(smsLiveData) { smsLoginRequest ->
        repository.smsLogin(smsLoginRequest)
    }

    fun smsLogin(username: String, hash: String, enduring: Boolean = false) {
        this.username = username
        this.hash = hash
        this.enduring = enduring
        smsLiveData.value =
            SmsLoginRequest(
                username,
                hash,
                enduring
            )
    }

    fun saveToken(token: String) = repository.saveToken(token)
    fun isTokenSaved() = repository.isTokenSaved()
    fun getToken() = repository.getToken()
}

class PassLoginViewModel @ViewModelInject constructor(repository: Repository) : TokenViewModel(repository) {
    private val smsLiveData = MutableLiveData<PassLoginRequest>()
    var username = ""
    var password = ""
    var enduring = false
    val passLoginLiveData = Transformations.switchMap(smsLiveData) { passLoginRequest ->
        repository.passLogin(passLoginRequest)
    }

    fun passLogin(username: String, password: String, enduring: Boolean = false) {
        this.username = username
        this.password = password
        this.enduring = enduring
        smsLiveData.value =
            PassLoginRequest(
                username,
                password,
                enduring
            )
    }
}