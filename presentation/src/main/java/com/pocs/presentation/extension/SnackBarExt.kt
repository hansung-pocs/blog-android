package com.pocs.presentation.extension

import android.content.Intent
import androidx.activity.result.ActivityResult

private const val EXTRA_NAME = "snackBarMessage"

fun ActivityResult.getSnackBarMessage(): String? {
    return data?.getStringExtra(EXTRA_NAME)
}

fun Intent.putSnackBarMessage(message: String): Intent {
    return putExtra(EXTRA_NAME, message)
}

