package com.pocs.presentation

import com.pocs.domain.usecase.post.GetPostDetailUseCase
import com.pocs.presentation.fake.FakePostRepositoryImpl
import com.pocs.presentation.mock.mockPostDetail1
import com.pocs.presentation.model.PostDetailUiState
import com.pocs.presentation.view.post.detail.PostDetailViewModel
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
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostDetailViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    private val repository = FakePostRepositoryImpl()
    private val viewModel = PostDetailViewModel(GetPostDetailUseCase(repository))

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
        repository.postDetailResult = Result.success(mockPostDetail1)

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
        repository.postDetailResult = Result.failure(exception)

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
}