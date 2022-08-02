package com.pocs.presentation.extension

import java.text.ParseException
import java.text.SimpleDateFormat

enum class DatePattern(val value: String) {
    COMPACT("yyyy-MM-DD"),
    FULL("yyyy-MM-DD HH:MM:SS")
}

fun String.isDateFormat(pattern: DatePattern): Boolean {
    val dateFormat = SimpleDateFormat(pattern.value)
    return try {
        dateFormat.parse(this)
        true
    } catch (e: ParseException) {
        false
    }
}