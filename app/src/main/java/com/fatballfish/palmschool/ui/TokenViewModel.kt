package com.fatballfish.palmschool.ui

import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository

open class TokenViewModel : ViewModel() {
    fun isTokenSaved() = Repository.isTokenSaved()
    fun getToken() = Repository.getToken()
    fun saveToken(token: String) = Repository.saveToken(token)
    fun removeToken() = Repository.removeToken()
}