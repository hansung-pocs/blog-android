package com.pocs.presentation.view.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import java.net.ConnectException

@Composable
fun FailureContent(
    modifier: Modifier = Modifier,
    exception: Throwable,
    onRetryClick: (() -> Unit)? = null
) {
    val message = when (exception) {
        is ConnectException -> stringResource(id = R.string.fail_to_connect)
        else -> exception.message ?: stringResource(id = R.string.failed_to_load)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = stringResource(R.string.error_icon),
            modifier = Modifier.size(96.dp),
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.padding(top = 16.dp))
        SelectionContainer {
            Text(
                message,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(0.7f)
            )
        }
        if (onRetryClick != null) {
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Button(onClick = onRetryClick) {
                Text(text = stringResource(R.string.retry))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FailureContentPreview() {
    FailureContent(exception = Exception("로딩을 실패했습니다."), onRetryClick = {})
}
