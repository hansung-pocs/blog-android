package com.pocs.domain.usecase.user

import com.pocs.domain.repository.UserRepository
import javax.inject.Inject

class SearchUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(query: String) = repository.search(query = query)
}