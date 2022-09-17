package com.pocs.test_library.fake.source.remote

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.user.UserDto
import com.pocs.data.model.user.UserProfileUpdateResponse
import com.pocs.data.model.user.anonymous.AnonymousCreateInfoBody
import com.pocs.data.source.UserRemoteDataSource
import com.pocs.test_library.mock.errorResponse
import com.pocs.test_library.mock.successResponse
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class FakeUserRemoteDataSource @Inject constructor() : UserRemoteDataSource {

    var updateUserResult = errorResponse<Unit>()
    var uploadProfileImageCallBack: () -> Unit = {}

    override suspend fun getUserDetail(id: Int): Response<ResponseBody<UserDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(
        id: Int,
        name: String,
        password: String?,
        email: String,
        company: String?,
        github: String?
    ): Response<ResponseBody<Unit>> {
        return updateUserResult
    }

    override suspend fun uploadProfileImage(
        id: Int,
        profileImage: File?
    ): Response<ResponseBody<UserProfileUpdateResponse>> {
        uploadProfileImageCallBack.invoke()
        return successResponse(UserProfileUpdateResponse(ok = true, userProfilePath = null))
    }

    override suspend fun createAnonymous(
        AnonymousCreateInfoBody: AnonymousCreateInfoBody
    ): Response<ResponseBody<Unit>> {
        TODO("Not yet implemented")
    }
}
