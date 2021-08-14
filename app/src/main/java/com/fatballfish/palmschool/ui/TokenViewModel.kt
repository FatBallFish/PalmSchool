package com.fatballfish.palmschool.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.fatballfish.palmschool.logic.Repository

open class TokenViewModel (val repository: Repository) : ViewModel() {
    fun isTokenSaved() = repository.isTokenSaved()
    fun getToken() = repository.getToken()
    fun saveToken(token: String) = repository.saveToken(token)
    fun removeToken() = repository.removeToken()
}