package com.pocs.data.di

import com.pocs.data.BuildConfig
import com.pocs.data.api.*
import com.pocs.data.mapper.EnumConverterFactory
import com.pocs.data.source.AuthLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        private const val BASE_URL = BuildConfig.SERVER_URL
        private const val TOKEN_HEADER_KEY = "x-pocs-session-token"
    }

    @Provides
    @Singleton
    fun provideHttpClient(authLocalDataSource: AuthLocalDataSource): OkHttpClient {
        val client = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    this.level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .addInterceptor {
                val authLocalData = authLocalDataSource.getData()
                var newRequest = it.request()
                if (authLocalData != null) {
                    newRequest = newRequest.newBuilder().addHeader(
                        name = TOKEN_HEADER_KEY,
                        value = authLocalData.sessionToken
                    ).build()
                }
                it.proceed(newRequest)
            }

        return client.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(EnumConverterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun providePostApiService(retrofit: Retrofit): PostApi {
        return retrofit.create(PostApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCommentApiService(retrofit: Retrofit): CommentApi {
        return retrofit.create(CommentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAdminApiService(retrofit: Retrofit): AdminApi {
        return retrofit.create(AdminApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}
