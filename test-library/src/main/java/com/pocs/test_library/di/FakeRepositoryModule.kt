package com.pocs.test_library.di

import com.pocs.data.di.RepositoryModule
import com.pocs.domain.repository.*
import com.pocs.test_library.fake.FakeAdminRepositoryImpl
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakeCommentRepositoryImpl
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
    abstract fun providePostRepository(
        fakePostRepositoryImpl: FakePostRepositoryImpl
    ): PostRepository

    @Singleton
    @Binds
    abstract fun provideCommentRepository(
        fakeCommentRepositoryImpl: FakeCommentRepositoryImpl
    ): CommentRepository

    @Singleton
    @Binds
    abstract fun provideUserRepository(
        fakeUserRepositoryImpl: FakeUserRepositoryImpl
    ): UserRepository

    @Singleton
    @Binds
    abstract fun provideAdminRepository(
        fakeAdminRepositoryImpl: FakeAdminRepositoryImpl
    ): AdminRepository

    @Singleton
    @Binds
    abstract fun provideAuthRepository(
        fakeAuthRepositoryImpl: FakeAuthRepositoryImpl
    ): AuthRepository
}
