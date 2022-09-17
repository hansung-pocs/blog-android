package com.pocs.test_library.di

import com.pocs.data.di.LocalModule
import com.pocs.data.source.AuthLocalDataSource
import com.pocs.test_library.fake.source.local.FakeAuthLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LocalModule::class]
)
abstract class FakeLocalModule {

    @Singleton
    @Binds
    abstract fun provideAuthLocalDataSource(
        fakeAuthLocalDataSource: FakeAuthLocalDataSource
    ): AuthLocalDataSource
}
