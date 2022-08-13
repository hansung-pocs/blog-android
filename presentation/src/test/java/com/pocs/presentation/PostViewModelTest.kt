package com.pocs.presentation

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.pocs.domain.model.post.PostFilterType
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.post.GetAllPostsUseCase
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.auth.IsCurrentUserUnknownUseCase
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.view.home.post.PostViewModel
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakePostRepositoryImpl
import com.pocs.test_library.mock.mockNormalUserDetail
import com.pocs.test_library.mock.mockPost
import com.pocs.test_library.paging.PostItemUiStateDiffCallback
import com.pocs.test_library.paging.NoopListCallback
import com.pocs.test_library.rule.JodaRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest {

    @get:Rule
    val jodaRule = JodaRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val authRepository = FakeAuthRepositoryImpl()
    private val postRepository = FakePostRepositoryImpl()

    private lateinit var viewModel: PostViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldVisibleFab_WhenCurrentUserIsNotUnknown() {
        authRepository.currentUser.value = mockNormalUserDetail.copy(type = UserType.MEMBER)
        initViewModel()

        assertTrue(viewModel.uiState.value.visiblePostWriteFab)
    }

    @Test
    fun shouldDoNotVisibleFab_WhenCurrentUserIsUnknown() {
        authRepository.currentUser.value = mockNormalUserDetail.copy(type = UserType.UNKNOWN)
        initViewModel()

        assertFalse(viewModel.uiState.value.visiblePostWriteFab)
    }

    @Test
    fun shouldUpdateSelectedPostFilterType_WhenCallUpdateFunction() {
        initViewModel()

        viewModel.updatePostFilterType(PostFilterType.KNOWHOW)

        assertEquals(PostFilterType.KNOWHOW, viewModel.uiState.value.selectedPostFilterType)

        viewModel.updatePostFilterType(PostFilterType.NOTICE)

        assertEquals(PostFilterType.NOTICE, viewModel.uiState.value.selectedPostFilterType)
    }

    @Test
    fun shouldFetchPosts_WhenUpdatePostFilterType() = runTest {
        // given
        val differ = AsyncPagingDataDiffer(
            diffCallback = PostItemUiStateDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )
        val list = listOf(mockPost)
        initViewModel()
        postRepository.postPagingDataFlow.emit(PagingData.from(list))
        differ.submitData(viewModel.uiState.value.pagingData)
        advanceUntilIdle()
        assertEquals(list.map { it.toUiState() }, differ.snapshot().items)

        // when
        postRepository.postPagingDataFlow = MutableSharedFlow()
        viewModel.updatePostFilterType(PostFilterType.KNOWHOW)
        postRepository.postPagingDataFlow.emit(PagingData.empty())

        // then
        differ.submitData(viewModel.uiState.value.pagingData)
        advanceUntilIdle()
        assertNotEquals(list.map { it.toUiState() }, differ.snapshot().items)
    }

    private fun initViewModel() {
        viewModel = PostViewModel(
            getAllPostsUseCase = GetAllPostsUseCase(postRepository),
            isCurrentUserUnknownUseCase = IsCurrentUserUnknownUseCase(
                GetCurrentUserTypeUseCase(authRepository)
            )
        )
    }
}