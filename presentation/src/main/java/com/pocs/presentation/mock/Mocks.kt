package com.pocs.presentation.mock

import androidx.paging.PagingData
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.model.post.item.PostItemUiState
import com.pocs.presentation.model.user.item.UserItemUiState

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
        UserItemUiState(1, "오인성", "1891065", 2),
        UserItemUiState(2, "김민성", "1871034", 3),
        UserItemUiState(3, "가가가", "1991066", 5),
        UserItemUiState(4, "나나나", "1891067", 6),
        UserItemUiState(5, "다다다", "1891068", 1),
        UserItemUiState(6, "마마마", "1891069", 3),
        UserItemUiState(7, "바바바", "1691010", 8),
        UserItemUiState(8, "사사사", "1791011", 9),
        UserItemUiState(9, "아아아", "1891012", 10),
    )
)

val mockUserDetails = listOf(
    UserDetail(1, "오인성", "1891065", 2, UserType.MEMBER, "google", 30, "https://github/jja08111", ""),
    UserDetail(2, "김민성", "1871034", 3, UserType.MEMBER, "google", 30, "https://github/jja08111", ""),
    UserDetail(3, "가가가", "jja08111@gmail.com", 5, UserType.MEMBER, "google", 30, "https://github/jja08111", ""),
    UserDetail(4, "나나나", "1891067", 6, UserType.MEMBER, "google", 30, "https://github/jja08111", ""),
    UserDetail(5, "다다다", "1891068", 1, UserType.MEMBER, "google", 30, "https://github/jja08111", ""),
    UserDetail(6, "마마마", "1891069", 3, UserType.MEMBER, "google", 30, "https://github/jja08111", ""),
    UserDetail(7, "바바바", "1691010", 8, UserType.MEMBER, "google", 30, "https://github/jja08111", ""),
    UserDetail(8, "사사사", "1791011", 9, UserType.MEMBER, "google", 30, "https://github/jja08111", ""),
    UserDetail(9, "아아아", "1891012", 10, UserType.MEMBER, "google", 30, "https://github/jja08111", ""),
)