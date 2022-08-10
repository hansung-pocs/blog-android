package com.pocs.data.model.auth

data class LoginResponseData(
    val sessionToken: String,
    val userId: Int
)
