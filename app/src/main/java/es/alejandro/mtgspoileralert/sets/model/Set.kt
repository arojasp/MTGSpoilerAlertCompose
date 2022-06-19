package es.alejandro.mtgspoileralert.sets.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sets")
data class Set(
    val arena_code: String?,
    val block: String?,
    val block_code: String?,
    val card_count: Int = 0,
    val code: String,
    val digital: Boolean?,
    val foil_only: Boolean?,
    val icon_svg_uri: String,
    @PrimaryKey
    val id: String,
    val mtgo_code: String?,
    val name: String,
    val nonfoil_only: Boolean?,
    val `object`: String?,
    val parent_set_code: String?,
    val released_at: String?,
    val scryfall_uri: String?,
    val search_uri: String?,
    val set_type: SetType?,
    val tcgplayer_id: Int?,
    val uri: String?
)
