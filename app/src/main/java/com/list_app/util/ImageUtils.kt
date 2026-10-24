package com.list_app.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

object ImageUtils {

    fun copyUriToInternalStorage(context: Context, uri: Uri): String? {
        runCatching {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream == null) return uri.toString()

            val imagesDir = File(context.filesDir, "images").apply {
                if (!exists()) mkdirs()
            }
            val fileName = "img_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(8)}.jpg"
            val destFile = File(imagesDir, fileName)

            FileOutputStream(destFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            inputStream.close()

            Uri.fromFile(destFile).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            uri.toString()
        }
    }

    fun normalizeUrl(url: String): String {
        val trimmed = url.trim()
        if (trimmed.isEmpty()) return ""
        return if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            "https://$trimmed"
        } else {
            trimmed
        }
    }

    fun launchWebUrl(context: Context, url: String) {
        val normalized = normalizeUrl(url)
        if (normalized.isEmpty()) {
            Toast.makeText(context, "No website link provided", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(normalized)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Cannot open web link: $normalized", Toast.LENGTH_SHORT).show()
        }
    }
}
