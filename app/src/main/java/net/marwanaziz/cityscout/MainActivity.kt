package net.marwanaziz.cityscout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import net.marwanaziz.ui.MainView
import net.marwanaziz.ui.UIApiKeys

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UIApiKeys.rapidApiKey = KeyProvider.rapidApiKey
        UIApiKeys.weatherApiKey = KeyProvider.weatherApiKey
        setContent {
            MainView()
        }
    }
}
