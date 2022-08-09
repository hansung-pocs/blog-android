package com.pocs.data.source

import android.content.SharedPreferences
import com.google.gson.Gson
import com.pocs.data.model.auth.AuthLocalData
import javax.inject.Inject
import javax.inject.Named

class AuthLocalDataSourceImpl @Inject constructor(
    @Named("auth") private val sharedPreferences: SharedPreferences
) : AuthLocalDataSource {

    override fun getData(): AuthLocalData? {
        val json = sharedPreferences.getString(AUTH_LOCAL_DATA_PREFS_KEY, null) ?: return null
        return Gson().fromJson(json, AuthLocalData::class.java)
    }

    override fun setData(authLocalData: AuthLocalData) {
        val json = Gson().toJson(authLocalData)
        sharedPreferences.edit().putString(AUTH_LOCAL_DATA_PREFS_KEY, json).apply()
    }

    override fun clear() {
        sharedPreferences.edit().remove(AUTH_LOCAL_DATA_PREFS_KEY).apply()
    }

    companion object {
        private const val AUTH_LOCAL_DATA_PREFS_KEY = "authLocalData"
    }
}