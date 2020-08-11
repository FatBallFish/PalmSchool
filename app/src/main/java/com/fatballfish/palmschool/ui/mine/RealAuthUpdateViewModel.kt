package com.fatballfish.palmschool.ui.mine

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.realAuth.RealAuthUpdateRequest
import com.fatballfish.palmschool.ui.TokenViewModel

class RealAuthUpdateViewModel : TokenViewModel() {
    private val requestLiveData = MutableLiveData<RealAuthUpdateRequest>()
    val realAuthUpdateLiveData = Transformations.switchMap(requestLiveData) { request ->
        Repository.updateRealAuth(getToken(), request)
    }

    fun updateRealAuth(data: RealAuthUpdateRequest) {
        requestLiveData.value = data
    }
}