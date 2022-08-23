package com.pocs.test_library.mock

import com.pocs.data.model.user.UserDefaultInfoDto
import com.pocs.data.model.user.UserDto
import com.pocs.domain.model.post.PostWriter
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDefaultInfo
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserType

val mockPostWriter1 = PostWriter(1, "kim", "jja0i213@naver.com", UserType.MEMBER)

val mockNormalUser = User(
    id = 1,
    defaultInfo = UserDefaultInfo(
        name = "권김정",
        email = "abc@google.com",
        studentId = 1971034,
        company = null,
        generation = 30,
        github = "https://github.com/"
    ),
    canceledAt = null
)
val mockKickedUser = User(
    id = 1,
    defaultInfo = UserDefaultInfo(
        name = "권김정",
        email = "abc@google.com",
        studentId = 1971034,
        company = null,
        generation = 30,
        github = "https://github.com/"
    ),
    canceledAt = "2021-02-12"
)

val mockAdminUserDetail = UserDetail(
    id = 2,
    defaultInfo = UserDefaultInfo(
        name = "권김정",
        email = "abc@google.com",
        studentId = 1971034,
        company = null,
        generation = 30,
        github = "https://github.com/"
    ),
    type = UserType.ADMIN,
    createdAt = "2021-02-12",
    canceledAt = null
)

val mockMemberUserDetail = UserDetail(
    id = 2,
    defaultInfo = UserDefaultInfo(
        name = "권김정",
        email = "abc@google.com",
        studentId = 1971034,
        company = null,
        generation = 30,
        github = "https://github.com/"
    ),
    type = UserType.MEMBER,
    createdAt = "2021-02-12",
    canceledAt = null
)

val mockKickedUserDetail = UserDetail(
    id = 2,
    defaultInfo = UserDefaultInfo(
        name = "권김정",
        email = "abc@google.com",
        studentId = 1971034,
        company = null,
        generation = 30,
        github = "https://github.com/"
    ),
    type = UserType.ADMIN,
    createdAt = "2021-02-12",
    canceledAt = "2021-02-12"
)

val mockUserDto = UserDto(
    id = 2,
    defaultInfo = UserDefaultInfoDto(
        name = "권김정",
        email = "abc@google.com",
        studentId = 1971034,
        company = null,
        generation = 30,
        github = "https://github.com/"
    ),
    type = UserType.ADMIN.toString().lowercase(),
    createdAt = "2021-02-12",
    canceledAt = null
)