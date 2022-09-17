package com.pocs.presentation.view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun UserProfileImage(
    imageModel: Any?,
    size: Dp = 120.dp,
    onClick: (() -> Unit)? = null,
) {
    val contentDescription = stringResource(id = R.string.user_image)
    var modifier = Modifier.size(size)
    if (onClick != null) {
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    }
    modifier = modifier.clip(CircleShape)

    if (imageModel != null) {
        GlideImage(
            modifier = modifier,
            imageModel = imageModel,
            failure = { FailureImage() },
            contentDescription = contentDescription
        )
    } else {
        Icon(
            modifier = modifier,
            imageVector = Icons.Filled.AccountCircle,
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = contentDescription
        )
    }
}
