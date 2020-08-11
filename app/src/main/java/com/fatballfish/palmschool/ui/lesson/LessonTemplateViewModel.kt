package com.fatballfish.palmschool.ui.lesson

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.lesson.LessonTable
import com.fatballfish.palmschool.logic.model.lesson.LessonTemplateListRequest
import com.fatballfish.palmschool.ui.TokenViewModel

class LessonTemplateViewModel : TokenViewModel() {
    private val requestLiveData = MutableLiveData<LessonTemplateListRequest>()
    val lessonTableList = ArrayList<LessonTable>()
    val personLessonListLiveData =
        Transformations.switchMap(requestLiveData) { personLessonListRequest ->
            Repository.getLessonTemplateList(personLessonListRequest)
        }

    fun getLessonTemplateList(token: String, tid: Int) {
        requestLiveData.value = LessonTemplateListRequest(token, tid)
    }

    fun isTemplateIDSaved(token: String) = Repository.isTemplateIDSaved(token)
    fun getTemplateID(token: String) = Repository.getTemplateID(token)
    fun saveTemplateID(token: String, tid: Int) = Repository.saveTemplateID(token, tid)
}