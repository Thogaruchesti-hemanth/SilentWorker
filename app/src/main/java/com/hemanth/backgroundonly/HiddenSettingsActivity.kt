package com.hemanth.backgroundonly

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.hemanth.backgroundonly.ui.theme.BackgroundonlyTheme

class HiddenSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BackgroundonlyTheme {
                if (Build.VERSION.SDK_INT >= 33) {
                    requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
                }
                Text(text = "Hi 👋")
            }
        }
    }
}