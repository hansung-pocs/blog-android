package com.pocs.data.source

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.nonmember.NonMemberCreateInfoBody
import retrofit2.Response

interface NonMemberRemoteDataSource {
    suspend fun createNonMember(nonMemberCreateInfoBody: NonMemberCreateInfoBody): Response<ResponseBody<Unit>>
}