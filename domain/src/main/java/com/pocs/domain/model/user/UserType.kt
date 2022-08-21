package com.pocs.domain.model.user

enum class UserType(val koreanString: String) {
    ADMIN("관리자"),
    MEMBER("회원"),
    비회원("비회원")
    // TODO : 백엔드에서 바꿔주면 바꾸기
}