package com.pocs.data.model.post

import com.google.gson.annotations.SerializedName

data class PostListDto(
    @SerializedName("posts") val posts: List<PostDto>
)