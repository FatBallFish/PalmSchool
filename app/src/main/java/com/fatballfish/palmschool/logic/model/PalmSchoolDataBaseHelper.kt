package com.fatballfish.palmschool.logic.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class PalmSchoolDataBaseHelper(val context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version) {
    private val createTokenUser = "CREATE TABLE `tokens`  (\n" +
            "  `token` TEXT PRIMARY KEY,\n" +
            "  `username` TEXT\n" +
            ") "
    private val createConfig = "CREATE TABLE `config`  (\n" +
            "  `username` TEXT PRIMARY KEY,\n" +
            "  `tid` INTEGER\n" +
            ") "

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTokenUser)
        db?.execSQL(createConfig)
        Toast.makeText(context, "create succeeded", Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}