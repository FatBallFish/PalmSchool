package com.fatballfish.palmschool.logic.dao

/*
定义切换窗口及返回时的请求码和返回码
 */
object ActivityDao {
    const val RESULT_OK = 0
    const val RESULT_CANCEL = -1
    const val REQUEST_LOGIN = 1
    const val REQUEST_USER_INFO = 2
    const val REQUEST_TEMPLATE_SEARCH = 3
    const val ACTION_REFRESH_TEMPLATE = "com.fatballfish.palmschool.REFRESH_TEMPLATE"
}