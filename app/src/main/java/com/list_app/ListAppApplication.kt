package com.list_app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import com.list_app.data.local.AppDatabase
import com.list_app.data.local.ThemePreferences
import com.list_app.data.repository.ListRepository

class ListAppApplication : Application(), ImageLoaderFactory {

    val database by lazy { AppDatabase.getInstance(this) }
    val themePreferences by lazy { ThemePreferences(this) }
    val repository by lazy {
        ListRepository(
            userListDao = database.userListDao(),
            listItemDao = database.listItemDao(),
            themePreferences = themePreferences
        )
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(100L * 1024L * 1024L)
                    .build()
            }
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .respectCacheHeaders(false)
            .allowHardware(true)
            .build()
    }
}
