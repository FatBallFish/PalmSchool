package com.fatballfish.palmschool.ui.lesson

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.lesson.LessonTable
import com.fatballfish.palmschool.logic.model.lesson.LessonTemplateListRequest

class LessonTemplateViewModel : ViewModel() {
    private val requestLiveData = MutableLiveData<LessonTemplateListRequest>()
    var local_token = ""
    var current_tid = -1
    val lessonTableList = ArrayList<LessonTable>()
    val personLessonListLiveData =
        Transformations.switchMap(requestLiveData) { personLessonListRequest ->
            Repository.getLessonTemplateList(personLessonListRequest)
        }

    fun getLessonTemplateList(token: String, tid: Int) {
        this.local_token = token
        this.current_tid = tid
        requestLiveData.value = LessonTemplateListRequest(token, tid)
    }

    fun isTokenSaved() = Repository.isTokenSaved()
    fun getToken() = Repository.getToken()
    fun saveToken(token: String) = Repository.saveToken(token)

    fun isTemplateIDSaved(token: String) = Repository.isTemplateIDSaved(token)
    fun getTemplateID(token: String) = Repository.getTemplateID(token)
    fun saveTemplateID(token: String, tid: Int) = Repository.saveTemplateID(token, tid)
}