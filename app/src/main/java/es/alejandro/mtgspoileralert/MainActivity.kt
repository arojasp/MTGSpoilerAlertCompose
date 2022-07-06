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
import androidx.compose.ui.res.stringResource
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
import es.alejandro.mtgspoileralert.sets.SetsScreen
import es.alejandro.mtgspoileralert.settings.SettingsScreen
import es.alejandro.mtgspoileralert.settings.model.Settings
import es.alejandro.mtgspoileralert.ui.theme.MTGSpoilerAlertTheme
import es.alejandro.mtgspoileralert.util.NavigationConstant
import es.alejandro.mtgspoileralert.util.NotificationConstant
import es.alejandro.mtgspoileralert.util.WorkerConstant

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
    NavHost(navController = navController, startDestination = NavigationConstant.SETS_DESTINATION) {
        composable(NavigationConstant.SETS_DESTINATION) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(stringResource(id = R.string.main_sets), style = MaterialTheme.typography.h1) },
                        actions = {
                            IconButton(onClick = {
                                navController.navigate(NavigationConstant.SETTINGS_DESTINATION)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            ) { padding ->
                SetsScreen(
                    padding,
                    onItemClick = { set ->
                        navController.navigate("${NavigationConstant.CARDS_DESTINATION}/$set")
                    },
                    preferencesAction = { settings ->
                        setUpWorker(context, settings)
                    }
                )
            }
        }
        composable(
            "${NavigationConstant.CARDS_DESTINATION}/{${NavigationConstant.SETS_ARGUMENT}}",
            arguments = listOf(
                navArgument(NavigationConstant.SETS_ARGUMENT) {
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${NavigationConstant.BASE_URI}/set={set}"
                }
            )
        ) {
            val setString = remember {
                it.arguments?.getString(NavigationConstant.SETS_ARGUMENT)
            }
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(stringResource(id = R.string.main_cards), style = MaterialTheme.typography.h1) },
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
            ) { padding ->
                CardsScreen(padding, set = setString) { cardId ->
                    navController.navigate("${NavigationConstant.CARD_DESTINATION}/$cardId")
                }
            }
        }
        composable(
            "${NavigationConstant.CARD_DESTINATION}/{${NavigationConstant.CARD_ARGUMENT}}",
            arguments = listOf(navArgument(NavigationConstant.CARD_ARGUMENT) { type = NavType.StringType })
        ) {
            val cardIdString = remember {
                it.arguments?.getString(NavigationConstant.CARD_ARGUMENT)
            }
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(stringResource(id = R.string.main_details), style = MaterialTheme.typography.h1) },
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
            ) { padding ->
                CardDetailScreen(padding, cardId = cardIdString)
            }
        }
        composable(NavigationConstant.SETTINGS_DESTINATION) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(stringResource(id = R.string.main_settings), style = MaterialTheme.typography.h1) },
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
            ) { padding ->
                SettingsScreen(padding) {
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

    WorkManager.getInstance(context).cancelUniqueWork(WorkerConstant.UNIQUE_WORK_NAME)

    if (settings.coreSetListen) {
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WorkerConstant.UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = context.getString(R.string.notification_channel_title)
        val desc = context.getString(R.string.notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(NotificationConstant.CHANNEL_ID, name, importance).apply {
            description = desc
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
