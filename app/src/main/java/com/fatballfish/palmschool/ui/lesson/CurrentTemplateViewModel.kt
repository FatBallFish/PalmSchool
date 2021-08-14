package com.fatballfish.palmschool.ui.lesson

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.template.CurrentTemplateUpdateRequest
import com.fatballfish.palmschool.ui.TokenViewModel

class CurrentTemplateViewModel @ViewModelInject constructor(repository: Repository) : TokenViewModel(repository) {
    private val getRequest = MutableLiveData<String>()
    private val updateRequest = MutableLiveData<CurrentTemplateUpdateRequest>()
    val getCurrentTemplateLiveData = Transformations.switchMap(getRequest) { token ->
        repository.getCurrentTemplateId(token)
    }
    val updateCurrentTemplateLiveData = Transformations.switchMap(updateRequest) { request ->
        repository.updateCurrentTemplateId(getToken(), request)
    }

    fun getCurrentTemplateId() {
        getRequest.value = getToken()
    }

    fun updateCurrentTemplateId(tid: Int) {
        updateRequest.value = CurrentTemplateUpdateRequest(tid)
    }
}