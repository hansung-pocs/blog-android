package com.pocs.domain.usecase.user

import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.domain.repository.UserRepository
import javax.inject.Inject

class SearchUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(query: String, sortingMethod: UserListSortingMethod) =
        repository.search(query = query, sortingMethod = sortingMethod)
}