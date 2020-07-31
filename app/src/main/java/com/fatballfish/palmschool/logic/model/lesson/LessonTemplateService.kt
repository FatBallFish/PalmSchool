package com.fatballfish.palmschool.logic.model.lesson

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LessonTemplateService {
    //    @Headers("content-type:application/json")
    @GET("lesson/list/get/")
    fun getLessonTemplateList(
        @Query("token") token: String,
        @Query("tid") tid: Int
    ): Call<LessonTemplateListResponse>


}