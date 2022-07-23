package com.pocs.presentation.view.post.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PostEditPage() {
    Scaffold(
        topBar = {
            TopAppBar(contentPadding = PaddingValues(horizontal = 24.dp)) {
                Text(text = "게시글 수정")
            }
        }
    ) {
        val scrollState = rememberScrollState()

        Column(Modifier.verticalScroll(scrollState)) {
            Text(text = "이것이 컴포즈")
        }
    }
}
