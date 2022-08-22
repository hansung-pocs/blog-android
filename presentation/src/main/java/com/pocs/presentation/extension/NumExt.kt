package com.pocs.presentation.extension

import android.content.res.Resources

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
