package com.fatballfish.palmschool.ui.school

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository
import com.fatballfish.palmschool.logic.model.school.School
import com.fatballfish.palmschool.ui.TokenViewModel


class SchoolListViewModel @ViewModelInject constructor(repository: Repository) : TokenViewModel(repository) {
    private val requestLiveData = MutableLiveData<Map<String, String>>()
    val schoolList = ArrayList<School>()
    val schoolListLiveData = Transformations.switchMap(requestLiveData) { map ->
        repository.getSchoolList(getToken(), map)
    }

    fun getSchoolList(map: Map<String, String>) {
        requestLiveData.value = map
    }
}