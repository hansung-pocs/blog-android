package com.pocs.data.di

import com.pocs.data.api.AdminApi
import com.pocs.data.api.AuthApi
import com.pocs.data.api.PostApi
import com.pocs.data.api.UserApi
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