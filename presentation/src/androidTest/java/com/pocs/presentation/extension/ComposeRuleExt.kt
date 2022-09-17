package com.pocs.presentation.extension

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText

inline fun ComposeTestRule.assertSnackBarIsDisplayed(
    before: () -> Unit,
    message: String
) {
    mainClock.autoAdvance = false

    before()

    mainClock.advanceTimeByFrame() // trigger recomposition
    waitForIdle() // await layout pass to set up animation
    mainClock.advanceTimeByFrame() // give animation a start time
    mainClock.advanceTimeBy(500)

    onNodeWithText(message).assertIsDisplayed()

    mainClock.autoAdvance = true
}
