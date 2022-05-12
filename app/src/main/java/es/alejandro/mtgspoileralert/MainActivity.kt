package es.alejandro.mtgspoileralert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import es.alejandro.mtgspoileralert.backgroundservice.CallWorker
import es.alejandro.mtgspoileralert.cards.CardsScreen
import es.alejandro.mtgspoileralert.detail.CardDetailScreen
import es.alejandro.mtgspoileralert.sets.SetsScreen
import es.alejandro.mtgspoileralert.ui.theme.MTGSpoilerAlertTheme
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val request = PeriodicWorkRequestBuilder<CallWorker>(16, TimeUnit.MINUTES).build()
        WorkManager.getInstance(applicationContext).enqueue(request)

        setContent {
            MTGSpoilerAlertTheme {
                    MTGApp()
            }
        }
    }
}

@Composable
fun MTGApp() {
    
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "sets" ) {
        composable("sets") {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text("Sets")})
                }
            ) {
                SetsScreen { set ->
                    navController.navigate("cards/${set}")
                }
            }
        }
        composable("cards/{set}", arguments = listOf(navArgument("set"){
            type = NavType.StringType
        })) {
            val setString = remember {
                it.arguments?.getString("set")
            }
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Cards")},
                        navigationIcon = { IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null) }
                        }
                    )
                }
            ) {
                CardsScreen(set = setString) { cardId ->
                    navController.navigate("card/${cardId}")
                }
            }
        }
        composable("card/{id}", arguments = listOf(navArgument("id"){type = NavType.StringType})) {
            val cardIdString = remember {
                it.arguments?.getString("id")
            }
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Details")},
                        navigationIcon = { IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null) }
                        }
                    )
                }
            ) {
                CardDetailScreen(cardId = cardIdString)
            }
        }
    }
}
