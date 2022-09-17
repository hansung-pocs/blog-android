package com.pocs.presentation.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting

private const val MESSAGE = "snackBarMessage"

@VisibleForTesting
const val RESULT_REFRESH = 25142

fun Activity.setResultRefresh(@StringRes res: Int? = null) {
    val intent = Intent().apply {
        if (res != null) putExtra(MESSAGE, getString(res))
    }
    setResult(RESULT_REFRESH, intent)
}

class RefreshStateResult(val message: String?)

class RefreshStateContract : ActivityResultContract<Intent, RefreshStateResult?>() {

    override fun createIntent(context: Context, input: Intent) = input

    override fun parseResult(resultCode: Int, intent: Intent?): RefreshStateResult? {
        if (resultCode == RESULT_REFRESH) {
            val message = intent?.getStringExtra(MESSAGE)
            return RefreshStateResult(message)
        }
        return null
    }
}
