package com.fatballfish.palmschool.logic.model.user

data class PortraitUploadResponse(
    val id: Int,
    val status: Int,
    val message: String,
    val data: Data
) {
    data class Data(val key: String, val url: String)
}