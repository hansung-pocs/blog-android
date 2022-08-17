package com.pocs.presentation

import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.compose.ui.test.junit4.ComposeTestRule
import org.junit.Assert.assertTrue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Helper methods for hiding and showing the keyboard in tests.
 * Must set [view] before calling any methods on this class.
 */
class KeyboardHelper(
    private val composeRule: ComposeTestRule,
    private val timeout: Long = 15_000L
) {
    /**
     * The [View] hosting the compose rule's content. Must be set before calling any methods on this
     * class.
     */
    lateinit var view: View
    private val imm by lazy {
        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    /**
     * Requests the keyboard to be hidden without waiting for it.
     * Should be called from the main thread.
     */
    fun hideKeyboard() {
        if (Build.VERSION.SDK_INT >= 30) {
            hideKeyboardWithInsets()
        } else {
            hideKeyboardWithImm()
        }
    }

    /**
     * Blocks until the [timeout] or the keyboard's visibility matches [visible].
     * May be called from the test thread or the main thread.
     */
    fun waitForKeyboardVisibility(visible: Boolean) {
        waitUntil(timeout) {
            isSoftwareKeyboardShown() == visible
        }
    }

    fun hideKeyboardIfShown() {
        composeRule.runOnIdle {
            if (isSoftwareKeyboardShown()) {
                hideKeyboard()
                waitForKeyboardVisibility(visible = false)
            }
        }
    }

    fun isSoftwareKeyboardShown(): Boolean {
        return if (Build.VERSION.SDK_INT >= 30) {
            isSoftwareKeyboardShownWithInsets()
        } else {
            isSoftwareKeyboardShownWithImm()
        }
    }

    @RequiresApi(30)
    private fun isSoftwareKeyboardShownWithInsets(): Boolean {
        return view.rootWindowInsets != null &&
                view.rootWindowInsets.isVisible(WindowInsets.Type.ime())
    }

    private fun isSoftwareKeyboardShownWithImm(): Boolean {
        // TODO(b/163742556): This is just a proxy for software keyboard visibility. Find a better
        //  way to check if the software keyboard is shown.
        return imm.isAcceptingText
    }

    private fun hideKeyboardWithImm() {
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @RequiresApi(30)
    private fun hideKeyboardWithInsets() {
        view.windowInsetsController?.hide(WindowInsets.Type.ime())
    }

    private fun waitUntil(timeout: Long, condition: () -> Boolean) {
        if (Build.VERSION.SDK_INT >= 30) {
            view.waitUntil(timeout, condition)
        } else {
            composeRule.waitUntil(timeout, condition)
        }
    }
}

@RequiresApi(30)
fun View.waitUntil(timeoutMillis: Long, condition: () -> Boolean) {
    val latch = CountDownLatch(1)
    rootView.setWindowInsetsAnimationCallback(
        InsetAnimationCallback {
            if (condition()) {
                latch.countDown()
            }
        }
    )
    val conditionMet = latch.await(timeoutMillis, TimeUnit.MILLISECONDS)
    assertTrue(conditionMet)
}

@RequiresApi(30)
private class InsetAnimationCallback(val block: () -> Unit) :
    WindowInsetsAnimation.Callback(DISPATCH_MODE_CONTINUE_ON_SUBTREE) {

    override fun onProgress(
        insets: WindowInsets,
        runningAnimations: MutableList<WindowInsetsAnimation>
    ) = insets

    override fun onEnd(animation: WindowInsetsAnimation) {
        block()
        super.onEnd(animation)
    }
}