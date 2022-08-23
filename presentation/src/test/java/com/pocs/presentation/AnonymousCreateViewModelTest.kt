package com.pocs.presentation

import com.pocs.domain.usecase.user.CreateAnonymousUseCase
import com.pocs.presentation.view.user.anonymous.AnonymousCreateViewModel
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AnonymousCreateViewModelTest {

    private val userRepository = FakeUserRepositoryImpl()

    private lateinit var viewModel: AnonymousCreateViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldIsSuccessToCreateIsTrue_WhenSuccessToCreateAnonymous() {
        userRepository.createAnonymousResult = Result.success(Unit)
        initViewModel()

        viewModel.uiState.onCreate()

        assertTrue(viewModel.uiState.isSuccessToCreate)
    }

    @Test
    fun shouldHaveErrorMessage_WhenFailedToCreateAnonymous() {
        val errorMessage = "에러입니다."
        userRepository.createAnonymousResult = Result.failure(Exception(errorMessage))
        initViewModel()

        viewModel.uiState.onCreate()

        assertEquals(errorMessage, viewModel.uiState.errorMessage)
    }

    private fun initViewModel() {
        viewModel = AnonymousCreateViewModel(
            CreateAnonymousUseCase(userRepository)
        )
    }
}