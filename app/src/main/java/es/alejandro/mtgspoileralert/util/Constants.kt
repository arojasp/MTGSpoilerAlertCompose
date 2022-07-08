package es.alejandro.mtgspoileralert.util

object WorkerConstant {
    const val TAG = "CallWorker"
    const val UNIQUE_WORK_NAME = "getSets"
}

object PreferencesConstant{
    object Set{
        const val SETS_KEY = "sets"
        const val LAST_SET_KEY = "last_set"
    }

    object Settings{
        const val SETTINGS_KEY = "settings"
        const val CORE_LISTEN_KEY = "core_listen"
        const val INTERVAL_KEY = "interval"
        const val TIME_UNIT_KEY = "time_unit"
        const val CARD_LANGUAGE_KEY = "card_language"
    }
}
object NavigationConstant {
    const val BASE_URI = "https://mtgsac.com"
    const val SETS_DESTINATION = "sets"
    const val SETS_ARGUMENT = "set"
    const val SETTINGS_DESTINATION = "settings"
    const val CARDS_DESTINATION = "cards"
    const val CARD_DESTINATION = "card"
    const val CARD_ARGUMENT = "cardId"
}
object NotificationConstant {
    const val CHANNEL_ID = "SetsChannel"
    const val NOTIFICATION_ID = 1
}