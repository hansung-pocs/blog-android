package com.pocs.data

import com.pocs.data.di.NetworkModule
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PostApiTest {
    @Test
    fun getAllWorksSuccessfully() {
        val api = with(NetworkModule()) {
            providePostApiService(provideRetrofit(provideHttpClient()))
        }

        runBlocking {
            val response = api.getAll()
            assertTrue(response.status == 200)
        }
    }
}