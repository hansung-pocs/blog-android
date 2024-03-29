package com.pocs.presentation.view.component.bottomsheet

import androidx.activity.compose.BackHandler
import androidx.annotation.VisibleForTesting
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.pocs.presentation.model.comment.item.CommentItemUiState
import com.pocs.presentation.view.component.RecheckDialog
import com.pocs.presentation.view.component.textfield.SimpleTextField
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
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
    val commentTextFieldDescription = stringResource(R.string.comment_text_field)

    SimpleTextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .semantics {
                contentDescription = commentTextFieldDescription
            },
        hint = hint,
        value = value,
        onValueChange = onValueChange,
        maxLength = 250, // TODO: 벡엔드에서 댓글 최대 길이 결정되면 수정하기
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
    controller: CommentModalController,
    onCreated: CommentCreateCallback,
    onUpdated: CommentUpdateCallback,
    content: @Composable () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        animationSpec = TweenSpec(durationMillis = 0)
    )
    val textFieldValueState = remember { mutableStateOf(TextFieldValue()) }

    var showRecheckDialog by remember { mutableStateOf(false) }

    val isNotEmpty = textFieldValueState.value.text.isNotEmpty()
    val canSend by rememberUpdatedState(
        if (controller.isUpdate) {
            isNotEmpty && textFieldValueState.value.text != controller.commentToBeUpdated!!.content
        } else {
            isNotEmpty
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
            if (canSend) {
                showRecheckDialog = true
            } else {
                controller.clear()
            }
        }
    }

    LaunchedEffect(controller) {
        controller.init(
            textFieldValueState = textFieldValueState,
            bottomSheetState = bottomSheetState
        )
    }

    LaunchedEffect(Unit) {
        snapshotFlow { bottomSheetState.currentValue }
            .filter { it == ModalBottomSheetValue.Expanded }
            .collectLatest {
                focusRequester.requestFocus()
                delay(50)
                keyboardController?.show()
            }
    }

    PocsModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        onDismiss = {
            keyboardController?.hide()
            if (canSend) {
                showRecheckDialog = true
            } else {
                controller.clear()
            }
        },
        sheetContent = {
            CommentTextField(
                modifier = Modifier.heightIn(max = 260.dp),
                value = textFieldValueState.value,
                onValueChange = { textFieldValueState.value = it },
                focusRequester = focusRequester,
                showSendIcon = canSend,
                onSend = {
                    val commentToBeUpdated = controller.commentToBeUpdated

                    if (commentToBeUpdated != null) {
                        onUpdated(commentToBeUpdated.id, it)
                    } else {
                        onCreated(controller.parentId, it)
                    }

                    coroutineScope.launch {
                        // 키보드를 숨기는 함수 호출 때문인지 가끔씩 입력한 텍스트 필드 그대로 값이 남아있는 버그가 존재한다.
                        // 따라서 아래의 키보드를 숨기는 함수가 suspend 함수가 아니어도 이 코루틴 스코프 내에 위치해야 한다.
                        // https://github.com/hansung-pocs/blog-android/issues/177
                        keyboardController?.hide()
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
        content()
    }
}

@OptIn(ExperimentalMaterialApi::class)
class CommentModalController {

    private lateinit var bottomSheetState: ModalBottomSheetState
    private lateinit var textFieldValueState: MutableState<TextFieldValue>

    private var inited: Boolean = false

    var parentId: Int? = null
        private set

    var commentToBeUpdated: CommentItemUiState? = null
        private set

    val isReply: Boolean get() = parentId != null

    val isUpdate: Boolean get() = commentToBeUpdated != null

    fun init(
        textFieldValueState: MutableState<TextFieldValue>,
        bottomSheetState: ModalBottomSheetState
    ) {
        check(!inited) { "이미 초기화되었습니다." }
        inited = true
        this.textFieldValueState = textFieldValueState
        this.bottomSheetState = bottomSheetState
    }

    suspend fun showForCreate(parentComment: CommentItemUiState? = null) {
        if (parentComment != null) {
            parentId = parentComment.parentId
        }
        show()
    }

    suspend fun showForUpdate(comment: CommentItemUiState) {
        this.commentToBeUpdated = comment

        textFieldValueState.value = TextFieldValue(
            text = comment.content,
            selection = TextRange(comment.content.length)
        )
        show()
    }

    private suspend fun show() {
        bottomSheetState.show()
    }

    suspend fun hide() {
        clear()
        bottomSheetState.hide()
    }

    fun clear() {
        textFieldValueState.value = TextFieldValue(text = "")
        parentId = null
        commentToBeUpdated = null
    }

    @VisibleForTesting
    val isCleared: Boolean
        get() = textFieldValueState.value.text.isEmpty() &&
            parentId == null &&
            commentToBeUpdated == null
}
