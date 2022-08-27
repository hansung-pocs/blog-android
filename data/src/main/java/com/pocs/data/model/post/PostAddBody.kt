package com.pocs.data.model.post

data class PostAddBody(
    val title: String,
    val onlyMember: Boolean,
    val content: String,
    val userId: Int,
    val category: String
)
