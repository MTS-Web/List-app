package com.list_app.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.list_app.ui.screens.ListDetailScreen
import com.list_app.ui.screens.ListOverviewScreen
import com.list_app.ui.screens.ThemeSettingsScreen
import com.list_app.ui.viewmodel.ListViewModel

@Composable
fun NavGraph(
    viewModel: ListViewModel
) {
    val backStack = remember { mutableStateListOf<Any>(Screen.Overview) }

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = { key ->
            when (key) {
                is Screen.Overview -> NavEntry(key) {
                    ListOverviewScreen(
                        viewModel = viewModel,
                        onNavigateToListDetail = { listId ->
                            if (backStack.lastOrNull() != Screen.Detail(listId)) {
                                backStack.add(Screen.Detail(listId))
                            }
                        },
                        onNavigateToThemeSettings = {
                            if (backStack.lastOrNull() != Screen.ThemeSettings) {
                                backStack.add(Screen.ThemeSettings)
                            }
                        }
                    )
                }
                is Screen.Detail -> NavEntry(key) {
                    ListDetailScreen(
                        listId = key.listId,
                        viewModel = viewModel,
                        onBack = {
                            if (backStack.size > 1) {
                                backStack.removeLastOrNull()
                            }
                        }
                    )
                }
                is Screen.ThemeSettings -> NavEntry(key) {
                    ThemeSettingsScreen(
                        viewModel = viewModel,
                        onBack = {
                            if (backStack.size > 1) {
                                backStack.removeLastOrNull()
                            }
                        }
                    )
                }
                else -> NavEntry(key) {
                    Text("Unknown Screen")
                }
            }
        }
    )
}
