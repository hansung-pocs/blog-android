package com.pocs.presentation.extension

import android.content.res.Resources

fun Float.toDp(): Int = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
