package com.pocs.data

import com.pocs.data.di.NetworkModule
import com.pocs.data.model.user.UserListSortingMethodDto
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ApiTest {

    @Test
    fun test() {
        val api = with(NetworkModule()) {
            provideUserApiService(provideRetrofit(provideHttpClient()))
        }

        runBlocking {
            val result = api.getAll(UserListSortingMethodDto.STUDENT_ID)
            print(result)
        }
    }
}