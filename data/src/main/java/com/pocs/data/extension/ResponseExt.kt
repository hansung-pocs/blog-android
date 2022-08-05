package com.pocs.data.extension

import com.pocs.data.model.ResponseBody
import org.json.JSONObject
import retrofit2.Response

val <T> Response<ResponseBody<T>>.errorMessage: String?
    get() {
        if (this.isSuccessful) throw IllegalStateException()

        return try {
            val jsonString = this.errorBody()?.string() ?: throw IllegalStateException()
            val jObjError = JSONObject(jsonString)
            jObjError.getString("message")
        } catch (e: Exception) {
            e.message
        }
    }
