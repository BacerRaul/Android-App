package com.bacer.notesapp.utils

import android.content.Context
import android.net.Uri
import java.io.File

fun saveImageToInternalStorage(context: Context, uri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri) ?: return ""
    val fileName = "note_img_${System.currentTimeMillis()}.jpg"
    val file = File(context.filesDir, fileName)

    file.outputStream().use { output ->
        inputStream.copyTo(output)
    }

    return file.absolutePath  // Save this in Room instead of the original URI
}
