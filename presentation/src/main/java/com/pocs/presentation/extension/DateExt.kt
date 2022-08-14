package com.pocs.presentation.extension

import org.joda.time.DateTime
import org.joda.time.Days.daysBetween
import org.joda.time.format.DateTimeFormat

enum class DatePattern(val value: String) {
    COMPACT("yyyy-MM-dd"),
    FULL("yyyy-MM-dd HH:mm:ss")
}

fun String.toFormattedDateString(): String {
    val patterns = DatePattern.values()
    for (pattern in patterns) {
        val formatter = DateTimeFormat.forPattern(pattern.value)
        try {
            val date = formatter.parseDateTime(this) ?: continue
            val localDate = date.toLocalDate()
            val nowTimeAtStartOfDay = DateTime.now().withTimeAtStartOfDay()

            var result = when (daysBetween(date.withTimeAtStartOfDay(), nowTimeAtStartOfDay).days) {
                0 -> "오늘"
                1 -> "어제"
                2 -> "그저께"
                else -> {
                    if (date.year == nowTimeAtStartOfDay.year) {
                        localDate.toString("M월 d일")
                    } else {
                        localDate.toString("yyyy년 M월 d일")
                    }
                }
            }
            if (pattern == DatePattern.FULL) {
                result += date.toLocalDateTime().toString(" H:mm")
            }
            return result
        } catch (e: Exception) {
            continue
        }
    }
    throw IllegalArgumentException("DatePattern들 중에 포함되지 않는 문자열입니다.")
}

fun createFormattedDateText(createdAt: String, updatedAt: String?): String {
    if (updatedAt != null) {
        return "${updatedAt.toFormattedDateString()}(수정됨)"
    }
    return createdAt.toFormattedDateString()
}