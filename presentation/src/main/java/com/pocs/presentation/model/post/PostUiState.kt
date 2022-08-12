package com.pocs.presentation.model.post

import androidx.paging.PagingData
import com.pocs.presentation.model.post.item.PostItemUiState

data class PostUiState(
    val pagingData : PagingData<PostItemUiState> = PagingData.empty(),
    val visiblePostWriteFab: Boolean,
    val selectedChip: ChipCategory = ChipCategory.ALL
) {
    enum class ChipCategory(val korean: String) {
        ALL("전체"),
        HOT("인기"),
        NOTICE("공지"),
        MEMORY("추억"),
        KNOWHOW("노하우"),
        REFERENCE("레퍼런스")
    }
}