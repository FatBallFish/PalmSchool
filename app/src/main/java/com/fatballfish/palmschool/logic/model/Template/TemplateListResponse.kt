package com.fatballfish.palmschool.logic.model.Template

import com.google.gson.annotations.SerializedName

data class TemplateListResponse(val id: Int, val status: Int, val message: String, val data: Data) {
    data class Data(val num: Int, val list: ArrayList<Template>)

}

data class Template(
    val tid: Int,
    val name: String,
    val content: String,
    val public: Boolean,
    val submiter: String,
    @SerializedName("add_time")val addTime: Long,
    val used: Int
)