package com.fatballfish.palmschool.ui.mine

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.fatballfish.palmschool.PalmSchoolApplication

import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.user.UserInfoUpdateRequest
import com.fatballfish.palmschool.ui.TokenViewModel

class UserInfoUpdateViewModel : TokenViewModel() {
    private var local_token: String = getToken()
    private val requestLiveData = MutableLiveData<UserInfoUpdateRequest>()
    val userInfoUpdateLiveData = Transformations.switchMap(requestLiveData) { request ->
        Repository.updateUserInfo(local_token, request)
    }

    fun updateUserInfo(data: UserInfoUpdateRequest) {
        if (local_token.isEmpty()) {
            Toast.makeText(PalmSchoolApplication.context, "请先登录", Toast.LENGTH_SHORT).show()
            return
        }
        requestLiveData.value = data
    }
}