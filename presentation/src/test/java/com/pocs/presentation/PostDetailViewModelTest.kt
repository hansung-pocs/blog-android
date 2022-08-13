package com.pocs.presentation

import com.pocs.domain.usecase.post.CanDeletePostUseCase
import com.pocs.domain.usecase.post.CanEditPostUseCase
import com.pocs.domain.usecase.post.DeletePostUseCase
import com.pocs.domain.usecase.post.GetPostDetailUseCase
import com.pocs.presentation.model.post.PostDetailUiState
import com.pocs.presentation.view.post.detail.PostDetailViewModel
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakePostRepositoryImpl
import com.pocs.test_library.mock.mockNormalUserDetail
import com.pocs.test_library.mock.mockPostDetail1
import com.pocs.test_library.rule.JodaRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostDetailViewModelTest {

    @get:Rule
    val jodaRule = JodaRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val authRepository = FakeAuthRepositoryImpl()
    private val postRepository = FakePostRepositoryImpl()

    private val viewModel = PostDetailViewModel(
        GetPostDetailUseCase(postRepository),
        DeletePostUseCase(postRepository, authRepository),
        CanEditPostUseCase(authRepository),
        CanDeletePostUseCase(authRepository)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun uiStateIsSuccess_WhenLoadingResultIsSuccess() = runTest {
        postRepository.postDetailResult = Result.success(mockPostDetail1)

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }

        viewModel.fetchPost(mockPostDetail1.id)

        assertTrue(viewModel.uiState.value is PostDetailUiState.Success)

        collectJob.cancel()
    }

    @Test
    fun uiStateIsFailure_WhenLoadingResultIsFailure() = runTest {
        val exception = Exception("실패 메시지")
        postRepository.postDetailResult = Result.failure(exception)

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }

        viewModel.fetchPost(mockPostDetail1.id)

        assertTrue(viewModel.uiState.value is PostDetailUiState.Failure)
        assertEquals(
            exception.message!!,
            (viewModel.uiState.value as PostDetailUiState.Failure).message!!
        )

        collectJob.cancel()
    }

    @Test
    fun shouldSetErrorMessage_WhenFailedToDeletePost() {
        val exception = Exception("ERROR")
        postRepository.postDetailResult = Result.success(mockPostDetail1)
        postRepository.deletePostResult = Result.failure(exception)
        authRepository.currentUser.value = mockNormalUserDetail.copy(id = mockPostDetail1.writer.id)
        viewModel.fetchPost(mockPostDetail1.id)

        viewModel.requestPostDeleting(1)

        assertEquals(
            exception.message,
            (viewModel.uiState.value as PostDetailUiState.Success).errorMessage
        )
    }

    @Test
    fun shouldIsSuccessToDeleteValueIsTrue_WhenSuccessToDeletePost() {
        postRepository.postDetailResult = Result.success(mockPostDetail1)
        postRepository.deletePostResult = Result.success(Unit)
        authRepository.currentUser.value = mockNormalUserDetail.copy(id = mockPostDetail1.writer.id)
        viewModel.fetchPost(mockPostDetail1.id)

        viewModel.requestPostDeleting(1)

        assertEquals(true, (viewModel.uiState.value as PostDetailUiState.Success).isSuccessToDelete)
    }
}