package es.alejandro.mtgspoileralert.settings.model

import androidx.compose.ui.text.capitalize
import es.alejandro.mtgspoileralert.sets.model.SetType
import es.alejandro.mtgspoileralert.settings.model.Settings.Available.formatToString
import java.util.*
import java.util.concurrent.TimeUnit

data class Settings(
    val coreSetListen: Boolean = false,
    val interval: Pair<Long, TimeUnit> = Pair(15, TimeUnit.MINUTES),
    val language: String = "en",
    val setTypes: MutableList<SetType> = mutableListOf(SetType.CORE, SetType.COMMANDER, SetType.EXPANSION)
) {
    object Available {
        val LANGUAGES = listOf("en", "es", "fr", "de", "it", "pt", "ja", "ko", "ru")
        val SET_TYPES = enumValues<SetType>()

        fun List<SetType>.formatToString(): List<String> {
            return this.map { it.formatString() }
        }

        fun SetType.formatString(): String {
            return this.name.replaceFirstChar { it.uppercaseChar() }.replace("_", " ")
        }

        fun List<String>.formatToSetType(): List<SetType> {
            return this.map { it.formatSetType() }
        }

        fun String.formatSetType(): SetType {
            return SetType.valueOf(this.uppercase().replace(" ", "_"))
        }
    }
}
