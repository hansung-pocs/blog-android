package com.pocs.presentation.extension

import android.app.Activity
import android.os.Build
import java.io.Serializable

@Suppress("UNCHECKED_CAST", "DEPRECATION")
fun <T : Serializable?> getSerializableExtra(activity: Activity, name: String, clazz: Class<T>): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        activity.intent.getSerializableExtra(name, clazz)!!
    } else {
        activity.intent.getSerializableExtra(name) as T
    }
}
