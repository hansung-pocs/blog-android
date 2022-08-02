package com.pocs.data

import com.pocs.data.di.NetworkModule
import com.pocs.data.model.user.UserListSortingMethodDto
import org.junit.Assert.assertEquals
import org.junit.Test

class EnumConverterFactoryTest {

    @Test
    fun convertsToStringSuccessfully() {
        val retrofit = with(NetworkModule()) { provideRetrofit(provideHttpClient()) }
        val converter = retrofit.stringConverter<UserListSortingMethodDto>(
            UserListSortingMethodDto::class.java,
            UserListSortingMethodDto::class.java.annotations
        )

        assertEquals("generation", converter.convert(UserListSortingMethodDto.GENERATION))
        assertEquals("studentId", converter.convert(UserListSortingMethodDto.STUDENT_ID))
    }
}