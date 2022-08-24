package com.pocs.presentation.view.component.bottomsheet

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.pocs.presentation.model.comment.item.CommentItemUiState
import kotlinx.coroutines.launch

@Immutable
data class Option(
    val imageVector: ImageVector,
    @StringRes val stringResId: Int,
    val onClick: (CommentItemUiState) -> Unit
)

@Composable
private fun OptionBottomSheet(controller: OptionModalController) {
    val options = controller.options

    if (options == null || options.isEmpty()) {
        // ModalBottomSheet의 Anchor는 조금이라도 높이가 존재해야지만 작동하기 때문에 의미없는 작은 높이 값을 넣어준다.
        Box(Modifier.height(1.dp))
        return
    }

    Column {
        for (option in options) {
            OptionBottomSheetItem(option, controller)
        }
    }
}

@Composable
fun OptionBottomSheetItem(option: Option, controller: OptionModalController) {
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val label = stringResource(id = option.stringResId)
    val color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = true),
                role = Role.Button,
                onClick = {
                    option.onClick(requireNotNull(controller.comment))
                    coroutineScope.launch {
                        controller.hide()
                    }
                }
            )
            .padding(16.dp),
    ) {
        Icon(
            imageVector = option.imageVector,
            contentDescription = label,
            tint = color
        )
        Box(modifier = Modifier.width(24.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(color = color)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OptionModalBottomSheet(
    optionBuilder: (CommentItemUiState?) -> List<Option>,
    controller: OptionModalController,
    content: @Composable () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(controller) {
        controller.init(bottomSheetState, optionBuilder)
    }

    BackHandler(bottomSheetState.isVisible) {
        coroutineScope.launch {
            controller.hide()
        }
    }

    PocsModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetContent = { OptionBottomSheet(controller) },
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterialApi::class)
class OptionModalController {

    private lateinit var modalBottomSheetState: ModalBottomSheetState
    private lateinit var optionBuilder: (CommentItemUiState?) -> List<Option>

    private var inited: Boolean = false

    private var _comment: CommentItemUiState? = null
    val comment get() = _comment

    var options by mutableStateOf<List<Option>?>(null)
        private set

    fun init(
        modalBottomSheetState: ModalBottomSheetState,
        optionBuilder: (CommentItemUiState?) -> List<Option>
    ) {
        check(!inited) { "이미 초기화되었습니다." }
        inited = true
        this.modalBottomSheetState = modalBottomSheetState
        this.optionBuilder = optionBuilder
    }

    suspend fun show(comment: CommentItemUiState) {
        options = optionBuilder(comment)
        _comment = comment
        modalBottomSheetState.show()
    }

    suspend fun hide() {
        modalBottomSheetState.hide()
        options = null
        _comment = null
    }
}

@Preview(showBackground = true)
@Composable
private fun OptionBottomSheetItemPreview() {
    OptionBottomSheetItem(
        Option(imageVector = Icons.Default.Edit, stringResId = R.string.edit, onClick = {}),
        controller = OptionModalController()
    )
}