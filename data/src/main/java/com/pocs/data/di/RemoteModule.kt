package com.pocs.data.di

import com.pocs.data.api.*
import com.pocs.data.source.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Singleton
    @Provides
    fun providePostRemoteDataSource(api: PostApi): PostRemoteDataSource {
        return PostRemoteDataSourceImpl(api)
    }

    @Singleton
    @Provides
    fun provideCommentRemoteDataSource(api: CommentApi): CommentRemoteDataSource {
        return CommentRemoteDataSourceImpl(api)
    }

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(api: UserApi): UserRemoteDataSource {
        return UserRemoteDataSourceImpl(api)
    }

    @Singleton
    @Provides
    fun provideAdminRemoteDataSource(api: AdminApi): AdminRemoteDataSource {
        return AdminRemoteDataSourceImpl(api)
    }

    @Singleton
    @Provides
    fun provideAuthRemoteDataSource(api: AuthApi): AuthRemoteDataSource {
        return AuthRemoteDataSourceImpl(api)
    }
}