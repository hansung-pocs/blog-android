package com.pocs.data.model.user

import android.graphics.Bitmap

data class UserDefaultInfoDto(
    val name: String,
    val email: String,
    val studentId: Int,
    val company: String? = null,
    val generation: Int,
    val github: String? = null,
    val profileImageUrl: Bitmap? = null
)