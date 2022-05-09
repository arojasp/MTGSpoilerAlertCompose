package es.alejandro.mtgspoileralert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import es.alejandro.mtgspoileralert.cards.CardsScreen
import es.alejandro.mtgspoileralert.detail.CardDetailScreen
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
    
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "sets" ) {
        composable("sets") {
            SetsScreen { set ->
                navController.navigate("cards/${set}")
            }
        }
        composable("cards/{set}", arguments = listOf(navArgument("set"){
            type = NavType.StringType
        })) {
            val setString = remember {
                it.arguments?.getString("set")
            }
            CardsScreen(set = setString) { cardId ->
                navController.navigate("card/${cardId}")
            }
        }
        composable("card/{id}", arguments = listOf(navArgument("id"){type = NavType.StringType})) {
            val cardIdString = remember {
                it.arguments?.getString("id")
            }
            CardDetailScreen(cardId = cardIdString)
        }
    }
}
