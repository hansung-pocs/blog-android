package com.pocs.presentation

import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.domain.usecase.user.GetAllUsersUseCase
import com.pocs.presentation.view.user.UserViewModel
import com.pocs.test_library.fake.FakeUserRepositoryImpl
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    private val repository = FakeUserRepositoryImpl()
    private lateinit var viewModel: UserViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = UserViewModel(GetAllUsersUseCase(repository))
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
}