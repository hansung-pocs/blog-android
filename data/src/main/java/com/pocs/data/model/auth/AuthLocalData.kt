package com.pocs.data.model.auth

import java.io.Serializable

data class AuthLocalData(
    val sessionToken: String,
    val userId: Int
) : Serializable
