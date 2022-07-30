package com.pocs.data.source

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.user.UserDto

interface UserRemoteDataSource {
    suspend fun getUserDetail(id: Int): ResponseBody<UserDto>
}