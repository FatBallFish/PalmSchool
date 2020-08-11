package com.fatballfish.palmschool.ui.lesson

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.template.CurrentTemplateUpdateRequest
import com.fatballfish.palmschool.ui.TokenViewModel

class CurrentTemplateViewModel : TokenViewModel() {
    private val getRequest = MutableLiveData<String>()
    private val updateRequest = MutableLiveData<CurrentTemplateUpdateRequest>()
    val getCurrentTemplateLiveData = Transformations.switchMap(getRequest) { token ->
        Repository.getCurrentTemplateId(token)
    }
    val updateCurrentTemplateLiveData = Transformations.switchMap(updateRequest) { request ->
        Repository.updateCurrentTemplateId(getToken(), request)
    }

    fun getCurrentTemplateId() {
        getRequest.value = getToken()
    }

    fun updateCurrentTemplateId(tid: Int) {
        updateRequest.value = CurrentTemplateUpdateRequest(tid)
    }
}