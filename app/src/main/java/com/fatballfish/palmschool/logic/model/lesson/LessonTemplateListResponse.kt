package com.fatballfish.palmschool.logic.model.lesson

import com.google.gson.annotations.SerializedName

data class LessonTemplateListResponse(
    val id: Int,
    val status: Int,
    val message: String,
    val data: Data
) {
    data class Data(val num: Int, val list: ArrayList<LessonTable>)
}

data class LessonTable(
    @SerializedName("lt_id") val ltid: Int,
    val name: String,
    val school: School,
    val teacher: String,
    @SerializedName("lesson_type") val lessonType: Int,
    val content: String,
    val location: String,
    val xn: String,
    val xq: String,
    @SerializedName("time_text") val timeText: String,
    val week: ArrayList<Int>,
    val weekday: Int,
    val lessons: ArrayList<Int>
)

data class School(val id: Int, val name: String, val location: String)