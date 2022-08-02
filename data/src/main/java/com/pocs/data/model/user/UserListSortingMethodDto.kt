package com.pocs.data.model.user

import com.google.gson.annotations.SerializedName

enum class UserListSortingMethodDto {
    @SerializedName("studentId")
    STUDENT_ID,

    @SerializedName("generation")
    GENERATION
}