package com.fatballfish.palmschool.logic.network

import com.fatballfish.palmschool.logic.model.Template.TemplateListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface TemplateService {
    @GET("template/list/")
    fun getTemplateList(
        @Query("token") token: String,
        @QueryMap map: Map<String, String>?
    ): Call<TemplateListResponse>
}