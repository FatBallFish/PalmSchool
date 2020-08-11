package com.fatballfish.palmschool.logic.network

import com.fatballfish.palmschool.logic.model.user.PortraitUploadRequest
import com.fatballfish.palmschool.logic.model.user.PortraitUploadResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface PortraitService {
    @POST("user/portrait/upload/")
    fun uploadPortrait(
        @Query("token") token: String,
        @Body data: PortraitUploadRequest
    ): Call<PortraitUploadResponse>
}