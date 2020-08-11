package com.fatballfish.palmschool.logic.dao

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import com.fatballfish.palmschool.PalmSchoolApplication
import java.lang.Exception

object TemplateDao {
    fun saveTemplateID(token: String, tid: Int): Boolean {
        val db = PalmSchoolApplication.db
        val cursor = db.rawQuery(
            "SELECT * FROM tokens WHERE token = ?",
            arrayOf(token)
        )
        if (!cursor.moveToFirst()) {
            // token记录不存在
            Log.d("SQL", "saveTemplateID:token不存在")
            return false
        }
        val username = cursor.getString(cursor.getColumnIndex("username"))
        cursor.close()
        val cursor2 = db.rawQuery("SELECT * FROM config WHERE username = ?", arrayOf(username))
        if (cursor2.moveToFirst()) {
            val values = ContentValues()
            values.put("tid", tid)
            val affected = db.update(
                PalmSchoolApplication.TABLE_CONFIG,
                values,
                "username = ?",
                arrayOf(username)
            )
            Log.d("SQL", "saveTemplateID:affected:$affected")
        } else {
            val config = ContentValues().apply {
                put("username", username)
                put("tid", tid)
            }
            db.insert(PalmSchoolApplication.TABLE_CONFIG, null, config)
        }
        cursor2.close()
        return true
    }

    fun removeTemplateID(token: String): Boolean {
        val db = PalmSchoolApplication.db
        var flag = true
        val cursor = db.rawQuery(
            "SELECT * FROM tokens WHERE token = ?",
            arrayOf(token)
        )
        if (!cursor.moveToFirst()) {
            // token记录不存在
            flag = false
        }
        if (flag) {
            val username = cursor.getString(cursor.getColumnIndex("username"))
            db.execSQL("UPDATE config SET tid = -1 WHERE username = ?", arrayOf(username))
        }
        cursor.close()
        return flag
    }

    fun getTemplateID(token: String): Int {
        val db = PalmSchoolApplication.db
        var tid: Int = -1
        val cursor = db.rawQuery(
            "SELECT config.tid FROM tokens,config WHERE tokens.token = ? AND tokens.username = config.username",
            arrayOf(token)
        )
        if (cursor.moveToFirst()) {
            try {
                tid = cursor.getInt(cursor.getColumnIndex("tid"))
            } catch (e: Exception) {
                tid = -1
            }
        }
        cursor.close()
        return tid
    }

    fun isTemplateIDSaved(token: String): Boolean {
        val db = PalmSchoolApplication.db
        var flag = true
        val cursor = db.rawQuery(
            "SELECT config.tid FROM tokens,config WHERE tokens.token = ? AND tokens.username = config.username",
            arrayOf(token)
        )
        if (cursor.moveToFirst()) {
            try {
                val tid = cursor.getInt(cursor.getColumnIndex("tid"))
            } catch (e: Exception) {
                flag = false
            }
        } else {
            flag = false
        }
        cursor.close()
        return flag
    }

}