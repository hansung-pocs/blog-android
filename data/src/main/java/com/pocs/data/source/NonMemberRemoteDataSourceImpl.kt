package com.pocs.data.source

import com.pocs.data.api.NonMemberApi
import com.pocs.data.model.ResponseBody
import com.pocs.data.model.nonmember.NonMemberCreateInfoBody
import retrofit2.Response
import javax.inject.Inject

class NonMemberRemoteDataSourceImpl @Inject constructor(
    private val api : NonMemberApi
) : NonMemberRemoteDataSource {

    override suspend fun createNonMember(
        nonMemberCreateInfoBody: NonMemberCreateInfoBody
    ): Response<ResponseBody<Unit>> {
        return api.createNonMember(nonMemberCreateInfoBody)
    }
}