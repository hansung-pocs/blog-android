package com.pocs.data.model.auth

import java.io.Serializable

data class AuthLocalData(
    val token: String,
    val userId: Int
) : Serializable
