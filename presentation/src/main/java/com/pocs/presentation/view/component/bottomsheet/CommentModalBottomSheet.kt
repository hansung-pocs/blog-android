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
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.pocs.presentation.view.component.textfield.SimpleTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.math.abs

typealias CommentSendCallback = (parentId: Int?, content: String) -> Unit

@Composable
private fun CommentTextField(
    modifier: Modifier = Modifier,
    comment: String,
    onCommentChange: (String) -> Unit,
    hint: String,
    onSend: (String) -> Unit,
    focusRequester: FocusRequester
) {
    SimpleTextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        hint = hint,
        value = comment,
        onValueChange = onCommentChange,
        maxLength = 250,// TODO: 벡엔드에서 댓글 최대 길이 결정되면 수정하기
        trailingIcon = {
            if (comment.isNotEmpty()) {
                IconButton(onClick = { onSend(comment) }) {
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
    onSend: CommentSendCallback,
    content: @Composable (CommentModalController) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val commentModalController = remember {
        CommentModalController(
            bottomSheetState,
            keyboardController,
            focusRequester
        )
    }
    val coroutineScope = rememberCoroutineScope()
    var comment by remember { mutableStateOf("") }

    BackHandler(bottomSheetState.isVisible) {
        coroutineScope.launch {
            bottomSheetState.hide()
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { bottomSheetState.currentValue }
            .filter { it == ModalBottomSheetValue.Hidden }
            .collectLatest {
                keyboardController?.hide()
                comment = ""
            }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            CommentTextField(
                modifier = Modifier
                    .heightIn(max = 260.dp)
                    .verticalScrollDisabled(),
                comment = comment,
                onCommentChange = { comment = it },
                focusRequester = focusRequester,
                onSend = {
                    onSend(commentModalController.parentId, it)
                    coroutineScope.launch {
                        commentModalController.hide()
                    }
                },
                hint = stringResource(
                    id = if (commentModalController.isReply) {
                        R.string.add_reply
                    } else {
                        R.string.add_comment
                    }
                )
            )
        },
    ) {
        content(commentModalController)
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
class CommentModalController(
    private val modalBottomSheetState: ModalBottomSheetState,
    private val keyboardController: SoftwareKeyboardController?,
    private val focusRequester: FocusRequester
) {

    private var _parentId: Int? = null
    val parentId: Int? get() = _parentId

    val isReply: Boolean get() = parentId != null

    suspend fun show(parentId: Int? = null) {
        _parentId = parentId
        focusRequester.requestFocus()
        modalBottomSheetState.snapTo(ModalBottomSheetValue.Expanded)
        keyboardController?.show()
    }

    suspend fun hide() {
        _parentId = null
        focusRequester.freeFocus()
        modalBottomSheetState.snapTo(ModalBottomSheetValue.Hidden)
        keyboardController?.hide()
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