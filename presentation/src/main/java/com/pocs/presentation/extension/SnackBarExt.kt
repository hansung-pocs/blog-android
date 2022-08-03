package com.pocs.presentation.extension

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

private const val EXTRA_NAME = "snackBarMessage"

fun ActivityResult.getSnackBarMessage(): String? {
    return data?.getStringExtra(EXTRA_NAME)
}

fun Activity.setResultOkWithSnackBarMessage(@StringRes res: Int) {
    val intent = Intent().putExtra(EXTRA_NAME, getString(res))
    setResult(AppCompatActivity.RESULT_OK, intent)
}
