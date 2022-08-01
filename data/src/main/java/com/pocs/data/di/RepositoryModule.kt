package com.pocs.data.di

import com.pocs.data.api.AdminApi
import com.pocs.data.api.PostApi
import com.pocs.data.api.UserApi
import com.pocs.data.repository.AdminRepositoryImpl
import com.pocs.data.repository.PostRepositoryImpl
import com.pocs.data.repository.UserRepositoryImpl
import com.pocs.data.source.AdminRemoteDataSource
import com.pocs.data.source.PostRemoteDataSource
import com.pocs.data.source.UserRemoteDataSource
import com.pocs.domain.repository.AdminRepository
import com.pocs.domain.repository.PostRepository
import com.pocs.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun providePostRepository(api: PostApi, dataSource: PostRemoteDataSource): PostRepository {
        return PostRepositoryImpl(api = api, dataSource = dataSource)
    }

    @Singleton
    @Provides
    fun provideUserRepository(api: UserApi, dataSource: UserRemoteDataSource): UserRepository {
        return UserRepositoryImpl(api = api, dataSource = dataSource)
    }

    @Singleton
    @Provides
    fun provideAdminRepository(api: AdminApi, dataSource: AdminRemoteDataSource): AdminRepository {
        return AdminRepositoryImpl(api = api, dataSource = dataSource)
    }
}