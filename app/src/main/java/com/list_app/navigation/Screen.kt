package com.list_app.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Overview : Screen

    @Serializable
    data class Detail(val listId: String) : Screen

    @Serializable
    data object ThemeSettings : Screen
}
