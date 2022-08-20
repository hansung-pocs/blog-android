package com.pocs.domain

import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.post.PostDetail
import com.pocs.domain.model.post.PostWriter
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.post.CanEditPostUseCase
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.mock.mockPostWriter1
import org.junit.Assert.*
import org.junit.Test

class CanEditPostUseCaseTest {

    private val authRepository = FakeAuthRepositoryImpl()

    private val useCase = CanEditPostUseCase(authRepository)

    private val postDetail = PostDetail(1, "", mockPostWriter1, 1, "", "", "", PostCategory.NOTICE)
    private val userDetail = UserDetail(
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

    @Test
    fun shouldReturnTrue_WhenSamePostWriterIdAndCurrentUserId() {
        authRepository.currentUser.value = userDetail

        val result = useCase(
            postDetail = postDetail.copy(
                writer = PostWriter(
                    id = userDetail.id,
                    name = userDetail.name,
                    email = userDetail.email,
                    type = userDetail.type
                )
            )
        )

        assertTrue(result)
    }

    @Test
    fun shouldReturnFalse_WhenDifferentPostWriterIdAndCurrentUserId() {
        authRepository.currentUser.value = userDetail

        val result = useCase(
            postDetail = postDetail.copy(
                writer = PostWriter(
                    id = 987654321,
                    name = "123",
                    email = "123",
                    type = UserType.ADMIN
                )
            )
        )

        assertFalse(result)
    }
}