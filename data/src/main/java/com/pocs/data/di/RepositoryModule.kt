package com.pocs.data.di

import com.pocs.data.api.PostApi
import com.pocs.data.repository.PostRepositoryImpl
import com.pocs.domain.repository.PostRepository
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
    fun providePostRepository(api: PostApi) : PostRepository {
        return PostRepositoryImpl(api)
    }
}