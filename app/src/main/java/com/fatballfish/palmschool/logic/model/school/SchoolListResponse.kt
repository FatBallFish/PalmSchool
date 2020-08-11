package com.fatballfish.palmschool.logic.model.school

data class SchoolListResponse(val id: Int, val status: Int, val message: String, val data: Data) {
    data class Data(val num: Int, val list: ArrayList<School>)
}

data class School(val id: Int, val name: String, val location: String)