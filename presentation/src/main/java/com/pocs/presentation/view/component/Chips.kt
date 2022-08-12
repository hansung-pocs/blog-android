@file:OptIn(ExperimentalMaterial3Api::class)

package com.pocs.presentation.view.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> HorizontalChips(
    items: List<T>,
    itemLabelBuilder: @Composable (T) -> String,
    selectedItem: T,
    onItemClick: (T) -> Unit,
) {
    val scrollState = rememberScrollState()

    Row(
        Modifier
            .horizontalScroll(scrollState)
            .padding(horizontal = 12.dp)
    ) {
        for (item in items) {
            val isSelected = item == selectedItem

            val colors = if (isSelected) selectedColors() else unselectedColors()
            val border = if (isSelected) selectedBorder() else unselectedBorder()

            AssistChip(
                modifier = Modifier.padding(horizontal = 4.dp),
                onClick = { onItemClick(item) },
                label = { Text(text = itemLabelBuilder(item)) },
                border = border,
                colors = colors
            )
        }
    }
}

@Composable
@Stable
private fun selectedColors(): ChipColors = AssistChipDefaults.elevatedAssistChipColors(
    containerColor = MaterialTheme.colorScheme.primaryContainer,
    labelColor = MaterialTheme.colorScheme.onPrimaryContainer
)

@Composable
@Stable
private fun unselectedColors(): ChipColors = AssistChipDefaults.elevatedAssistChipColors(
    containerColor = MaterialTheme.colorScheme.background,
    labelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
)

@Composable
@Stable
private fun selectedBorder(): ChipBorder = AssistChipDefaults.assistChipBorder(
    borderColor = MaterialTheme.colorScheme.primaryContainer
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Stable
private fun unselectedBorder(): ChipBorder = AssistChipDefaults.assistChipBorder(
    borderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f)
)
