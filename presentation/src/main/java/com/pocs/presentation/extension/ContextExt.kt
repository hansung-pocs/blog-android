package com.pocs.presentation.extension

import android.content.Context
import android.content.res.Configuration

val Context.isDarkMode: Boolean
    get() {
        val darkModeFlag = this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }
