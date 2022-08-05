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
            val nowTimeAtStartOfDay = DateTime.now().withTimeAtStartOfDay()

            return when (daysBetween(date.withTimeAtStartOfDay(), nowTimeAtStartOfDay).days) {
                0 -> {
                    when (pattern) {
                        DatePattern.COMPACT -> date.toLocalDate().toString("오늘")
                        DatePattern.FULL -> date.toLocalDateTime().toString("오늘 H:mm")
                    }
                }
                1 -> {
                    when (pattern) {
                        DatePattern.COMPACT -> date.toLocalDate().toString("어제")
                        DatePattern.FULL -> date.toLocalDateTime().toString("어제 H:mm")
                    }
                }
                2 -> {
                    when (pattern) {
                        DatePattern.COMPACT -> date.toLocalDate().toString("그저께")
                        DatePattern.FULL -> date.toLocalDateTime().toString("그저께 H:mm")
                    }
                }
                else -> {
                    if (date.year == nowTimeAtStartOfDay.year) {
                        when (pattern) {
                            DatePattern.COMPACT -> date.toLocalDate().toString("M월 d일")
                            DatePattern.FULL -> date.toLocalDateTime().toString("M월 d일 H:mm")
                        }
                    } else {
                        when (pattern) {
                            DatePattern.COMPACT -> date.toLocalDate().toString("yyyy년 M월 d일")
                            DatePattern.FULL -> date.toLocalDateTime()
                                .toString("yyyy년 M월 d일 H:mm")
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