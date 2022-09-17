package com.pocs.data.source

import com.pocs.data.model.auth.AuthLocalData

interface AuthLocalDataSource {
    fun hasData(): Boolean
    fun getData(): AuthLocalData?
    fun setData(authLocalData: AuthLocalData)
    fun clear()
}
