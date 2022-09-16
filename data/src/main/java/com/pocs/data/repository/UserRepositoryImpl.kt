package com.pocs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pocs.data.api.UserApi
import com.pocs.data.extension.errorMessage
import com.pocs.data.mapper.toDetailEntity
import com.pocs.data.mapper.toDto
import com.pocs.data.mapper.toUserProfileImageUrl
import com.pocs.data.model.ResponseBody
import com.pocs.data.model.user.UserProfileUpdateResponse
import com.pocs.data.paging.UserPagingSource
import com.pocs.data.paging.UserPagingSource.Companion.PAGE_SIZE
import com.pocs.data.source.UserRemoteDataSource
import com.pocs.domain.model.user.AnonymousCreateInfo
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.domain.repository.AuthRepository
import com.pocs.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val dataSource: UserRemoteDataSource,
    private val authRepository: AuthRepository
) : UserRepository {

    override fun getAll(sortingMethod: UserListSortingMethod): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { UserPagingSource(api = api, sortingMethod = sortingMethod) }
        ).flow
    }

    override fun search(
        query: String,
        sortingMethod: UserListSortingMethod
    ): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                UserPagingSource(
                    api = api,
                    query = query,
                    sortingMethod = sortingMethod
                )
            }
        ).flow
    }

    override suspend fun getUserDetail(id: Int): Result<UserDetail> {
        return try {
            val response = dataSource.getUserDetail(id)
            if (response.isSuccessful) {
                Result.success(response.body()!!.data.toDetailEntity())
            } else {
                throw Exception(response.errorMessage)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(
        id: Int,
        password: String?,
        name: String,
        email: String,
        company: String?,
        github: String?,
        useDefaultProfileImage: Boolean,
        newProfileImage: File?
    ): Result<Unit> {
        if (useDefaultProfileImage) {
            require(newProfileImage == null) { "기본 이미지로 변경하는 경우 newProfileImage은 null이어야 합니다." }
        }
        return try {
            val updateResponse = dataSource.updateUser(
                id = id,
                password = password,
                name = name,
                email = email,
                company = company,
                github = github
            )
            var imageResponse: Response<ResponseBody<UserProfileUpdateResponse>>? = null
            if (newProfileImage != null) {
                // 새로운 이미지 변경이 있을때만 새 파일과 함께 변경 요청을 보낸다.
                imageResponse = dataSource.uploadProfileImage(
                    id = id,
                    profileImage = newProfileImage
                )
            } else if (useDefaultProfileImage) {
                imageResponse = dataSource.uploadProfileImage(
                    id = id,
                    profileImage = null
                )
            }

            if (imageResponse?.isSuccessful == true) {
                val imageUrl = imageResponse.body()!!.data.userProfilePath?.toUserProfileImageUrl()
                authRepository.syncCurrentUserProfileImage(imageUrl)
            }

            if (updateResponse.isSuccessful && imageResponse?.isSuccessful != false) {
                Result.success(Unit)
            } else {
                if (!updateResponse.isSuccessful) {
                    throw Exception(updateResponse.errorMessage)
                } else if (imageResponse?.isSuccessful == false) {
                    throw Exception(imageResponse.errorMessage)
                } else {
                    throw Exception("유저 정보 업데이트 실패")
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createAnonymous(anonymousCreateInfo: AnonymousCreateInfo): Result<Unit> {
        return try {
            val response = dataSource.createAnonymous(anonymousCreateInfo.toDto())
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                throw Exception(response.errorMessage)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}