package com.list_app.data.model

enum class ThemeMode(val displayName: String) {
    SYSTEM("Follow System"),
    LIGHT("Light"),
    DARK("Dark")
}

data class ThemeConfig(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val accentTheme: AccentTheme = AccentTheme.TEAL,
    val useDynamicColor: Boolean = false,
    val customAccentColorHex: String? = null
) {
    val isDarkMode: Boolean
        get() = themeMode == ThemeMode.DARK

    constructor(isDarkMode: Boolean, accentTheme: AccentTheme) : this(
        themeMode = if (isDarkMode) ThemeMode.DARK else ThemeMode.LIGHT,
        accentTheme = accentTheme,
        useDynamicColor = false,
        customAccentColorHex = null
    )
}
