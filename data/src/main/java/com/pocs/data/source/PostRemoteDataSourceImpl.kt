package com.pocs.data.source

import com.pocs.data.api.PostApi
import com.pocs.data.model.ResponseBody
import com.pocs.data.model.post.PostAddBody
import com.pocs.data.model.post.PostDeleteBody
import com.pocs.data.model.post.PostUpdateBody
import retrofit2.Response
import javax.inject.Inject

class PostRemoteDataSourceImpl @Inject constructor(
    private val api: PostApi
) : PostRemoteDataSource {

    override suspend fun getPostDetail(postId: Int) = api.getPostDetail(postId)

    override suspend fun addPost(postAddBody: PostAddBody) = api.addPost(postAddBody)

    override suspend fun deletePost(
        postId: Int,
        postDeleteBody: PostDeleteBody
    ): Response<ResponseBody<Unit>> {
        return api.deletePost(postId, postDeleteBody)
    }

    override suspend fun updatePost(
        postId: Int,
        postUpdateBody: PostUpdateBody
    ): Response<ResponseBody<Unit>> {
        return api.updatePost(postId, postUpdateBody)
    }
}