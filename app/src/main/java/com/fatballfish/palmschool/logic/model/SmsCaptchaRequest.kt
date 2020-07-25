package com.fatballfish.palmschool.logic.model

data class SmsCaptchaCreateRequest(val phone: String)
data class SmsCaptchaValidateRequest(val hash: String)