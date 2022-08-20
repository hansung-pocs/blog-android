package com.pocs.presentation.view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun Label(
    imageVector: ImageVector,
    label: String?,
    contentDescription: String,
    onClick: (() -> Unit)? = null
) {
    val color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
    val interactionSource = remember { MutableInteractionSource() }
    var modifier = Modifier.size(16.dp)
    if (onClick != null) {
        modifier = modifier.clickable(
            onClick = onClick,
            interactionSource = interactionSource,
            indication = rememberRipple(
                bounded = false,
                radius = 24.dp
            ),
            role = Role.Button,
        )
    }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 20.dp)
    ) {
        Icon(
            modifier = modifier,
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = color
        )
        Text(
            modifier = Modifier
                .padding(start = 24.dp)
                .defaultMinSize(minWidth = 32.dp),
            text = label ?: "",
            style = MaterialTheme.typography.bodySmall.copy(color = color)
        )
    }
}