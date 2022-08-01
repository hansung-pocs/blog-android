package com.pocs.presentation

import com.pocs.presentation.extension.DatePattern
import com.pocs.presentation.extension.isDateFormat
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StringExtensionTest {

    @Test
    fun shouldBeTrue_WhenStringMatchesDateFormat() {
        val isDateFormat = "2022-01-31".isDateFormat(DatePattern.COMPACT)
        assertTrue(isDateFormat)
    }

    @Test
    fun shouldBeFalse_WhenStringDoesNotMatchDateFormat() {
        val isDateFormat = "-".isDateFormat(DatePattern.COMPACT)
        assertFalse(isDateFormat)
    }
}