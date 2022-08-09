package com.pocs.test_library.di

import com.pocs.data.di.RemoteModule
import com.pocs.data.source.AdminRemoteDataSource
import com.pocs.data.source.AuthRemoteDataSource
import com.pocs.data.source.PostRemoteDataSource
import com.pocs.data.source.UserRemoteDataSource
import com.pocs.test_library.fake.source.remote.FakeAdminRemoteDataSource
import com.pocs.test_library.fake.source.remote.FakeAuthRemoteDataSource
import com.pocs.test_library.fake.source.remote.FakePostRemoteDataSource
import com.pocs.test_library.fake.source.remote.FakeUserRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RemoteModule::class]
)
abstract class FakeRemoteModule {

    @Singleton
    @Binds
    abstract fun providePostRemoteDataSource(fakePostRemoteDataSource: FakePostRemoteDataSource): PostRemoteDataSource

    @Singleton
    @Binds
    abstract fun provideUserRemoteDataSource(fakeUserRemoteDataSource: FakeUserRemoteDataSource): UserRemoteDataSource

    @Singleton
    @Binds
    abstract fun provideAdminRemoteDataSource(fakeAdminRemoteDataSource: FakeAdminRemoteDataSource): AdminRemoteDataSource

    @Singleton
    @Binds
    abstract fun provideAuthRemoteDataSource(fakeAuthRemoteDataSource: FakeAuthRemoteDataSource): AuthRemoteDataSource
}