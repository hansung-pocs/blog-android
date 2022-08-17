package com.pocs.data.di

import com.pocs.data.api.AdminApi
import com.pocs.data.api.NonMemberApi
import com.pocs.data.api.PostApi
import com.pocs.data.api.UserApi
import com.pocs.data.repository.*
import com.pocs.data.source.*
import com.pocs.domain.repository.*
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

    @Singleton
    @Provides
    fun provideAuthRepository(
        remoteDataSource: AuthRemoteDataSource,
        localDataSource: AuthLocalDataSource
    ): AuthRepository {
        return AuthRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource
        )
    }

    @Singleton
    @Provides
    fun provideNonMemberRepository(
        dataSource: NonMemberRemoteDataSource
    ): NonMemberRepository {
        return NonMemberRepositoryImpl(
            dataSource = dataSource
        )
    }
}