package com.pocs.presentation.mock

import androidx.paging.PagingData
import com.pocs.presentation.view.home.article.ArticleItemUiState

val mockArticleItemsPagingData = PagingData.from(
    listOf(
        ArticleItemUiState("가나다라마", "작성자", "2022.07.19"),
        ArticleItemUiState("가나다라마", "작성자", "2022.07.19"),
        ArticleItemUiState("가나다라마", "작성자", "2022.07.19"),
        ArticleItemUiState("가나다라마", "작성자", "2022.07.19"),
        ArticleItemUiState("가나다라마", "작성자", "2022.07.19"),
    )
)