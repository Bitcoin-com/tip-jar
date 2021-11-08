package com.example.tipjar.core.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object FileHelper {

    fun saveBitmapToFile(
        context: Context,
        bitmap: Bitmap
    ): String? {
        return try {
            val date = SimpleDateFormat(
                "yyyy-MM-dd_HH-mm-ss",
                Locale.getDefault()
            ).format(Calendar.getInstance().time)
            val fileName = "$date.png"
            return File(context.filesDir, fileName).apply {
                createNewFile()
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream)
                val bitmapData = byteArrayOutputStream.toByteArray()

                val fos = FileOutputStream(this)
                fos.write(bitmapData)
                fos.flush()
                fos.close()
            }.path
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}