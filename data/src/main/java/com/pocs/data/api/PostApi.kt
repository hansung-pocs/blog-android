package com.pocs.data.api

import com.pocs.data.model.PostListDto
import com.pocs.data.model.ResponseBody
import retrofit2.http.GET

interface PostApi {
    @GET("posts")
    suspend fun getAll(): ResponseBody<PostListDto>
}