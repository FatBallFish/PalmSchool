package com.fatballfish.palmschool.ui.mine

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.user.PortraitUploadRequest
import com.fatballfish.palmschool.ui.TokenViewModel

class PortraitUploadViewModel : TokenViewModel() {
    private val requestLiveData = MutableLiveData<PortraitUploadRequest>()
    val portraitUploadLiveData = Transformations.switchMap(requestLiveData) { request ->
        Repository.uploadPortrait(getToken(), request)
    }

    fun uploadPortrait(base64: String) {
        requestLiveData.value = PortraitUploadRequest(base64)
    }
}