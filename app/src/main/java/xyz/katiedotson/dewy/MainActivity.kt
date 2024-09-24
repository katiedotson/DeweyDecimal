package xyz.katiedotson.dewy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import xyz.katiedotson.dewy.ui.theme.DeweyDecimalTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeweyDecimalTheme {
                MainComponent()
            }
        }
    }
}

