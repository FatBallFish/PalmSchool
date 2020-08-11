package com.fatballfish.palmschool.logic.network

import com.fatballfish.palmschool.logic.model.realAuth.RealAuthCreateRequest
import com.fatballfish.palmschool.logic.model.realAuth.RealAuthCreateResponse
import com.fatballfish.palmschool.logic.model.realAuth.RealAuthUpdateRequest
import com.fatballfish.palmschool.logic.model.realAuth.RealAuthUpdateResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface RealAuthService {
    @POST("user/realauth/update/")
    fun updateRealAuth(
        @Query("token") token: String,
        @Body data: RealAuthUpdateRequest
    ): Call<RealAuthUpdateResponse>

    @POST("user/realauth/create/")
    fun createRealAuth(
        @Query("token") token: String,
        @Body data: RealAuthCreateRequest
    ): Call<RealAuthCreateResponse>
}