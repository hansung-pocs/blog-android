package com.pocs.presentation.model.user.item

import android.graphics.Bitmap
import java.io.Serializable

data class UserDefaultInfoUiState(
    val name: String,
    val email: String,
    val profileImageUrl: String?,
    val studentId: Int,
    val company: String?,
    val generation: Int,
    val github: String?,
    val profileImageUrl: Bitmap?
) : Serializable