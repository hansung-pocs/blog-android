package com.pocs.presentation.extension

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.InputStream

fun Uri.getImageSizeInMegaByte(context: Context): Long {
    val scheme: String? = this.scheme
    var dataSize = 0L
    if (scheme == ContentResolver.SCHEME_CONTENT) {
        try {
            val fileInputStream: InputStream? = context.contentResolver.openInputStream(this)
            if (fileInputStream != null) {
                dataSize = fileInputStream.available().toLong()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } else if (scheme == ContentResolver.SCHEME_FILE) {
        val path: String? = this.path
        var file: File? = null
        try {
            file = path?.let { File(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (file != null) {
            dataSize = file.length().toInt().toLong()
        }
    }
    return dataSize / (1024 * 1024)
}