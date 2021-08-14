package com.fatballfish.palmschool.ui.mine

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.realAuth.RealAuthUpdateRequest
import com.fatballfish.palmschool.ui.TokenViewModel

class RealAuthUpdateViewModel @ViewModelInject constructor(repository: Repository) : TokenViewModel(repository) {
    private val requestLiveData = MutableLiveData<RealAuthUpdateRequest>()
    val realAuthUpdateLiveData = Transformations.switchMap(requestLiveData) { request ->
        repository.updateRealAuth(getToken(), request)
    }

    fun updateRealAuth(data: RealAuthUpdateRequest) {
        requestLiveData.value = data
    }
}