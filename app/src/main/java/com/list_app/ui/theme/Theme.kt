package com.list_app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.list_app.data.model.ThemeConfig
import com.list_app.data.model.ThemeMode

@Composable
fun ListAppTheme(
    themeConfig: ThemeConfig = ThemeConfig(),
    dynamicColor: Boolean = themeConfig.useDynamicColor,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val darkTheme = when (themeConfig.themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val useDynamic = dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val customHex = themeConfig.customAccentColorHex

    val colorScheme = when {
        useDynamic && darkTheme -> dynamicDarkColorScheme(context)
        useDynamic && !darkTheme -> dynamicLightColorScheme(context)
        customHex != null -> {
            val seedColor = runCatching { Color(android.graphics.Color.parseColor(customHex)) }.getOrDefault(TealLightPrimary)
            if (darkTheme) generateDarkColorScheme(seedColor) else generateLightColorScheme(seedColor)
        }
        darkTheme -> getDarkColorScheme(themeConfig.accentTheme)
        else -> getLightColorScheme(themeConfig.accentTheme)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
