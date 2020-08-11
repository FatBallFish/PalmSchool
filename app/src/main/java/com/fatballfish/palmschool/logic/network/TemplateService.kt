package com.fatballfish.palmschool.logic.network

import com.fatballfish.palmschool.logic.model.template.CurrentTemplateResponse
import com.fatballfish.palmschool.logic.model.template.CurrentTemplateUpdateRequest
import com.fatballfish.palmschool.logic.model.template.CurrentTemplateUpdateResponse
import com.fatballfish.palmschool.logic.model.template.TemplateListResponse
import retrofit2.Call
import retrofit2.http.*

interface TemplateService {
    @GET("template/list/")
    fun getTemplateList(
        @Query("token") token: String,
        @QueryMap map: Map<String, String>
    ): Call<TemplateListResponse>

    @GET("template/current/")
    fun getCurrentTemplateId(@Query("token") token: String): Call<CurrentTemplateResponse>

    @POST("template/current/update/")
    fun updateCurrentTemplateId(
        @Query("token") token: String,
        @Body tid: CurrentTemplateUpdateRequest
    ): Call<CurrentTemplateUpdateResponse>
}