package com.pocs.presentation.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.domain.usecase.auth.GetCurrentUserUseCase
import com.pocs.domain.usecase.auth.IsAuthReadyUseCase
import com.pocs.domain.usecase.auth.IsCurrentUserAdminUseCase
import com.pocs.presentation.model.post.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    isAuthReadyUseCase: IsAuthReadyUseCase,
    isCurrentUserAdminUseCase: IsCurrentUserAdminUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(
        HomeUiState(isCurrentUserAdmin = isCurrentUserAdminUseCase())
    )
    val uiState: StateFlow<HomeUiState> get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            isAuthReadyUseCase().collectLatest { isAuthReady ->
                if (isAuthReady) {
                    val loggedIn = getCurrentUserUseCase() != null

                    _uiState.update {
                        if (loggedIn) {
                            it.copy(hideSplashScreen = true)
                        } else {
                            // 로그인 화면으로 전환하는 경우 `hideSplashScreen`를 `true`로 갱신하지 않는다.
                            // 그 이유는 앱을 실행하고 곧바로 로그인 화면으로 이동시 짧은 시간동안 홈 화면이 등장하는
                            // 버그를 막기 위해서이다.
                            // https://github.com/hansung-pocs/blog-android/issues/150
                            it.copy(replaceToLoginActivity = true)
                        }
                    }
                }
            }
        }
    }
}