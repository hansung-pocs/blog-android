package com.pocs.presentation.view.component.bottomsheet

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.pocs.presentation.model.comment.item.CommentItemUiState
import com.pocs.presentation.view.component.RecheckDialog
import com.pocs.presentation.view.component.textfield.SimpleTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

typealias CommentCreateCallback = (parentId: Int?, content: String) -> Unit

typealias CommentUpdateCallback = (id: Int, content: String) -> Unit

@Composable
private fun CommentTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    hint: String,
    onSend: (String) -> Unit,
    focusRequester: FocusRequester,
    showSendIcon: Boolean
) {
    SimpleTextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        hint = hint,
        value = value,
        onValueChange = onValueChange,
        maxLength = 250,// TODO: 벡엔드에서 댓글 최대 길이 결정되면 수정하기
        trailingIcon = {
            if (showSendIcon) {
                IconButton(onClick = { onSend(value.text) }) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = stringResource(R.string.send_comment),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun CommentModalBottomSheet(
    onCreated: CommentCreateCallback,
    onUpdated: CommentUpdateCallback,
    content: @Composable (CommentModalController) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        animationSpec = TweenSpec(durationMillis = 75, easing = FastOutSlowInEasing)
    )
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val controller = remember { CommentModalController(bottomSheetState) }
    val coroutineScope = rememberCoroutineScope()
    val textFieldValue = controller.textFieldValue
    var showRecheckDialog by remember { mutableStateOf(false) }

    var didSend by remember { mutableStateOf(false) }
    val canSend by rememberUpdatedState(
        if (controller.isUpdate) {
            textFieldValue.text != controller.commentToBeUpdated!!.content
        } else {
            textFieldValue.text.isNotEmpty()
        }
    )

    if (showRecheckDialog) {
        RecheckDialog(
            title = stringResource(R.string.stop_writing),
            confirmText = stringResource(R.string.stop),
            onOkClick = {
                showRecheckDialog = false
                controller.clear()
            },
            dismissText = stringResource(R.string.keep_writing),
            onDismissRequest = {
                showRecheckDialog = false
                coroutineScope.launch {
                    bottomSheetState.show()
                }
            }
        )
    }

    BackHandler(bottomSheetState.isVisible) {
        coroutineScope.launch {
            bottomSheetState.hide()
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { bottomSheetState.currentValue }
            .collectLatest {
                when (it) {
                    ModalBottomSheetValue.Expanded -> {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }
                    ModalBottomSheetValue.Hidden -> {
                        if (!didSend && canSend) {
                            showRecheckDialog = true
                        } else {
                            controller.clear()
                        }

                        focusRequester.freeFocus()
                        keyboardController?.hide()

                        didSend = false
                    }
                    else -> {}
                }
            }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            CommentTextField(
                modifier = Modifier.heightIn(max = 260.dp),
                value = textFieldValue,
                onValueChange = { controller.textFieldValue = it },
                focusRequester = focusRequester,
                showSendIcon = canSend,
                onSend = {
                    didSend = true

                    val commentToBeUpdated = controller.commentToBeUpdated

                    if (commentToBeUpdated != null) {
                        onUpdated(commentToBeUpdated.id, it)
                    } else {
                        onCreated(controller.parentId, it)
                    }

                    coroutineScope.launch {
                        controller.hide()
                    }
                },
                hint = stringResource(
                    id = if (controller.isReply) {
                        R.string.add_reply
                    } else {
                        R.string.add_comment
                    }
                )
            )
        },
    ) {
        content(controller)
    }
}

@OptIn(ExperimentalMaterialApi::class)
class CommentModalController(
    private val modalBottomSheetState: ModalBottomSheetState,
) {

    var textFieldValue: TextFieldValue by mutableStateOf(TextFieldValue())

    var parentId: Int? = null
        private set

    var commentToBeUpdated: CommentItemUiState? = null
        private set

    val isReply: Boolean get() = parentId != null

    val isUpdate: Boolean get() = commentToBeUpdated != null

    suspend fun showForCreate(parentId: Int? = null) {
        this.parentId = parentId
        show()
    }

    suspend fun showForUpdate(comment: CommentItemUiState) {
        this.commentToBeUpdated = comment

        textFieldValue = textFieldValue.copy(
            text = comment.content,
            selection = TextRange(comment.content.length)
        )
        show()
    }

    private suspend fun show() {
        modalBottomSheetState.show()
    }

    suspend fun hide() {
        clear()
        modalBottomSheetState.hide()
    }

    fun clear() {
        textFieldValue = textFieldValue.copy(text = "")
        parentId = null
        commentToBeUpdated = null
    }
}
