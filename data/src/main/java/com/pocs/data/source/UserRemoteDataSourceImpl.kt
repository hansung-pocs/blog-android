package com.pocs.data.source

import com.pocs.data.api.UserApi
import com.pocs.data.model.ResponseBody
import com.pocs.data.model.user.UserProfileUpdateResponse
import com.pocs.data.model.user.UserUpdateBody
import com.pocs.data.model.user.anonymous.AnonymousCreateInfoBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val api: UserApi
) : UserRemoteDataSource {

    override suspend fun getUserDetail(id: Int) = api.getUserDetail(id = id)

    override suspend fun updateUser(
        id: Int,
        name: String,
        password: String?,
        email: String,
        company: String?,
        github: String?
    ): Response<ResponseBody<Unit>> {
        return api.updateUser(
            userId = id,
            userUpdateBody = UserUpdateBody(
                password = password,
                name = name,
                email = email,
                github = github,
                company = company,
            )
        )
    }

    override suspend fun uploadProfileImage(
        id: Int,
        profileImage: File?
    ): Response<ResponseBody<UserProfileUpdateResponse>> {
        val image = if (profileImage != null) {
            MultipartBody.Part.createFormData(
                IMAGE_KEY,
                profileImage.name,
                profileImage.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            )
        } else {
            MultipartBody.Part.createFormData(IMAGE_KEY, "")
        }
        return api.uploadProfileImage(
            userId = id,
            image = image
        )
    }

    override suspend fun createAnonymous(
        AnonymousCreateInfoBody: AnonymousCreateInfoBody
    ): Response<ResponseBody<Unit>> {
        return api.createAnonymous(AnonymousCreateInfoBody)
    }

    companion object {
        private const val IMAGE_KEY = "image"
    }
}