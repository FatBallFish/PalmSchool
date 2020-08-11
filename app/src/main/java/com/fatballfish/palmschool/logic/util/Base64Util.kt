package com.fatballfish.palmschool.logic.util

import java.io.FileInputStream
import java.io.IOException
import android.util.Base64
import java.lang.Exception

object Base64Util {
    fun imageToBase64(path: String): String {
        var data: ByteArray? = null
        try {
            val inputStream = FileInputStream(path)
            data = ByteArray(inputStream.available())
            inputStream.read(data)
            inputStream.close()
            // 对字节数组进行Base64编码
            return Base64.encodeToString(data, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    fun encodeToString(string: String): String {
        try {
            return Base64.encodeToString(string.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    fun encodeToString(bytes: ByteArray): String {
        try {
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    fun decodeToBytes(base64: String): ByteArray {
        try {
            return Base64.decode(base64, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            return ByteArray(0)
        }
    }

    fun decodeToBytes(bytes: ByteArray): ByteArray {
        try {
            return Base64.decode(bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            return ByteArray(0)
        }
    }

    fun decodeToString(base64: String): String {
        try {
            return String(Base64.decode(base64, Base64.DEFAULT))
        } catch (e: Exception) {
            return ""
        }
    }

    fun decodeToString(bytes: ByteArray): String {
        try {
            return String(Base64.decode(bytes, Base64.DEFAULT))
        } catch (e: Exception) {
            return ""
        }
    }
}