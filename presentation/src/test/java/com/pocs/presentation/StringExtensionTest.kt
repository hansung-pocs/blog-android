package com.pocs.presentation

import com.pocs.presentation.extension.isValidGithubUserUrl
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StringExtensionTest {

    @Test
    fun isValidGithubUrlReturnsTrue_WhenUrlIsValid() {
        val urls = listOf(
            "https://github.com/use2r",
            "https://github.com/us4er-fga",
            "https://github.com/W"
        )

        urls.forEach {
            assertTrue(it.isValidGithubUserUrl())
        }
    }

    @Test
    fun isValidGithubUrlReturnsFalse_WhenUrlIsNotValid() {
        val urls = listOf(
            "https://github.cm/use4r",
            "http://github.com/user3-fga",
            "https://github.com/"
        )

        urls.forEach {
            assertFalse(it.isValidGithubUserUrl())
        }
    }
}