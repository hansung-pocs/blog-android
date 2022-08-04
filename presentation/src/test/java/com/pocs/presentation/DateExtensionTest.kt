package com.pocs.presentation

import com.pocs.presentation.extension.DatePattern
import com.pocs.presentation.extension.isDateFormat
import com.pocs.presentation.extension.toFormattedDateString
import com.pocs.test_library.rule.JodaRule
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class DateExtensionTest {

    @get:Rule
    val jodaRule = JodaRule()

    @Test
    fun shouldBeTrue_WhenStringMatchesDateFormat() {
        val isDateFormat = "2022-01-01".isDateFormat(DatePattern.COMPACT)
        assertTrue(isDateFormat)
    }

    @Test
    fun shouldBeFalse_WhenStringDoesNotMatchDateFormat() {
        val isDateFormat = "-".isDateFormat(DatePattern.COMPACT)
        assertFalse(isDateFormat)
    }

    @Test
    fun shouldReturnTodayKorean_WhenPatternIsCompact() {
        val date = DateTime(2022, 8, 4, 10, 22)
        DateTimeUtils.setCurrentMillisFixed(date.millis)

        val result = "2022-08-04".toFormattedDateString()
        assertEquals("오늘", result)
    }

    @Test
    fun shouldReturnYesterdayKorean_WhenPatternIsCompact() {
        val date = DateTime(2022, 8, 4, 10, 22)
        DateTimeUtils.setCurrentMillisFixed(date.millis)

        val result = "2022-08-03".toFormattedDateString()
        assertEquals("어제", result)
    }

    @Test
    fun shouldReturnYesterdayOfYesterdayKorean_WhenPatternIsCompact() {
        val date = DateTime(2022, 8, 4, 10, 22)
        DateTimeUtils.setCurrentMillisFixed(date.millis)

        val result = "2022-08-02".toFormattedDateString()
        assertEquals("그저께", result)
    }

    @Test
    fun shouldReturnDateStringKorean_WhenPatternIsCompact() {
        val date = DateTime(2022, 8, 4, 10, 22)
        DateTimeUtils.setCurrentMillisFixed(date.millis)

        val result = "2022-08-01".toFormattedDateString()
        assertEquals("8월 1일", result)
    }

    @Test
    fun shouldReturnDateStringKorean_WhenSameYearAndPatternIsCompact() {
        val date = DateTime(2022, 8, 4, 10, 22)
        DateTimeUtils.setCurrentMillisFixed(date.millis)

        val result = "2022-01-01".toFormattedDateString()
        assertEquals("1월 1일", result)
    }

    @Test
    fun shouldReturnDateStringKorean_WhenDifferentYearAndPatternIsCompact() {
        val date = DateTime(2022, 8, 4, 10, 22)
        DateTimeUtils.setCurrentMillisFixed(date.millis)

        val result = "2021-12-31".toFormattedDateString()
        assertEquals("2021년 12월 31일", result)
    }

    @Test
    fun shouldReturnTodayKorean_WhenPatternIsFull() {
        val date = DateTime(2022, 8, 4, 10, 22)
        DateTimeUtils.setCurrentMillisFixed(date.millis)

        val result = "2022-08-04 16:23:45".toFormattedDateString()
        assertEquals("오늘", result)
    }

    @Test
    fun shouldReturnYesterdayKorean_WhenPatternIsFull() {
        val date = DateTime(2022, 8, 4, 10, 22)
        DateTimeUtils.setCurrentMillisFixed(date.millis)

        val result = "2022-08-03 16:23:45".toFormattedDateString()
        assertEquals("어제", result)
    }

    @Test
    fun shouldReturnYesterdayOfYesterdayKorean_WhenPatternIsFull() {
        val date = DateTime(2022, 8, 4, 10, 22)
        DateTimeUtils.setCurrentMillisFixed(date.millis)

        val result = "2022-08-02 16:23:45".toFormattedDateString()
        assertEquals("그저께", result)
    }

    @Test
    fun shouldReturnDateStringKorean_WhenPatternIsFull() {
        val date = DateTime(2022, 8, 4, 10, 22)
        DateTimeUtils.setCurrentMillisFixed(date.millis)

        val result = "2022-08-01 16:23:45".toFormattedDateString()
        assertEquals("8월 1일 16:23", result)
    }

    @Test
    fun shouldReturnDateStringKorean_WhenSameYearAndPatternIsFull() {
        val date = DateTime(2022, 8, 4, 10, 22)
        DateTimeUtils.setCurrentMillisFixed(date.millis)

        val result = "2022-01-01 16:03:45".toFormattedDateString()
        assertEquals("1월 1일 16:03", result)
    }

    @Test
    fun shouldReturnDateStringKorean_WhenDifferentYearAndPatternIsFull() {
        val date = DateTime(2022, 8, 4, 10, 22)
        DateTimeUtils.setCurrentMillisFixed(date.millis)

        val result = "2021-12-31 01:23:45".toFormattedDateString()
        assertEquals("2021년 12월 31일 01:23", result)
    }
}
