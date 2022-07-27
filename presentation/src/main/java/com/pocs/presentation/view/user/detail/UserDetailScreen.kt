package com.pocs.presentation.view.user.detail

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pocs.presentation.R
import com.pocs.presentation.model.UserDetailItemUiState
import com.pocs.presentation.model.UserDetailUiState
import com.pocs.presentation.view.common.AppBarBackButton

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

    }
}

@Composable
fun UserDetailTopBar(name: String) {
    SmallTopAppBar(
        title = { Text(text = stringResource(R.string.user_info_title, name)) },
        navigationIcon = { AppBarBackButton() },
    )
}
