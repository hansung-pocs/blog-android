package com.pocs.data

import com.pocs.data.di.NetworkModule
import com.pocs.data.model.user.UserUpdateBody
import kotlinx.coroutines.runBlocking
import org.junit.Test

class difj {
    @Test
    fun sifj() {
        val api = with(NetworkModule()) {
            provideUserApiService(provideRetrofit(provideHttpClient()))
        }
        runBlocking {
            api.updateUser(1, UserUpdateBody("", "", "", "", ""))
        }
    }
}