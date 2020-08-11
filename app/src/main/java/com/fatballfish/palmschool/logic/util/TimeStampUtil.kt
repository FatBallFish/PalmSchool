package com.fatballfish.palmschool.logic.util

import java.text.ParsePosition
import java.text.SimpleDateFormat

object TimeStampUtil {
    /**
     * Timestamp to String
     * @param Timestamp
     * @return String
     */
    fun transTimeStampToDateString(time: Long): String {
        return SimpleDateFormat("yyyy.MM.dd").format(time)
    }

    fun transTimeStampToDateTimeString(time: Long): String {
        return SimpleDateFormat("yyyy.MM.dd hh:mm:ss").format(time)
    }

    /**
     * String to Timestamp
     * @param String
     * @return Timestamp
     */

    fun transDateTimeToTimeStamp(date: String): Long {
        return SimpleDateFormat("yyyy.MM.dd hh:mm:ss").parse(date, ParsePosition(0)).time
    }

    fun transDateToTimeStamp(date: String): Long {
        return SimpleDateFormat("yyyy.MM.dd").parse(date, ParsePosition(0)).time
    }
}
