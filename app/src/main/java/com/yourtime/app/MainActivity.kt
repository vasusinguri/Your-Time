package com.yourtime.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.yourtime.app.data.PreferencesManager
import com.yourtime.app.ui.YourTimeScreen
import com.yourtime.app.ui.theme.YourTimeTheme
import com.yourtime.app.viewmodel.YourTimeViewModel

class MainActivity : ComponentActivity() {

    private val preferencesManager by lazy {
        PreferencesManager(applicationContext)
    }

    private val viewModel: YourTimeViewModel by viewModels {
        YourTimeViewModel.Factory(preferencesManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enforce dark status bar and navigation bar with transparent background
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        // Observe lifecycle to pause/resume real-time ticker
        lifecycle.addObserver(LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.onForegroundResume()
                    com.yourtime.app.widget.YourTimeWidgetUpdater.updateAllWidgets(applicationContext)
                }
                Lifecycle.Event.ON_PAUSE -> viewModel.onBackgroundPause()
                else -> Unit
            }
        })

        setContent {
            YourTimeTheme {
                YourTimeScreen(viewModel = viewModel)
            }
        }
    }
}
