package com.pocs.presentation.view.user.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.domain.model.UserType
import com.pocs.presentation.R
import com.pocs.presentation.model.UserDetailItemUiState
import com.pocs.presentation.model.UserDetailUiState
import com.pocs.presentation.view.common.AppBarBackButton

private const val URL_TAG = "url"

@Composable
fun UserDetailScreen(uiState: UserDetailUiState) {
    when (uiState) {
        is UserDetailUiState.Loading -> {}
        is UserDetailUiState.Success -> {
            UserDetailContent(uiState.userDetailItem)
        }
        is UserDetailUiState.Failure -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailContent(itemUiState: UserDetailItemUiState) {
    Scaffold(
        topBar = { UserDetailTopBar(itemUiState.name) }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 24.dp)
                .padding(bottom = FloatingActionButtonDefaults.LargeIconSize)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                UserAvatar(itemUiState.name)
            }
            item {
                UserInfo(
                    stringResource(R.string.generation),
                    stringResource(R.string.user_generation_info, itemUiState.generation)
                )
            }
            item {
                UserInfo(
                    stringResource(R.string.student_id),
                    itemUiState.studentId.toString()
                )
            }
            if (itemUiState.company.isNotEmpty()) {
                item {
                    UserInfo(
                        stringResource(R.string.company),
                        itemUiState.company
                    )
                }
            }
            item {
                UserInfo(
                    label = stringResource(R.string.github),
                    link = itemUiState.github,
                    annotation = itemUiState.github
                )
            }
            item {
                UserInfo(
                    label = stringResource(R.string.email),
                    link = itemUiState.email,
                    annotation = stringResource(R.string.mailto_scheme, itemUiState.email)
                )
            }
        }
    }
}

@Composable
fun UserDetailTopBar(name: String) {
    SmallTopAppBar(
        title = { Text(text = stringResource(R.string.user_info_title, name)) },
        navigationIcon = { AppBarBackButton() },
    )
}

@Composable
fun UserAvatar(name: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
    ) {
        // TODO: 회원 사진 정보가 생기면 넣기
        Icon(
            modifier = Modifier.size(120.dp),
            imageVector = Icons.Filled.Person,
            contentDescription = stringResource(id = R.string.user_image)
        )
        Text(text = name, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun UserInfo(label: String, value: String) {
    val annotatedString = buildAnnotatedString {
        append(value)
    }
    UserInfoContainer(label, annotatedString)
}

@Composable
fun UserInfo(label: String, link: String, annotation: String) {
    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = URL_TAG, annotation = annotation)
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(link)
        }
        pop()
    }

    UserInfoContainer(label = label, annotatedString = annotatedString)
}

@Composable
fun UserInfoContainer(label: String, annotatedString: AnnotatedString) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        val uriHandler = LocalUriHandler.current

        Text(
            label,
            modifier = Modifier.fillMaxWidth(0.3f),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        ClickableText(
            annotatedString,
            modifier = Modifier.fillMaxWidth(0.7f),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            ),
            onClick = { offset ->
                annotatedString.getStringAnnotations(
                    tag = URL_TAG,
                    offset,
                    offset
                ).firstOrNull()?.let {
                    uriHandler.openUri(it.item)
                }
            }
        )
    }
}


@Composable
@Preview
fun UserDetailContentPreview() {
    UserDetailContent(
        itemUiState = UserDetailItemUiState(
            1,
            "김민성",
            "jja08111@gmail.com",
            1234528,
            UserType.MEMBER,
            "Hello",
            30,
            "https://github/jja08111",
            "2022-04-04"
        )
    )
}