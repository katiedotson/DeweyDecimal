package xyz.katiedotson.deweydecimal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import xyz.katiedotson.deweydecimal.ui.theme.DeweyDecimalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeweyDecimalTheme {
                MainApplication()
            }
        }
    }
}

