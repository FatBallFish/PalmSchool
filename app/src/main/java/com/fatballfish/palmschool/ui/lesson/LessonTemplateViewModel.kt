package com.fatballfish.palmschool.ui.lesson

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.lesson.LessonTable
import com.fatballfish.palmschool.logic.model.lesson.LessonTemplateListRequest
import com.fatballfish.palmschool.ui.TokenViewModel


class LessonTemplateViewModel @ViewModelInject constructor(repository: Repository) : TokenViewModel(repository) {
    private val requestLiveData = MutableLiveData<LessonTemplateListRequest>()
    val lessonTableList = ArrayList<LessonTable>()
    val personLessonListLiveData =
        Transformations.switchMap(requestLiveData) { personLessonListRequest ->
            repository.getLessonTemplateList(personLessonListRequest)
        }

    fun getLessonTemplateList(token: String, tid: Int) {
        requestLiveData.value = LessonTemplateListRequest(token, tid)
    }

    fun isTemplateIDSaved(token: String) = repository.isTemplateIDSaved(token)
    fun getTemplateID(token: String) = repository.getTemplateID(token)
    fun saveTemplateID(token: String, tid: Int) = repository.saveTemplateID(token, tid)
}