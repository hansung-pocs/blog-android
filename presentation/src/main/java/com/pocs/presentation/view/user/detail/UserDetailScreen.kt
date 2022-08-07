package com.pocs.presentation.view.user.detail

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.R
import com.pocs.presentation.extension.RefreshStateContract
import com.pocs.presentation.model.user.item.UserDetailItemUiState
import com.pocs.presentation.model.user.UserDetailUiState
import com.pocs.presentation.view.component.button.AppBarBackButton
import com.pocs.presentation.view.component.FailureContent
import com.pocs.presentation.view.component.LoadingContent
import com.pocs.presentation.view.component.RecheckDialog
import com.pocs.presentation.view.post.by.user.PostByUserActivity
import com.pocs.presentation.view.user.edit.UserEditActivity
import kotlinx.coroutines.launch

private const val URL_TAG = "url"

@Composable
fun UserDetailScreen(uiState: UserDetailUiState, onEdited: () -> Unit) {
    when (uiState) {
        is UserDetailUiState.Loading -> {
            LoadingContent()
        }
        is UserDetailUiState.Success -> {
            val snackBarHostState = remember { SnackbarHostState() }

            if (uiState.errorMessage != null) {
                LaunchedEffect(uiState.errorMessage) {
                    snackBarHostState.showSnackbar(uiState.errorMessage)
                    uiState.shownErrorMessage()
                }
            }

            UserDetailContent(
                userDetail = uiState.userDetail,
                snackBarHostState = snackBarHostState,
                isCurrentUserAdmin = uiState.isCurrentUserAdmin,
                onConfirmToKick = uiState.onKickClick,
                onEdited = onEdited
            )
        }
        is UserDetailUiState.Failure -> {
            UserDetailFailureContent(
                message = uiState.e.message ?: stringResource(R.string.failed_to_load),
                onRetryClick = uiState.onRetryClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailContent(
    userDetail: UserDetailItemUiState,
    snackBarHostState: SnackbarHostState,
    onConfirmToKick: () -> Unit,
    onEdited: () -> Unit,
    isCurrentUserAdmin: Boolean
) {
    var showKickRecheckDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val userEditActivityResult = rememberLauncherForActivityResult(
        contract = RefreshStateContract(),
        onResult = {
            if (it != null) {
                onEdited()
                it.message?.let { message ->
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(message)
                    }
                }
            }
        }
    )

    if (showKickRecheckDialog) {
        RecheckDialog(
            title = stringResource(R.string.kick_user_recheck_dialog_title),
            text = stringResource(R.string.kick_user_recheck_dialog_text, userDetail.name),
            confirmText = stringResource(id = R.string.kick),
            onOkClick = {
                onConfirmToKick()
                showKickRecheckDialog = false
            },
            onDismissRequest = { showKickRecheckDialog = false }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            val context = LocalContext.current

            UserDetailTopBar(
                name = userDetail.name,
                isUserKicked = userDetail.isKicked,
                displayActions = isCurrentUserAdmin,
                onKickClick = { showKickRecheckDialog = true },
                onSeeUsersPostClick = {
                    val intent = PostByUserActivity.getIntent(
                        context,
                        userId = userDetail.id,
                        userName = userDetail.name
                    )
                    context.startActivity(intent)
                }
            )
        },
        floatingActionButton = {
            val context = LocalContext.current
            // TODO: 본인 정보인 경우에만 내 정보 수정 버튼 보이기
            ExtendedFloatingActionButton(
                onClick = {
                    val intent = UserEditActivity.getIntent(context, userDetail)
                    userEditActivityResult.launch(intent)
                }
            ) {
                Icon(Icons.Filled.Edit, contentDescription = stringResource(id = R.string.edit))
                Text(
                    stringResource(R.string.edit_my_info),
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                UserAvatar(name = userDetail.name, isKicked = userDetail.isKicked)
            }
            item {
                UserInfo(
                    stringResource(R.string.generation),
                    userDetail.generation.toString()
                )
            }
            item {
                UserInfo(
                    stringResource(R.string.student_id),
                    userDetail.studentId.toString()
                )
            }
            item {
                UserInfo(
                    stringResource(R.string.company),
                    userDetail.company ?: stringResource(R.string.empty_label)
                )
            }
            item {
                val github = userDetail.github ?: stringResource(R.string.empty_label)
                UserInfo(
                    label = stringResource(R.string.github),
                    link = github,
                    annotation = github
                )
            }
            item {
                UserInfo(
                    label = stringResource(R.string.email),
                    link = userDetail.email,
                    annotation = stringResource(R.string.mailto_scheme, userDetail.email)
                )
            }
            item {
                Box(modifier = Modifier.height(dimensionResource(id = R.dimen.fab_height)))
            }
        }
    }
}

@Composable
fun UserDetailTopBar(
    name: String,
    displayActions: Boolean,
    isUserKicked: Boolean,
    onKickClick: () -> Unit,
    onSeeUsersPostClick: () -> Unit
) {
    SmallTopAppBar(
        title = { Text(text = stringResource(R.string.user_info_title, name)) },
        navigationIcon = { AppBarBackButton() },
        actions = {
            if (displayActions) {
                var showDropdownMenu by remember { mutableStateOf(false) }
                val options = remember(isUserKicked) {
                    if (isUserKicked) {
                        listOf(R.string.see_user_post)
                    } else {
                        listOf(R.string.see_user_post, R.string.kick)
                    }
                }

                IconButton(onClick = { showDropdownMenu = !showDropdownMenu }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = stringResource(id = R.string.more_info_button)
                    )
                }
                DropdownMenu(
                    expanded = showDropdownMenu,
                    onDismissRequest = { showDropdownMenu = false },
                ) {
                    options.forEach { stringResourceId ->
                        DropdownMenuItem(
                            onClick = {
                                when (stringResourceId) {
                                    R.string.kick -> onKickClick()
                                    R.string.see_user_post -> onSeeUsersPostClick()
                                    else -> throw IllegalArgumentException()
                                }
                                showDropdownMenu = false
                            },
                            text = {
                                Text(
                                    text = stringResource(stringResourceId),
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = if (stringResourceId == R.string.kick) {
                                            MaterialTheme.colorScheme.error
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun UserAvatar(name: String, isKicked: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
    ) {
        // TODO: 회원 사진 정보가 생기면 넣기
        Icon(
            modifier = Modifier.size(120.dp),
            imageVector = Icons.Filled.AccountCircle,
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = stringResource(id = R.string.user_image)
        )
        Text(text = name, style = MaterialTheme.typography.titleMedium)
        if (isKicked) {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.is_kicked),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.error
                )
            )
        }
    }
}

@Composable
fun UserInfo(label: String, value: String) {
    val annotatedString = buildAnnotatedString {
        append(value)
    }
    UserInfoContainer(label, annotatedString)
}

// TODO: [annotation] nullable로 수정하기
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
                    try {
                        uriHandler.openUri(it.item)
                    } catch (e: Exception) {
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailFailureContent(message: String, onRetryClick: () -> Unit) {
    Scaffold(
        topBar = { UserDetailTopBar("", displayActions = false, isUserKicked = false, {}, {}) }
    ) {
        FailureContent(
            modifier = Modifier.padding(it),
            message = message,
            onRetryClick = onRetryClick
        )
    }
}

@Composable
@Preview
fun UserDetailContentPreview() {
    UserDetailContent(
        userDetail = UserDetailItemUiState(
            1,
            "김민성",
            "jja08111@gmail.com",
            1234528,
            UserType.MEMBER,
            "Hello",
            30,
            "https://github/jja08111",
            "2022-04-04",
            "-"
        ),
        snackBarHostState = SnackbarHostState(),
        onConfirmToKick = {},
        isCurrentUserAdmin = true,
        onEdited = {}
    )
}

@Composable
@Preview
fun KickedUserDetailContentPreview() {
    UserDetailContent(
        userDetail = UserDetailItemUiState(
            1,
            "김민성",
            "jja08111@gmail.com",
            1234528,
            UserType.MEMBER,
            "Hello",
            30,
            "https://github/jja08111",
            "2022-04-04",
            "2022-04-04",
        ),
        snackBarHostState = SnackbarHostState(),
        onConfirmToKick = {},
        isCurrentUserAdmin = true,
        onEdited = {}
    )
}