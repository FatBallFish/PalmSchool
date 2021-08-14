package com.fatballfish.palmschool.ui.mine

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.ui.TokenViewModel

class UserInfoViewModel @ViewModelInject constructor(repository: Repository) : TokenViewModel(repository) {
    private val requestLiveData = MutableLiveData<String>()
    var local_token: String = ""
    val userInfoLiveData = Transformations.switchMap(requestLiveData) { token ->
        repository.getUserInfo(token)
    }

    fun getUserInfo(token: String) {
        this.local_token = token
        requestLiveData.value = token
    }
}