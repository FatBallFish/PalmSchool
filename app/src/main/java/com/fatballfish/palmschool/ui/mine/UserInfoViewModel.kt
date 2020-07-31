package com.fatballfish.palmschool.ui.mine

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository

class UserInfoViewModel : ViewModel() {
    private val requestLiveData = MutableLiveData<String>()
    var local_token: String = ""
    val userInfoLiveData = Transformations.switchMap(requestLiveData) { token ->
        Repository.getUserInfo(token)
    }

    fun getUserInfo(token: String) {
        this.local_token = token
        requestLiveData.value = token
    }

    fun isTokenSaved() = Repository.isTokenSaved()
    fun getToken() = Repository.getToken()
    fun saveToken(token: String) = Repository.saveToken(token)
    fun removeToken() = Repository.removeToken()
}