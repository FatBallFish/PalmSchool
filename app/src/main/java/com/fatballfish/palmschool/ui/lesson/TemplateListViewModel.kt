package com.fatballfish.palmschool.ui.lesson

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.Template.Template

class TemplateListViewModel : ViewModel() {
    private val requestLiveData = MutableLiveData<TemplateListRequest>()
    val templateList = ArrayList<Template>()
    val templateListLiveData = Transformations.switchMap(requestLiveData) { templateListRequest ->
        Repository.getTemplateList(templateListRequest.token, templateListRequest.map)
    }

    fun getTemplateList(token: String, map: Map<String, String>?) {
        requestLiveData.value = TemplateListRequest(token, map)
    }

    fun isTokenSaved() = Repository.isTokenSaved()
    fun getToken() = Repository.getToken()
    fun saveToken(token: String) = Repository.saveToken(token)

    fun isTemplateIDSaved(token: String) = Repository.isTemplateIDSaved(token)
    fun getTemplateID(token: String) = Repository.getTemplateID(token)
    fun saveTemplateID(token: String, tid: Int) = Repository.saveTemplateID(token, tid)
}