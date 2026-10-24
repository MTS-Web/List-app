package com.list_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.list_app.navigation.NavGraph
import com.list_app.ui.theme.ListAppTheme
import com.list_app.ui.viewmodel.ListViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ListViewModel by viewModels {
        val app = application as ListAppApplication
        ListViewModel.Factory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeConfig by viewModel.themeConfig.collectAsStateWithLifecycle()

            ListAppTheme(themeConfig = themeConfig) {
                NavGraph(viewModel = viewModel)
            }
        }
    }
}
