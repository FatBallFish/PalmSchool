package com.fatballfish.palmschool.logic.model.realAuth

data class RealAuthCreateResponse(
    val id: Int,
    val status: Int,
    val message: String,
    val data: Data
) {
    class Data()
}