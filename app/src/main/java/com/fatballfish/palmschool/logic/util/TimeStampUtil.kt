package com.fatballfish.palmschool.logic.util

import java.text.ParsePosition
import java.text.SimpleDateFormat

object TimeStampUtil {
    /**
     * Timestamp to String
     * @param Timestamp
     * @return String
     */
    fun transToString(time: Long): String {
        return SimpleDateFormat("yyyy.MM.dd").format(time)
    }

    /**
     * String to Timestamp
     * @param String
     * @return Timestamp
     */

    fun transToTimeStamp(date: String): Long {
        return SimpleDateFormat("yyyy.MM.dd hh:mm:ss").parse(date, ParsePosition(0)).time
    }
}
