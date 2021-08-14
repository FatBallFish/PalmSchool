package com.fatballfish.palmschool.ui.lesson

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.template.Template
import com.fatballfish.palmschool.ui.TokenViewModel

class TemplateListViewModel @ViewModelInject constructor(repository: Repository) : TokenViewModel(repository) {
    private val requestLiveData = MutableLiveData<TemplateListRequest>()
    val templateList = ArrayList<Template>()
    val templateListLiveData = Transformations.switchMap(requestLiveData) { templateListRequest ->
        repository.getTemplateList(templateListRequest.token, templateListRequest.map)
    }

    fun getTemplateList(token: String, map: Map<String, String>) {
        requestLiveData.value = TemplateListRequest(token, map)
    }

    fun isTemplateIDSaved(token: String) = repository.isTemplateIDSaved(token)
    fun getTemplateID(token: String) = repository.getTemplateID(token)
    fun saveTemplateID(token: String, tid: Int) = repository.saveTemplateID(token, tid)
}