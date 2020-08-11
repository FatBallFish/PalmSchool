package com.fatballfish.palmschool.logic.model.template

data class CurrentTemplateUpdateResponse(
    val id: Int,
    val status: Int,
    val message: String,
    val data: Data
) {
    data class Data(val tid: Int)
}