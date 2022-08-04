package com.pocs.presentation.extension

import org.joda.time.DateTime
import org.joda.time.Days.daysBetween
import org.joda.time.format.DateTimeFormat
import java.text.ParseException
import java.text.SimpleDateFormat

enum class DatePattern(val value: String) {
    COMPACT("yyyy-MM-dd"),
    FULL("yyyy-MM-dd HH:mm:ss")
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

fun String.toFormattedDateString(): String {
    val patterns = DatePattern.values()
    for (pattern in patterns) {
        val formatter = DateTimeFormat.forPattern(pattern.value)
        try {
            val date = formatter.parseDateTime(this) ?: continue
            val nowTimeAtStartOfDay = DateTime.now().withTimeAtStartOfDay()

            return when (daysBetween(date.withTimeAtStartOfDay(), nowTimeAtStartOfDay).days) {
                0 -> "오늘"
                1 -> "어제"
                2 -> "그저께"
                else -> {
                    if (date.year == nowTimeAtStartOfDay.year) {
                        when (pattern) {
                            DatePattern.COMPACT -> date.toLocalDate().toString("M월 d일")
                            DatePattern.FULL -> date.toLocalDateTime().toString("M월 d일 HH:mm")
                        }
                    } else {
                        when (pattern) {
                            DatePattern.COMPACT -> date.toLocalDate().toString("yyyy년 M월 d일")
                            DatePattern.FULL -> date.toLocalDateTime()
                                .toString("yyyy년 M월 d일 HH:mm")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            continue
        }
    }
    throw IllegalArgumentException("DatePattern들 중에 포함되지 않는 문자열입니다.")
}