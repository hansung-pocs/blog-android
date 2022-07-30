package com.pocs.domain.usecase.user

import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.domain.repository.UserRepository
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(sortType: UserListSortingMethod) = repository.getAll(sortType)
}