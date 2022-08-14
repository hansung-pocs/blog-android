package com.pocs.presentation.view.component.bottomsheet

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.pocs.presentation.model.comment.item.CommentItemUiState
import com.pocs.presentation.view.component.textfield.SimpleTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

typealias CommentCreateCallback = (parentId: Int?, content: String) -> Unit

typealias CommentUpdateCallback = (id: Int, content: String) -> Unit

@Composable
private fun CommentTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    hint: String,
    onSend: (String) -> Unit,
    focusRequester: FocusRequester
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
            if (value.text.isNotEmpty()) {
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
        initialValue = ModalBottomSheetValue.Hidden
    )
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val controller = remember { CommentModalController(bottomSheetState) }
    val coroutineScope = rememberCoroutineScope()
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = "")) }

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
                        val commentToBeUpdated = controller.commentToBeUpdated
                        // 댓글을 업데이트하는 경우는 이전에 작성한 댓글의 내용을 텍스트 필드에 넣는다.
                        if (commentToBeUpdated != null) {
                            val text = commentToBeUpdated.content
                            textFieldValue = textFieldValue.copy(
                                text = text,
                                selection = TextRange(text.length)
                            )
                        }

                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }
                    ModalBottomSheetValue.Hidden -> {
                        textFieldValue = textFieldValue.copy(text = "")
                        controller.clear()

                        focusRequester.freeFocus()
                        keyboardController?.hide()
                    }
                    else -> {}
                }
            }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            CommentTextField(
                modifier = Modifier
                    .heightIn(max = 260.dp)
                    .verticalScrollDisabled(),
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                focusRequester = focusRequester,
                onSend = {
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

    var parentId: Int? = null
        private set

    var commentToBeUpdated: CommentItemUiState? = null
        private set

    val isReply: Boolean get() = parentId != null

    suspend fun showForCreate(parentId: Int? = null) {
        this.parentId = parentId
        show()
    }

    suspend fun showForUpdate(comment: CommentItemUiState) {
        this.commentToBeUpdated = comment
        show()
    }

    private suspend fun show() {
        modalBottomSheetState.snapTo(ModalBottomSheetValue.Expanded)
    }

    suspend fun hide() {
        clear()
        modalBottomSheetState.snapTo(ModalBottomSheetValue.Hidden)
    }

    fun clear() {
        parentId = null
        commentToBeUpdated = null
    }
}

fun Modifier.verticalScrollDisabled() =
    pointerInput(Unit) {
        awaitPointerEventScope {
            // we should wait for all new pointer events
            while (true) {
                awaitPointerEvent(pass = PointerEventPass.Initial).changes.forEach {
                    val offset = it.positionChange()
                    if (abs(offset.y) > 0f) {
                        it.consume()
                    }
                }
            }
        }
    }