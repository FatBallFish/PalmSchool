package com.fatballfish.palmschool.logic.model.mine

import com.fatballfish.palmschool.R

data class UserInfoItem(
    val key: String,
    val value: String,
    val icon_id: Int,
    val clickable: Boolean
) {
}

private val infoIcon = mapOf(
    "email" to R.drawable.ic_email,
    "phone" to R.drawable.ic_phone,
    "birthday" to R.drawable.ic_birthday,
    "real_auth" to R.drawable.ic_real_auth,
    "id" to R.drawable.ic_id,
    "name" to R.drawable.ic_name,
    "school" to R.drawable.ic_school,
    "dept" to R.drawable.ic_dept,
    "major" to R.drawable.ic_major,
    "cls" to R.drawable.ic_class,
    "unknown" to R.drawable.ic_unknown
)

fun getInfoItem(key: String, value: String?, clickable: Boolean): UserInfoItem {
    return UserInfoItem(key, value ?: "", infoIcon[key] ?: infoIcon["unknown"]!!, clickable)
}