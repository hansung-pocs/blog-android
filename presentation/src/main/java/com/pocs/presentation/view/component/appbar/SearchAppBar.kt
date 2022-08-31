package com.pocs.presentation.view.component.appbar

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.pocs.presentation.R
import com.pocs.presentation.view.component.button.AppBarBackButton
import com.pocs.presentation.view.component.button.ClearButton
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchAppBar(
    title: String,
    enabledSearchMode: Boolean,
    onSearchModeChange: (Boolean) -> Unit,
    onSearch: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var query by remember { mutableStateOf("") }

    LaunchedEffect(enabledSearchMode) {
        if (enabledSearchMode) {
            focusRequester.requestFocus()
            delay(100)
            keyboardController?.show()
        } else {
            query = ""
            keyboardController?.hide()
        }
    }

    BackHandler(enabledSearchMode) {
        onSearchModeChange(false)
    }

    SmallTopAppBar(
        title = {
            if (enabledSearchMode) {
                SearchTextField(
                    query = query,
                    focusRequester = focusRequester,
                    onQueryChange = { query = it },
                    onSearch = { query ->
                        if (query.length >= 2) {
                            keyboardController?.hide()
                        }
                        onSearch(query)
                    }
                )
            } else {
                Text(text = title)
            }
        },
        navigationIcon = {
            AppBarBackButton(onBackPressed = {
                if (enabledSearchMode) {
                    onSearchModeChange(false)
                } else {
                    onBackPressedDispatcher?.onBackPressed()
                }
            })
        },
        actions = {
            if (enabledSearchMode) {
                if (query.isNotEmpty()) {
                    ClearButton { query = "" }
                }
            } else {
                IconButton(onClick = {
                    onSearchModeChange(true)
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search_by_name)
                    )
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTextField(
    query: String,
    focusRequester: FocusRequester,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    val textStyle = MaterialTheme.typography.titleLarge

    BasicTextField(
        modifier = Modifier.focusRequester(focusRequester),
        textStyle = textStyle,
        value = query,
        onValueChange = {
            if (it.length >= 40) {
                return@BasicTextField
            }
            val value = it.filter { char -> char != '\n' }
            onQueryChange(value)
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
        decorationBox = { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = query,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = PaddingValues(),
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_by_name),
                        style = textStyle.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                        )
                    )
                }
            )
        }
    )
}

@Preview
@Composable
fun SearchAppBarPreview() {

}