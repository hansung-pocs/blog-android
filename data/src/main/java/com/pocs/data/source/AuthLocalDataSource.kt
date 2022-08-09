package com.pocs.data.source

import com.pocs.data.model.auth.AuthLocalData

interface AuthLocalDataSource {
    fun getData(): AuthLocalData?
    fun setData(authLocalData: AuthLocalData)
    fun clear()
}