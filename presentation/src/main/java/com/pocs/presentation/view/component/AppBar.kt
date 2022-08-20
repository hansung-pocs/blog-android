package com.pocs.presentation.view.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import kotlin.math.abs

@Composable
fun rememberTitleAlphaFromScrollOffset(key: Any, lazyListState: LazyListState) = remember {
    derivedStateOf {
        val visibleItemsInfo = lazyListState.layoutInfo.visibleItemsInfo
        if (visibleItemsInfo.isEmpty()) {
            return@derivedStateOf 0.0f
        }
        val headerItemInfo = lazyListState.layoutInfo.visibleItemsInfo.first()
        if (headerItemInfo.key != key) {
            return@derivedStateOf 1.0f
        }
        val headerSize = headerItemInfo.size.toFloat()
        val headerOffset = headerItemInfo.offset.toFloat()
        abs(headerOffset).coerceIn(0.0f, headerSize) / headerSize
    }
}