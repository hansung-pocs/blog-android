package com.pocs.presentation.view.component.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxColors
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.pocs.presentation.R

@Composable
fun PocsCheckBox(
    modifier: Modifier,
    isMember: Boolean,
    enabled: Boolean = true
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
        Checkbox(
            checked = isMember,
            onCheckedChange = { !isMember },
            modifier = modifier,
            enabled = enabled,
            colors = CheckboxDefaults.colors(MaterialTheme.colorScheme.primary)
        )
        Text(
            text = stringResource(R.string.can_see_onlymember),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            ),
            lineHeight = 24.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
