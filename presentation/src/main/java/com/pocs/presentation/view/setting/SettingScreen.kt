package com.pocs.presentation.view.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.R
import com.pocs.presentation.model.setting.SettingUiState
import com.pocs.presentation.view.component.PocsDivider
import com.pocs.presentation.view.component.RecheckDialog
import com.pocs.presentation.view.component.button.AppBarBackButton
import com.pocs.presentation.view.user.detail.UserDetailActivity

@Composable
fun SettingScreen(
    viewModel: SettingViewModel,
    onLoginClick: () -> Unit,
    onSuccessToLogout: () -> Unit
) {
    if (viewModel.uiState.onSuccessToLogout) {
        onSuccessToLogout()
    }

    SettingContent(
        uiState = viewModel.uiState,
        onLogoutClick = {
            assert(viewModel.uiState.currentUser != null)
            viewModel.logout()
        },
        onLoginClick = onLoginClick,
        onErrorMessageShow = viewModel::errorMessageShown
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingContent(
    uiState: SettingUiState,
    onLogoutClick: () -> Unit,
    onLoginClick: () -> Unit,
    onErrorMessageShow: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        RecheckDialog(
            title = stringResource(R.string.logout_dialog_title),
            confirmText = stringResource(R.string.logout),
            onDismissRequest = { showLogoutDialog = false },
            onOkClick = onLogoutClick
        )
    }

    if (uiState.errorMessage != null) {
        LaunchedEffect(uiState.errorMessage) {
            snackbarHostState.showSnackbar(uiState.errorMessage)
            onErrorMessageShow()
        }
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = stringResource(id = R.string.setting)) },
                navigationIcon = { AppBarBackButton() }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        val scrollState = rememberScrollState()

        Column(
            Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            val isLoggedIn = uiState.currentUser != null
            val context = LocalContext.current

            if (isLoggedIn) {
                SettingUserTile(
                    name = uiState.currentUser!!.name,
                    studentId = uiState.currentUser.studentId.toString(),
                    onClick = {
                        val intent = UserDetailActivity.getIntent(context, uiState.currentUser.id)
                        context.startActivity(intent)
                    }
                )
            } else {
                SettingNonMemberTile(onLoginClick = onLoginClick)
            }
            PocsDivider(startIndent = 20.dp)
            if (isLoggedIn) {
                SettingTile(
                    title = stringResource(R.string.logout),
                    titleColor = MaterialTheme.colorScheme.error,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = stringResource(id = R.string.logout),
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    onClick = { showLogoutDialog = true }
                )
            }
        }
    }
}

@Composable
fun SettingNonMemberTile(onLoginClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(64.dp),
            imageVector = Icons.Filled.AccountCircle,
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = stringResource(id = R.string.user_image)
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f),
            text = stringResource(R.string.anonymous),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        TextButton(onClick = onLoginClick) {
            Text(text = stringResource(id = R.string.navigate_to_login_screen))
        }
    }
}

@Composable
fun SettingUserTile(name: String, studentId: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO: 회원 사진으로 수정하기
        Icon(
            modifier = Modifier.size(64.dp),
            imageVector = Icons.Filled.AccountCircle,
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = stringResource(id = R.string.user_image)
        )
        Box(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Box(modifier = Modifier.height(4.dp))
            Text(
                text = studentId,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = stringResource(id = R.string.more_info_button)
        )
    }
}

@Composable
fun SettingTile(
    title: String,
    onClick: () -> Unit,
    titleColor: Color? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon?.invoke()
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                color = titleColor ?: MaterialTheme.colorScheme.onBackground
            )
        )
        trailingIcon?.invoke()
    }
}

@Composable
@Preview
fun SettingScreenNotLoginPreview() {
    SettingContent(SettingUiState(), onLogoutClick = {}, onLoginClick = {}, onErrorMessageShow = {})
}

@Composable
@Preview
fun SettingScreenLoginPreview() {
    SettingContent(
        SettingUiState(
            currentUser = UserDetail(
                2,
                "권김정",
                "abc@google.com",
                1971034,
                UserType.ADMIN,
                null,
                30,
                "https://github.com/",
                "2021-02-12",
                null,
            )
        ),
        onLogoutClick = {},
        onLoginClick = {},
        onErrorMessageShow = {}
    )
}

@Composable
@Preview(showBackground = true)
fun SettingUserTilePreview() {
    SettingUserTile(name = "홍길동", studentId = "1921034") {

    }
}

@Composable
@Preview(showBackground = true)
fun SettingTilePreview() {
    SettingTile(
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Logout, contentDescription = "", tint = Color.Red)
        },
        titleColor = Color.Red,
        title = "로그아웃",
        onClick = {}
    )
}