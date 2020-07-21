package com.fatballfish.palmschool

import android.app.Application
import android.content.Context

class PalmSchoolApplication : Application() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}