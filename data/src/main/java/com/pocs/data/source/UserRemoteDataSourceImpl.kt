package com.pocs.data.source

import com.pocs.data.api.UserApi
import com.pocs.data.model.ResponseBody
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
        github: String?,
        profileImage: File?
    ): Response<ResponseBody<Unit>> {
        return api.updateUser(
            userId = id,
            password = password?.let { MultipartBody.Part.createFormData("password", it) },
            name = MultipartBody.Part.createFormData("name", name),
            email = MultipartBody.Part.createFormData("email", email),
            github = github?.let { MultipartBody.Part.createFormData("github", it) },
            company = company?.let { MultipartBody.Part.createFormData("company", it) },
            profileImage = profileImage?.let {
                MultipartBody.Part.createFormData(
                    "profileImage",
                    it.name,
                    it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                )
            },
        )
    }

    override suspend fun createAnonymous(
        AnonymousCreateInfoBody: AnonymousCreateInfoBody
    ): Response<ResponseBody<Unit>> {
        return api.createAnonymous(AnonymousCreateInfoBody)
    }
}