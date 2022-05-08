package es.alejandro.mtgspoileralert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import es.alejandro.mtgspoileralert.sets.SetsScreen
import es.alejandro.mtgspoileralert.ui.theme.MTGSpoilerAlertTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MTGSpoilerAlertTheme {
                MTGApp()
            }
        }
    }
}

@Composable
fun MTGApp() {
    SetsScreen()
}
