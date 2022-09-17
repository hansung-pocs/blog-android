package com.pocs.presentation

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.user.GetAllUsersUseCase
import com.pocs.domain.usecase.user.SearchUserUseCase
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.view.user.UserViewModel
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.mock.mockNormalUser
import com.pocs.test_library.paging.NoopListCallback
import com.pocs.test_library.paging.UserItemUiStateDiffCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    private val authRepository = FakeAuthRepositoryImpl()
    private val userRepository = FakeUserRepositoryImpl()

    private lateinit var viewModel: UserViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = UserViewModel(
            GetAllUsersUseCase(userRepository),
            SearchUserUseCase(userRepository),
            GetCurrentUserTypeUseCase(authRepository)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldUpdateUiState_WhenUsingUpdateSortingMethod() = runTest {
        val collectJob = launch(dispatcher) {
            viewModel.uiState.collect()
        }

        viewModel.updateSortingMethod(UserListSortingMethod.STUDENT_ID)
        assertEquals(viewModel.uiState.value.sortingMethod, UserListSortingMethod.STUDENT_ID)

        viewModel.updateSortingMethod(UserListSortingMethod.GENERATION)
        assertEquals(viewModel.uiState.value.sortingMethod, UserListSortingMethod.GENERATION)

        collectJob.cancel()
    }

    @Test
    fun shouldHaveErrorMessage_WhenQueryLengthIsLowerThan2() = runTest {
        val collectJob = launch(dispatcher) {
            viewModel.uiState.collect()
        }

        viewModel.search("h")

        assertNotNull(viewModel.uiState.value.errorMessageRes)

        collectJob.cancel()
    }

    @Test
    fun shouldHaveSearchPagingData_WhenSuccessToSearchUser() = runTest {
        // given
        val collectJob = launch(dispatcher) {
            viewModel.uiState.collect()
        }
        val differ = AsyncPagingDataDiffer(
            diffCallback = UserItemUiStateDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )
        val user = mockNormalUser
        userRepository.searchResult = flowOf(PagingData.from(listOf(user)))

        // when
        viewModel.search("hi")
        differ.submitData(viewModel.uiState.value.searchPagingData)

        // then
        assertEquals(user.toUiState(), differ.snapshot().items.first())

        collectJob.cancel()
    }

    @Test
    fun shouldClearSearchPagingData_WhenChangeToNonSearchMode() = runTest {
        // given
        val collectJob = launch(dispatcher) {
            viewModel.uiState.collect()
        }
        val differ = AsyncPagingDataDiffer(
            diffCallback = UserItemUiStateDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )
        val user = mockNormalUser
        userRepository.searchResult = flowOf(PagingData.from(listOf(user)))
        viewModel.search("hi")
        differ.submitData(viewModel.uiState.value.searchPagingData)
        assertEquals(1, differ.snapshot().items.size)

        // when
        viewModel.onSearchModeChange(enabled = false)
        differ.submitData(viewModel.uiState.value.searchPagingData)

        // then
        assertEquals(0, differ.snapshot().items.size)

        collectJob.cancel()
    }
}
