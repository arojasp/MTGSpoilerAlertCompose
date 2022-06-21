package es.alejandro.mtgspoileralert

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import es.alejandro.mtgspoileralert.backgroundservice.CallWorker
import es.alejandro.mtgspoileralert.cards.CardsScreen
import es.alejandro.mtgspoileralert.detail.CardDetailScreen
import es.alejandro.mtgspoileralert.notification.NotificationService.Companion.CHANNEL_ID
import es.alejandro.mtgspoileralert.sets.SetsScreen
import es.alejandro.mtgspoileralert.settings.SettingsScreen
import es.alejandro.mtgspoileralert.settings.model.Settings
import es.alejandro.mtgspoileralert.ui.theme.MTGSpoilerAlertTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel(applicationContext)

        setContent {
            MTGSpoilerAlertTheme {
                MTGApp(applicationContext)
            }
        }
    }
}

@Composable
fun MTGApp(context: Context) {

    val navController = rememberNavController()
    val uri = "https://mtgsac.com"
    NavHost(navController = navController, startDestination = "sets") {
        composable("sets") {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Sets", style = MaterialTheme.typography.h1) },
                        actions = {
                            IconButton(onClick = {
                                navController.navigate("settings")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            ) {
                SetsScreen(
                    onItemClick = { set ->
                        navController.navigate("cards/$set")
                    },
                    preferencesAction = { settings ->
                        setUpWorker(context, settings)
                    }
                )
            }
        }
        composable(
            "cards/{set}",
            arguments = listOf(
                navArgument("set") {
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$uri/set={set}"
                }
            )
        ) {
            val setString = remember {
                it.arguments?.getString("set")
            }
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Cards", style = MaterialTheme.typography.h1) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            ) {
                CardsScreen(set = setString) { cardId ->
                    navController.navigate("card/$cardId")
                }
            }
        }
        composable(
            "card/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            val cardIdString = remember {
                it.arguments?.getString("id")
            }
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Details", style = MaterialTheme.typography.h1) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            ) {
                CardDetailScreen(cardId = cardIdString)
            }
        }
        composable("settings") {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Settings", style = MaterialTheme.typography.h1) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            ) {
                SettingsScreen {
                    setUpWorker(context, it)
                }
            }
        }
    }
}

private fun setUpWorker(context: Context, settings: Settings) {
    val request = PeriodicWorkRequestBuilder<CallWorker>(
        settings.interval.first,
        settings.interval.second
    ).build()

    WorkManager.getInstance(context).cancelUniqueWork("getSets")

    if (settings.coreSetListen) {

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "getSets",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "MTGSpoilerAlert"
        val desc = "New sets alerts"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = desc
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
