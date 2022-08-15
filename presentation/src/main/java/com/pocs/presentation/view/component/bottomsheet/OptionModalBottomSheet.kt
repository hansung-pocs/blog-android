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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.pocs.presentation.model.comment.item.CommentItemUiState
import kotlinx.coroutines.launch
import java.io.Serializable

@Stable
data class Option(
    val imageVector: ImageVector,
    @StringRes val stringResId: Int,
    val onClick: (CommentItemUiState) -> Unit
) : Serializable

@Composable
private fun OptionBottomSheet(options: List<Option>, controller: OptionModalController) {
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
    options: List<Option>,
    content: @Composable (OptionModalController) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val controller = remember { OptionModalController(bottomSheetState) }
    val coroutineScope = rememberCoroutineScope()

    BackHandler(bottomSheetState.isVisible) {
        coroutineScope.launch {
            controller.hide()
        }
    }

    PocsModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetContent = { OptionBottomSheet(options, controller) },
    ) {
        content(controller)
    }
}

@OptIn(ExperimentalMaterialApi::class)
class OptionModalController(
    private val modalBottomSheetState: ModalBottomSheetState
) {
    private var _comment: CommentItemUiState? = null
    val comment get() = _comment

    suspend fun show(comment: CommentItemUiState) {
        _comment = comment
        modalBottomSheetState.show()
    }

    suspend fun hide() {
        _comment = null
        modalBottomSheetState.hide()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
private fun OptionBottomSheetItemPreview() {
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    OptionBottomSheetItem(
        Option(imageVector = Icons.Default.Edit, stringResId = R.string.edit, onClick = {}),
        controller = OptionModalController(bottomSheetState)
    )
}