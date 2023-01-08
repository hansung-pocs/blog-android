package com.pocs.data.di

import com.pocs.data.api.*
import com.pocs.data.source.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteModule {

    @Singleton
    @Binds
    abstract fun bindsPostRemoteDataSource(
        postRemoteDataSourceImpl: PostRemoteDataSourceImpl
    ): PostRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindsCommentRemoteDataSource(
        commentRemoteDataSourceImpl: CommentRemoteDataSourceImpl
    ): CommentRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindsUserRemoteDataSource(
        userRemoteDataSourceImpl: UserRemoteDataSourceImpl
    ): UserRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindsAdminRemoteDataSource(
        adminRemoteDataSourceImpl: AdminRemoteDataSourceImpl
    ): AdminRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindsAuthRemoteDataSource(
        authRemoteDataSourceImpl: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource
}
