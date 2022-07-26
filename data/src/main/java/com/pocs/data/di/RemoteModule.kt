package com.pocs.data.di

import com.pocs.data.api.PostApi
import com.pocs.data.source.PostRemoteDataSource
import com.pocs.data.source.PostRemoteDataSourceImpl
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
    fun providePostRemoteDataSource(api: PostApi) : PostRemoteDataSource {
        return PostRemoteDataSourceImpl(api)
    }
}