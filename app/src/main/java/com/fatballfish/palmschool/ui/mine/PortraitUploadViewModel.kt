package com.fatballfish.palmschool.ui.mine

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.user.PortraitUploadRequest
import com.fatballfish.palmschool.ui.TokenViewModel

class PortraitUploadViewModel @ViewModelInject constructor(repository: Repository) : TokenViewModel(repository) {
    private val requestLiveData = MutableLiveData<PortraitUploadRequest>()
    val portraitUploadLiveData = Transformations.switchMap(requestLiveData) { request ->
        repository.uploadPortrait(getToken(), request)
    }

    fun uploadPortrait(base64: String) {
        requestLiveData.value = PortraitUploadRequest(base64)
    }
}