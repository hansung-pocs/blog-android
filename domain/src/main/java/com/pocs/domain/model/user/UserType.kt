package com.pocs.domain.model.user

enum class UserType(val koreanString: String) {
    ADMIN("관리자"),
    MEMBER("회원"),
    UNKNOWN("비회원")
}