package com.pocs.data.model.user

import com.google.gson.annotations.SerializedName

enum class UserListSortingMethodDto(val value: String) {
    @SerializedName("studentId")
    STUDENT_ID("studentId"),

    @SerializedName("generation")
    GENERATION("generation")
}