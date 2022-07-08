package es.alejandro.mtgspoileralert.settings.model

import java.util.concurrent.TimeUnit

data class Settings(
    val coreSetListen: Boolean = false,
    val interval: Pair<Long, TimeUnit> = Pair(15, TimeUnit.MINUTES),
    val language: String = "en",
    val languagesAvailable: List<String> = listOf("en", "es")
)
