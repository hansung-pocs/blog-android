package com.pocs.domain.usecase.util

import android.content.Context
import android.graphics.Bitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ConvertBitmapToFileUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(bitmap: Bitmap, fileName: String) : File? {
        val file = File(context.cacheDir, fileName)
        return try {
            val outStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream.flush()
            outStream.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}