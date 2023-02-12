package com.pocs.data.extension

import com.google.gson.Gson
import com.pocs.data.model.ResponseBody
import retrofit2.Response

val <T> Response<ResponseBody<T>>.errorMessage: String?
    get() {
        if (isSuccessful) throw IllegalStateException()

        return try {
            message()
            val errorBodyJson = requireNotNull(errorBody()).string()
            val responseBody = Gson().fromJson(errorBodyJson, ResponseBody::class.java)
            responseBody.message
        } catch (e: Exception) {
            e.message
        }
    }

fun <T> Response<ResponseBody<T>>.getDataOrThrowMessage(): T {
    if (isSuccessful) {
        return requireNotNull(body()).data
    } else {
        throw Exception(errorMessage)
    }
}
