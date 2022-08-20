package com.pocs.test_library.fake.source.local

import com.pocs.data.model.auth.AuthLocalData
import com.pocs.data.source.AuthLocalDataSource
import javax.inject.Inject

class FakeAuthLocalDataSource @Inject constructor() : AuthLocalDataSource {

    var authLocalData: AuthLocalData? = null

    override fun hasData(): Boolean {
        return getData() != null
    }

    override fun getData(): AuthLocalData? {
        return authLocalData
    }

    override fun setData(authLocalData: AuthLocalData) {
        this.authLocalData = authLocalData
    }

    override fun clear() {
        authLocalData = null
    }
}