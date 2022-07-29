package com.pocs.presentation.view.user.edit

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pocs.presentation.R
import com.pocs.presentation.model.UserEditUiState
import com.pocs.presentation.view.common.RecheckHandler
import com.pocs.presentation.view.common.appbar.EditContentAppBar
import kotlinx.coroutines.launch

@Composable
fun UserEditScreen(viewModel: UserEditViewModel, navigateUp: () -> Unit) {
    UserEditContent(uiState = viewModel.uiState.value, navigateUp = navigateUp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEditContent(uiState: UserEditUiState, navigateUp: () -> Unit) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    RecheckHandler(navigateUp = navigateUp)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            EditContentAppBar(
                title = stringResource(id = R.string.edit_my_info),
                onBackPressed = { onBackPressedDispatcher?.onBackPressed() },
                isInSaving = uiState.isInSaving,
                enableSendIcon = true,
                onClickSend = {
                    if (!uiState.isInSaving) {
                        coroutineScope.launch {
                            val result = uiState.onSave()
                            if (result.isSuccess) {
                                navigateUp()
                            } else {
                                val exception = result.exceptionOrNull()!!
                                snackBarHostState.showSnackbar(exception.message!!)
                            }
                        }
                    }
                }
            )
        }
    ) {
        Column(Modifier.padding(it)) {

        }
    }
}
