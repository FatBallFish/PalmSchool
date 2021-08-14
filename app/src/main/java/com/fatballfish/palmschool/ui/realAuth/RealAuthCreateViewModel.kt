package com.fatballfish.palmschool.ui.realAuth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.realAuth.RealAuthCreateRequest
import com.fatballfish.palmschool.ui.TokenViewModel

class RealAuthCreateViewModel @ViewModelInject constructor(repository: Repository) : TokenViewModel(repository) {
    private val requestLiveData = MutableLiveData<RealAuthCreateRequest>()
    val realAuthCreateRequest = Transformations.switchMap(requestLiveData) { data ->
        repository.createRealAuth(getToken(), data)
    }

    fun createRealAuth(data: RealAuthCreateRequest) {
        requestLiveData.value = data
    }
}