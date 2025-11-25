package com.bacer.notesapp.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object ImageUtils {
    private fun getNoteImagesDir(context: Context): File {
        val dir = File(context.filesDir, "note_images")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun copyImageToInternalStorage(context: Context, uri: Uri): String {
        val fileName = "note_${System.currentTimeMillis()}_${UUID.randomUUID()}.jpg"
        val file = File(getNoteImagesDir(context), fileName)

        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath // This is permanent
    }

    fun deleteImageFile(context: Context, filePath: String) {
        try {
            File(filePath).delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}