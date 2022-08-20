package com.pocs.test_library.fake.source.remote

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.post.*
import com.pocs.data.source.PostRemoteDataSource
import retrofit2.Response
import javax.inject.Inject

class FakePostRemoteDataSource @Inject constructor() : PostRemoteDataSource {

    override suspend fun getPostDetail(postId: Int): Response<ResponseBody<PostDetailDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun addPost(postAddBody: PostAddBody): Response<ResponseBody<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePost(
        postId: Int,
        postDeleteBody: PostDeleteBody
    ): Response<ResponseBody<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePost(
        postId: Int,
        postUpdateBody: PostUpdateBody
    ): Response<ResponseBody<Unit>> {
        TODO("Not yet implemented")
    }
}