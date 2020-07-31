package com.fatballfish.palmschool

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.fatballfish.palmschool.logic.model.PalmSchoolDataBaseHelper

class PalmSchoolApplication : Application() {
    companion object {
        var token: String? = null
        lateinit var context: Context
        private lateinit var dbHelper: PalmSchoolDataBaseHelper
        lateinit var db: SQLiteDatabase

        const val TABLE_TOKEN = "tokens"
        const val TABLE_CONFIG = "config"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        dbHelper = PalmSchoolDataBaseHelper(this, "PalmSchool.db", 1)
        db = dbHelper.writableDatabase
    }
}