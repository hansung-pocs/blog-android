package com.pocs.data.model.user

import android.graphics.Bitmap

data class UserUpdateBody(
    val password: String?,
    val name: String,
    val email: String,
    val github: String?,
    val company: String?,
    val profileImageUrl: Bitmap?
)
