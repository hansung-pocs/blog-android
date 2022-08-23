package com.pocs.presentation.model.post

import androidx.paging.PagingData
import com.pocs.domain.model.post.PostFilterType
import com.pocs.presentation.model.post.item.PostItemUiState

data class PostUiState(
    val pagingData: PagingData<PostItemUiState> = PagingData.empty(),
    val isUserAnonymous: Boolean,
    val selectedPostFilterType: PostFilterType = PostFilterType.ALL
) {
    val visiblePostCategory: Boolean
        get() {
            return selectedPostFilterType == PostFilterType.ALL
                    || selectedPostFilterType == PostFilterType.BEST
        }
}