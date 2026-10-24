package com.list_app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.list_app.data.model.AccentTheme
import com.list_app.data.model.ThemeConfig
import com.list_app.data.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

open class ThemePreferences(private val context: Context?) {

    constructor() : this(null)

    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val ACCENT_THEME = stringPreferencesKey("accent_theme")
        val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
        val CUSTOM_ACCENT_COLOR_HEX = stringPreferencesKey("custom_accent_color_hex")
        val IS_FIRST_LAUNCH_COMPLETED = booleanPreferencesKey("is_first_launch_completed")
    }

    open val themeConfigFlow: Flow<ThemeConfig> = context?.dataStore?.data
        ?.map { preferences ->
            val themeModeString = preferences[PreferencesKeys.THEME_MODE]
            val themeMode = if (themeModeString != null) {
                runCatching { ThemeMode.valueOf(themeModeString) }.getOrDefault(ThemeMode.SYSTEM)
            } else {
                val isDark = preferences[PreferencesKeys.IS_DARK_MODE]
                if (isDark != null) {
                    if (isDark) ThemeMode.DARK else ThemeMode.LIGHT
                } else {
                    ThemeMode.SYSTEM
                }
            }
            val accentThemeName = preferences[PreferencesKeys.ACCENT_THEME] ?: AccentTheme.TEAL.name
            val accentTheme = runCatching { AccentTheme.valueOf(accentThemeName) }.getOrDefault(AccentTheme.TEAL)
            val useDynamicColor = preferences[PreferencesKeys.USE_DYNAMIC_COLOR] ?: false
            val customAccentColorHex = preferences[PreferencesKeys.CUSTOM_ACCENT_COLOR_HEX]

            ThemeConfig(
                themeMode = themeMode,
                accentTheme = accentTheme,
                useDynamicColor = useDynamicColor,
                customAccentColorHex = customAccentColorHex
            )
        } ?: emptyFlow()

    open suspend fun updateThemeConfig(themeConfig: ThemeConfig) {
        context?.dataStore?.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = themeConfig.themeMode.name
            preferences[PreferencesKeys.IS_DARK_MODE] = themeConfig.isDarkMode
            preferences[PreferencesKeys.ACCENT_THEME] = themeConfig.accentTheme.name
            preferences[PreferencesKeys.USE_DYNAMIC_COLOR] = themeConfig.useDynamicColor
            if (themeConfig.customAccentColorHex != null) {
                preferences[PreferencesKeys.CUSTOM_ACCENT_COLOR_HEX] = themeConfig.customAccentColorHex
            } else {
                preferences.remove(PreferencesKeys.CUSTOM_ACCENT_COLOR_HEX)
            }
        }
    }

    open suspend fun setThemeMode(themeMode: ThemeMode) {
        context?.dataStore?.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = themeMode.name
            preferences[PreferencesKeys.IS_DARK_MODE] = (themeMode == ThemeMode.DARK)
        }
    }

    open suspend fun setDarkMode(isDarkMode: Boolean) {
        setThemeMode(if (isDarkMode) ThemeMode.DARK else ThemeMode.LIGHT)
    }

    open suspend fun setAccentTheme(accentTheme: AccentTheme) {
        context?.dataStore?.edit { preferences ->
            preferences[PreferencesKeys.ACCENT_THEME] = accentTheme.name
            preferences.remove(PreferencesKeys.CUSTOM_ACCENT_COLOR_HEX)
        }
    }

    open suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        context?.dataStore?.edit { preferences ->
            preferences[PreferencesKeys.USE_DYNAMIC_COLOR] = useDynamicColor
        }
    }

    open suspend fun setCustomAccentColorHex(hex: String?) {
        context?.dataStore?.edit { preferences ->
            if (hex != null) {
                preferences[PreferencesKeys.CUSTOM_ACCENT_COLOR_HEX] = hex
            } else {
                preferences.remove(PreferencesKeys.CUSTOM_ACCENT_COLOR_HEX)
            }
        }
    }

    open suspend fun isFirstLaunchCompleted(): Boolean {
        val prefs = context?.dataStore?.data?.first() ?: return false
        return prefs[PreferencesKeys.IS_FIRST_LAUNCH_COMPLETED] ?: false
    }

    open suspend fun setFirstLaunchCompleted(completed: Boolean) {
        context?.dataStore?.edit { preferences ->
            preferences[PreferencesKeys.IS_FIRST_LAUNCH_COMPLETED] = completed
        }
    }
}
