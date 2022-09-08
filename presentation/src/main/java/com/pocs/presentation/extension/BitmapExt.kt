package com.pocs.presentation.extension

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.InputStream
import kotlin.math.roundToInt

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

fun Bitmap.scaleDown(maxImageSize: Float): Bitmap {
    val ratio = (maxImageSize / width).coerceAtMost(maxImageSize / height)
    val width = (ratio * width).roundToInt()
    val height = (ratio * height).roundToInt()

    if (ratio >= 1.0) {
        return this
    }
    return Bitmap.createScaledBitmap(
        this,
        width,
        height,
        true
    )
}