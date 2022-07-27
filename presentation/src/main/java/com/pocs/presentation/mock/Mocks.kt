package com.pocs.presentation.mock

import androidx.paging.PagingData
import com.pocs.domain.model.PostCategory
import com.pocs.presentation.model.PostDetailUiState
import com.pocs.presentation.model.PostItemUiState
import com.pocs.presentation.model.UserItemUiState

val mockNoticeItemsPagingData = PagingData.from(
    listOf(
        PostItemUiState(6, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(7, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(8, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(9, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(10, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(11, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(12, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(13, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(14, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(15, "가나다라마", "관리자", "2022.07.19"),
    )
)

val mockUsersPagingData = PagingData.from(
    listOf(
        UserItemUiState("오인성", "1891065", 2),
        UserItemUiState("김민성", "1871034", 3),
        UserItemUiState("가가가", "1991066", 5),
        UserItemUiState("나나나", "1891067", 6),
        UserItemUiState("다다다", "1891068", 1),
        UserItemUiState("마마마", "1891069", 3),
        UserItemUiState("바바바", "1691010", 8),
        UserItemUiState("사사사", "1791011", 9),
        UserItemUiState("아아아", "1891012", 10),
    )
)