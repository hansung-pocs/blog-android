package com.pocs.presentation.model.user.item

import java.text.ParseException
import java.text.SimpleDateFormat

data class UserItemUiState(
    val id: Int,
    val name: String,
    val studentId: String,
    val generation: Int,
    val canceledAt: String
) {
    val isKicked: Boolean
        get() {
            val dateFormat = SimpleDateFormat("yyyy-MM-DD")
            return try {
                dateFormat.parse(canceledAt)
                true
            } catch (e: ParseException) {
                false
            }
        }
}
