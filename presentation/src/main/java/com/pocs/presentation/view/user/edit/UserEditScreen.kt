package com.pocs.presentation.view.user.edit

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pocs.presentation.R
import com.pocs.presentation.constant.MAX_USER_COMPANY_LEN
import com.pocs.presentation.constant.MAX_USER_EMAIL_LEN
import com.pocs.presentation.constant.MAX_USER_GITHUB_LEN
import com.pocs.presentation.constant.MAX_USER_NAME_LEN
import com.pocs.presentation.model.user.UserEditUiState
import com.pocs.presentation.view.component.UserProfileImage
import com.pocs.presentation.view.component.RecheckHandler
import com.pocs.presentation.view.component.appbar.EditContentAppBar
import com.pocs.presentation.view.component.textfield.PasswordOutlineTextField
import com.pocs.presentation.view.component.textfield.PocsOutlineTextField
import kotlinx.coroutines.launch

@Composable
fun UserEditScreen(
    viewModel: UserEditViewModel,
    navigateUp: () -> Unit,
    onSuccessToSave: () -> Unit
) {
    UserEditContent(
        uiState = viewModel.uiState.value,
        navigateUp = navigateUp,
        onSuccessToSave = onSuccessToSave
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEditContent(uiState: UserEditUiState, navigateUp: () -> Unit, onSuccessToSave: () -> Unit) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var showProfileDialog by remember { mutableStateOf(false) }
    val failedToUpdateString = stringResource(R.string.failed_to_update)
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
            uiState.update { it.copy(newProfileImage = bitmap) }
        }
    }

    RecheckHandler(navigateUp = navigateUp)

    if (showProfileDialog) {
        UserProfileDialog(
            onDismissRequest = { showProfileDialog = false },
            onChooseFromGalleryClick = {
                imagePickerLauncher.launch("image/*")
                showProfileDialog = false
            },
            onRemoveImageClick = {
                uiState.update { it.copy(profileImageUrl = null, newProfileImage = null) }
                showProfileDialog = false
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            EditContentAppBar(
                title = stringResource(id = R.string.edit_my_info),
                onBackPressed = { onBackPressedDispatcher?.onBackPressed() },
                isInSaving = uiState.isInSaving,
                enableSendIcon = uiState.canSave,
                onClickSend = {
                    if (!uiState.isInSaving) {
                        coroutineScope.launch {
                            val result = uiState.onSave()
                            if (result.isSuccess) {
                                onSuccessToSave()
                                navigateUp()
                            } else {
                                val exception = result.exceptionOrNull()!!
                                snackBarHostState.showSnackbar(
                                    exception.message ?: failedToUpdateString
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()

        Column(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
                .fillMaxWidth()
        ) {
            UserEditAvatar(
                imageModel = uiState.newProfileImage ?: uiState.profileImageUrl,
                onClick = {
                    showProfileDialog = true
                }
            )
            PocsOutlineTextField(
                value = uiState.name,
                label = if (uiState.canSaveName) {
                    stringResource(R.string.name)
                } else {
                    stringResource(R.string.name_must_be_needed)
                },
                modifier = Modifier.semantics { contentDescription = "이름 입력창" },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                isError = !uiState.canSaveName,
                maxLength = MAX_USER_NAME_LEN,
                onValueChange = { name ->
                    uiState.update { it.copy(name = name) }
                },
                onClearClick = {
                    uiState.update { it.copy(name = "") }
                }
            )
            PasswordOutlineTextField(
                password = uiState.password,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                onPasswordChange = { password ->
                    uiState.update { it.copy(password = password) }
                },
                onClearClick = {
                    uiState.update { it.copy(password = "") }
                }
            )
            PocsOutlineTextField(
                value = uiState.email,
                label = if (uiState.canSaveEmail) {
                    stringResource(R.string.email)
                } else if (uiState.email.isEmpty()) {
                    stringResource(R.string.email_must_be_needed)
                } else {
                    stringResource(R.string.email_is_not_valid)
                },
                isError = !uiState.canSaveEmail,
                maxLength = MAX_USER_EMAIL_LEN,
                placeholder = stringResource(R.string.email_placeholder),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                onValueChange = { email ->
                    uiState.update { it.copy(email = email) }
                },
                onClearClick = {
                    uiState.update { it.copy(email = "") }
                }
            )
            PocsOutlineTextField(
                value = uiState.company ?: "",
                label = stringResource(R.string.company),
                maxLength = MAX_USER_COMPANY_LEN,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                onValueChange = { company ->
                    uiState.update { it.copy(company = company) }
                },
                onClearClick = {
                    uiState.update { it.copy(company = "") }
                }
            )
            PocsOutlineTextField(
                value = uiState.github ?: "",
                label = stringResource(if (uiState.canSaveGithubUrl) R.string.github else R.string.github_url_is_not_valid),
                isError = !uiState.canSaveGithubUrl,
                maxLength = MAX_USER_GITHUB_LEN,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Next
                ),
                placeholder = stringResource(R.string.github_placeholder),
                onValueChange = { github ->
                    uiState.update { it.copy(github = github) }
                },
                onClearClick = {
                    uiState.update { it.copy(github = "") }
                }
            )
            Box(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun UserProfileDialog(
    onDismissRequest: () -> Unit,
    onChooseFromGalleryClick: () -> Unit,
    onRemoveImageClick: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 24.dp, bottom = 12.dp)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = stringResource(R.string.profile_image),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(12.dp))
                UserProfileDialogMenuItem(
                    text = stringResource(R.string.choose_from_gallery),
                    onClick = onChooseFromGalleryClick
                )
                UserProfileDialogMenuItem(
                    text = stringResource(R.string.remove_profile_image),
                    onClick = onRemoveImageClick
                )
            }
        }
    }
}

@Composable
private fun UserProfileDialogMenuItem(text: String, onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(0.8f)
            )
        )
    }
}

@Composable
fun UserEditAvatar(imageModel: Any?, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Box {
            UserProfileImage(imageModel = imageModel, onClick = onClick)
            Icon(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .size(24.dp),
                imageVector = Icons.Default.PhotoCamera,
                contentDescription = stringResource(id = R.string.edit_user_image)
            )
        }
    }
}

@Preview
@Composable
fun UserEditContentPreview() {
    UserEditContent(
        uiState = UserEditUiState(
            id = 1,
            password = "password",
            name = "박민석",
            email = "hello@gmiad.com",
            profileImageUrl = null,
            company = "google",
            github = "https://github.com/",
            isInSaving = false,
            onUpdate = {},
            onSave = { Result.success(Unit) }
        ),
        navigateUp = {}
    ) {}
}
