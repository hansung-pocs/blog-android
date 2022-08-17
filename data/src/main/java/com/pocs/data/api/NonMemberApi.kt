package com.pocs.data.api

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.nonmember.NonMemberCreateInfoBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NonMemberApi {
    @POST("admin/users")
    suspend fun createNonMember(
        @Body nonMemberCreateInfoBody: NonMemberCreateInfoBody
    ): Response<ResponseBody<Unit>>
}