package es.alejandro.mtgspoileralert.detail.model

data class CardFace(
    val artist: String,
    val artist_id: String,
    val flavor_name: String,
    val illustration_id: String?,
    val mana_cost: String?,
    val name: String,
    val `object`: String,
    val oracle_text: String? = "",
    val type_line: String
)
