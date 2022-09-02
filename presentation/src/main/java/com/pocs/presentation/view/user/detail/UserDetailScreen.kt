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
import com.pocs.presentation.model.user.item.UserDefaultInfoUiState
import com.pocs.presentation.view.component.UserProfileImage
import com.pocs.presentation.view.component.button.AppBarBackButton
import com.pocs.presentation.view.component.FailureContent
import com.pocs.presentation.view.component.LoadingContent
import com.pocs.presentation.view.component.RecheckDialog
import com.pocs.presentation.view.component.button.DropdownButton
import com.pocs.presentation.view.component.button.DropdownOption
import com.pocs.presentation.view.post.by.user.PostByUserActivity
import com.pocs.presentation.view.user.edit.UserEditActivity
import kotlinx.coroutines.launch

private const val URL_TAG = "url"

@Composable
fun UserDetailScreen(
    uiState: UserDetailUiState,
    onEdited: () -> Unit,
    onSuccessToKick: () -> Unit
) {
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

            if (uiState.successToKick) {
                LaunchedEffect(uiState.successToKick) {
                    onSuccessToKick()
                }
            }

            UserDetailContent(
                userDetail = uiState.userDetail,
                snackBarHostState = snackBarHostState,
                isCurrentUserAdmin = uiState.isCurrentUserAdmin,
                isMyInfo = uiState.isMyInfo,
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
    isMyInfo: Boolean,
    onConfirmToKick: () -> Unit,
    onEdited: () -> Unit,
    isCurrentUserAdmin: Boolean
) {
    val userDefaultInfo = userDetail.defaultInfo
    val name = userDefaultInfo?.name ?: stringResource(id = R.string.anonymous_name, userDetail.id)
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
            text = stringResource(R.string.kick_user_recheck_dialog_text, name),
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
                name = name,
                isUserKicked = userDetail.isKicked,
                displayActions = isCurrentUserAdmin,
                onKickClick = { showKickRecheckDialog = true },
                onSeeUsersPostClick = {
                    val intent = PostByUserActivity.getIntent(
                        context,
                        userId = userDetail.id,
                        name = name
                    )
                    context.startActivity(intent)
                }
            )
        },
        floatingActionButton = {
            if (isMyInfo) {
                val context = LocalContext.current

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
        }
    ) { innerPadding ->
        if (userDefaultInfo != null) { // 회원인 경우
            MemberUserInfoList(
                modifier = Modifier.padding(innerPadding),
                isKicked = userDetail.isKicked,
                userDefaultInfo = userDefaultInfo
            )
        } else { // 비회원인 경우
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(R.string.anonymous_user),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    )
                    if (userDetail.isKicked) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            stringResource(R.string.is_kicked),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                            )
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
                val options = mutableListOf(R.string.see_user_post)

                if (!isUserKicked) {
                    options += R.string.kick
                }

                DropdownButton(
                    options = options.map { resourceId ->
                        DropdownOption(
                            label = stringResource(resourceId),
                            onClick = {
                                when (resourceId) {
                                    R.string.kick -> onKickClick()
                                    R.string.see_user_post -> onSeeUsersPostClick()
                                    else -> throw IllegalArgumentException()
                                }
                            },
                            labelColor = if (resourceId == R.string.kick) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    },
                )
            }
        }
    )
}

@Composable
private fun UserAvatarWithName(name: String, profileImageUrl: String?, isKicked: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
    ) {
        UserProfileImage(imageModel = profileImageUrl)
        Spacer(modifier = Modifier.height(8.dp))
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
fun MemberUserInfoList(
    modifier: Modifier = Modifier,
    isKicked: Boolean,
    userDefaultInfo: UserDefaultInfoUiState
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            UserAvatarWithName(
                name = userDefaultInfo.name,
                profileImageUrl = userDefaultInfo.profileImageUrl,
                isKicked = isKicked
            )
        }
        item {
            UserInfo(
                stringResource(R.string.generation),
                userDefaultInfo.generation.toString()
            )
        }
        item {
            UserInfo(
                stringResource(R.string.student_id),
                userDefaultInfo.studentId.toString()
            )
        }
        item {
            UserInfo(
                stringResource(R.string.company),
                userDefaultInfo.company ?: stringResource(R.string.empty_label)
            )
        }
        item {
            val github = userDefaultInfo.github ?: stringResource(R.string.empty_label)
            UserInfo(
                label = stringResource(R.string.github),
                link = github,
                annotation = github
            )
        }
        item {
            UserInfo(
                label = stringResource(R.string.email),
                link = userDefaultInfo.email,
                annotation = stringResource(R.string.mailto_scheme, userDefaultInfo.email)
            )
        }
        item {
            Box(modifier = Modifier.height(dimensionResource(id = R.dimen.fab_height)))
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
            2,
            defaultInfo = UserDefaultInfoUiState(
                name = "권김정",
                email = "abc@google.com",
                profileImageUrl = null,
                studentId = 1971034,
                company = null,
                generation = 30,
                github = "https://github.com/"
            ),
            type = UserType.ADMIN,
            createdAt = "2021-02-12",
            canceledAt = null
        ),
        isMyInfo = true,
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
            id = 2,
            defaultInfo = UserDefaultInfoUiState(
                name = "권김정",
                email = "abc@google.com",
                profileImageUrl = null,
                studentId = 1971034,
                company = null,
                generation = 30,
                github = "https://github.com/"
            ),
            type = UserType.ADMIN,
            createdAt = "2021-02-12",
            canceledAt = null
        ),
        isMyInfo = true,
        snackBarHostState = SnackbarHostState(),
        onConfirmToKick = {},
        isCurrentUserAdmin = true,
        onEdited = {}
    )
}