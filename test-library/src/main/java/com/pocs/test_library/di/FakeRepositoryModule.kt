package com.pocs.test_library.di

import com.pocs.data.di.RepositoryModule
import com.pocs.domain.repository.AdminRepository
import com.pocs.domain.repository.AuthRepository
import com.pocs.domain.repository.PostRepository
import com.pocs.domain.repository.UserRepository
import com.pocs.test_library.fake.FakeAdminRepositoryImpl
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakePostRepositoryImpl
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class FakeRepositoryModule {

    @Singleton
    @Binds
    abstract fun providePostRepository(fakePostRepositoryImpl: FakePostRepositoryImpl): PostRepository

    @Singleton
    @Binds
    abstract fun provideUserRepository(fakeUserRepositoryImpl: FakeUserRepositoryImpl): UserRepository

    @Singleton
    @Binds
    abstract fun provideAdminRepository(fakeAdminRepositoryImpl: FakeAdminRepositoryImpl): AdminRepository

    @Singleton
    @Binds
    abstract fun provideAuthRepository(fakeAuthRepositoryImpl: FakeAuthRepositoryImpl): AuthRepository
}