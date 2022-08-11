package com.pocs.presentation.view.component

import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PocsDivider(startIndent: Dp = 0.dp) {
    Divider(startIndent = startIndent, modifier = Modifier.alpha(0.4f))
}