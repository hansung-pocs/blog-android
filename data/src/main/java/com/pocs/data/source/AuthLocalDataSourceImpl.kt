package com.pocs.data.source

import android.content.SharedPreferences
import com.google.gson.Gson
import com.pocs.data.model.auth.AuthLocalData
import javax.inject.Inject
import javax.inject.Named

class AuthLocalDataSourceImpl @Inject constructor(
    @Named("auth") private val sharedPreferences: SharedPreferences
) : AuthLocalDataSource {

    private var data: AuthLocalData? = null

    override fun getData(): AuthLocalData? {
        // 이미 전에 데이터를 읽어 값이 있는 경우에는 곧바로 데이터를 반환한다.
        if (data != null) {
            return data
        }
        val json = sharedPreferences.getString(AUTH_LOCAL_DATA_PREFS_KEY, null) ?: return null
        data = Gson().fromJson(json, AuthLocalData::class.java)
        return data
    }

    override fun setData(authLocalData: AuthLocalData) {
        data = authLocalData
        val json = Gson().toJson(authLocalData)
        sharedPreferences.edit().putString(AUTH_LOCAL_DATA_PREFS_KEY, json).apply()
    }

    override fun clear() {
        data = null
        sharedPreferences.edit().remove(AUTH_LOCAL_DATA_PREFS_KEY).apply()
    }

    companion object {
        private const val AUTH_LOCAL_DATA_PREFS_KEY = "authLocalData"
    }
}