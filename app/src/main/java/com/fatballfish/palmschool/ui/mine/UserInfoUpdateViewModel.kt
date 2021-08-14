package com.fatballfish.palmschool.ui.mine

import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.PalmSchoolApplication

import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.user.UserInfoUpdateRequest
import com.fatballfish.palmschool.ui.TokenViewModel


class UserInfoUpdateViewModel @ViewModelInject constructor(repository: Repository) : TokenViewModel(repository) {
    private var local_token: String = getToken()
    private val requestLiveData = MutableLiveData<UserInfoUpdateRequest>()
    val userInfoUpdateLiveData = Transformations.switchMap(requestLiveData) { request ->
        repository.updateUserInfo(local_token, request)
    }

    fun updateUserInfo(data: UserInfoUpdateRequest) {
        if (local_token.isEmpty()) {
            Toast.makeText(PalmSchoolApplication.context, "请先登录", Toast.LENGTH_SHORT).show()
            return
        }
        requestLiveData.value = data
    }
}