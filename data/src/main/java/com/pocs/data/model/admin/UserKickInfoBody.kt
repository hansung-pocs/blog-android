package com.pocs.data.model.admin

import com.google.gson.annotations.SerializedName

data class UserKickInfoBody(
    @SerializedName("canceledAt") val canceledAt: String
)
