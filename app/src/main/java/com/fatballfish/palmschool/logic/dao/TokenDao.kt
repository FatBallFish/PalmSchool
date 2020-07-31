package com.fatballfish.palmschool.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.fatballfish.palmschool.PalmSchoolApplication

object TokenDao {
    fun saveToken(token: String) {
        sharedPreferences().edit {
            putString("token", token)
        }
    }

    fun removeToken() {
        sharedPreferences().edit {
            remove("token")
        }
    }

    fun getToken(): String {
        val token = sharedPreferences().getString("token", "")
        return token!!
    }

    fun isTokenSaved() = sharedPreferences().contains("token")
    private fun sharedPreferences() =
        PalmSchoolApplication.context.getSharedPreferences("palm_school", Context.MODE_PRIVATE)
}