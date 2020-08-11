package com.fatballfish.palmschool.logic.network

import com.fatballfish.palmschool.logic.model.school.SchoolListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface SchoolService {
    @GET("school/list/")
    fun getSchoolList(
        @Query("token") token: String,
        @QueryMap map: Map<String, String>
    ): Call<SchoolListResponse>
}