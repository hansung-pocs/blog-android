package com.pocs.test_library.mock

import com.pocs.domain.model.post.PostWriter
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserType

val mockPostWriter1 = PostWriter(1, "kim", "jja0i213@naver.com", UserType.MEMBER)

val mockNormalUser = User(1, "권김정", 1971034, 30, null)
val mockKickedUser = User(1, "권김정", 1971034, 30, "2021-02-12")

val mockNormalUserDetail = UserDetail(
    2,
    "권김정",
    "abc@google.com",
    1971034,
    UserType.ADMIN,
    null,
    30,
    "https://github.com/",
    "2021-02-12",
    null,
)
val mockKickedUserDetail = UserDetail(
    2,
    "권김정",
    "abc@google.com",
    1971034,
    UserType.ADMIN,
    null,
    30,
    "https://github.com/",
    "2021-02-12",
    "2021-02-13",
)