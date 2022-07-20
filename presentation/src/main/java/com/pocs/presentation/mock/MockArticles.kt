package com.pocs.presentation.mock

import androidx.paging.PagingData
import com.pocs.presentation.model.PostItemUiState

val mockArticleItemsPagingData = PagingData.from(
    listOf(
        PostItemUiState("가나다라마", "작성자", "2022.07.19"),
        PostItemUiState("가나다라마", "작성자", "2022.07.19"),
        PostItemUiState("가나다라마", "작성자", "2022.07.19"),
        PostItemUiState("가나다라마", "작성자", "2022.07.19"),
        PostItemUiState("가나다라마", "작성자", "2022.07.19"),
    )
)