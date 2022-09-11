package com.pocs.data.model.post

import com.google.gson.annotations.SerializedName

data class PostAddBody(
    @SerializedName("title") val title: String,
    @SerializedName("onlyMember") val onlyMember: Boolean,
    @SerializedName("content") val content: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("category") val category: String
)
