package com.pocs.presentation

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.pocs.domain.model.post.PostFilterType
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.post.GetAllPostsUseCase
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.auth.IsCurrentUserAnonymousUseCase
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.view.home.post.PostViewModel
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakePostRepositoryImpl
import com.pocs.test_library.mock.mockAdminUserDetail
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
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.MEMBER)
        initViewModel()

        assertTrue(viewModel.uiState.value.visiblePostWriteFab)
    }

    @Test
    fun shouldDoNotVisibleFab_WhenCurrentUserIsAnonymous() {
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.ANONYMOUS)
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
        viewModel.updatePostFilterType(PostFilterType.STUDY)
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

    @Test
    fun shouldExistCategory_WhenPostFilterTypeIsAllOrBest() = runTest {
        val differ = AsyncPagingDataDiffer(
            diffCallback = PostItemUiStateDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )
        initViewModel()
        viewModel.updatePostFilterType(PostFilterType.ALL)

        postRepository.postPagingDataFlow.emit(PagingData.from(listOf(mockPost)))

        differ.submitData(viewModel.uiState.value.pagingData)
        advanceUntilIdle()
        differ.snapshot().items.forEach {
            assertNotNull(it.category)
        }
    }


    @Test
    fun shouldCategoryIsNull_WhenPostFilterTypeIsNotAllOrBest() = runTest {
        val differ = AsyncPagingDataDiffer(
            diffCallback = PostItemUiStateDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )
        initViewModel()
        viewModel.updatePostFilterType(PostFilterType.STUDY)

        postRepository.postPagingDataFlow.emit(PagingData.from(listOf(mockPost)))

        differ.submitData(viewModel.uiState.value.pagingData)
        advanceUntilIdle()
        differ.snapshot().items.forEach {
            assertNull(it.category)
        }
    }

    private fun initViewModel() {
        viewModel = PostViewModel(
            getAllPostsUseCase = GetAllPostsUseCase(postRepository),
            isCurrentUserAnonymousUseCase = IsCurrentUserAnonymousUseCase(
                GetCurrentUserTypeUseCase(authRepository)
            )
        )
    }
}