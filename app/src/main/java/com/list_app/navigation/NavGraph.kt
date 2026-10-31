package com.list_app.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(350, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(350)) togetherWith
            slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(350, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(350))
        },
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(350, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(350)) togetherWith
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(350, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(350))
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(350, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(350)) togetherWith
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(350, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(350))
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
