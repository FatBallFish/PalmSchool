package com.fatballfish.palmschool.ui.realAuth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.realAuth.RealAuthCreateRequest
import com.fatballfish.palmschool.ui.TokenViewModel

class RealAuthCreateViewModel : TokenViewModel() {
    private val requestLiveData = MutableLiveData<RealAuthCreateRequest>()
    val realAuthCreateRequest = Transformations.switchMap(requestLiveData) { data ->
        Repository.createRealAuth(getToken(), data)
    }

    fun createRealAuth(data: RealAuthCreateRequest) {
        requestLiveData.value = data
    }
}