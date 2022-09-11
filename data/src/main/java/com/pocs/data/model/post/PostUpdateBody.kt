package com.pocs.data.model.post

import com.google.gson.annotations.SerializedName

data class PostUpdateBody(
    @SerializedName("title") val title: String,
    @SerializedName("onlyMember") val onlyMember: Boolean,
    @SerializedName("content") val content: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("category") val category: String
)