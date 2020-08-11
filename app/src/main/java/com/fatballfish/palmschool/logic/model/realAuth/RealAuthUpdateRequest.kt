package com.fatballfish.palmschool.logic.model.realAuth

data class RealAuthUpdateRequest(
    val school: Int? = null,
    val dept: String? = null,
    val major: String? = null,
    val cls: String? = null
)