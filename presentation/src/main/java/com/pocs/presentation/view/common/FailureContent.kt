package com.pocs.presentation.view.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R

@Composable
fun FailureContent(modifier: Modifier = Modifier, message: String, onRetryClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SelectionContainer {
            Text(
                message,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(0.7f)
            )
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Button(onClick = onRetryClick) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FailureContentPreview() {
    FailureContent(message = "로딩을 실패했습니다.", onRetryClick = {})
}
