package com.pocs.data.di

import android.content.Context
import android.content.SharedPreferences
import com.pocs.data.source.AuthLocalDataSource
import com.pocs.data.source.AuthLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Singleton
    @Provides
    @Named("auth")
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthLocalDataSource(
        @Named("auth") sharedPreferences: SharedPreferences
    ): AuthLocalDataSource {
        return AuthLocalDataSourceImpl(sharedPreferences)
    }
}