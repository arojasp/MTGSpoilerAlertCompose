package es.alejandro.mtgspoileralert.detail.model

data class CardResponse(
    val artist: String,
    val artist_ids: List<String>,
    val booster: Boolean,
    val border_color: String,
    val card_back_id: String,
    val card_faces: List<CardFace>,
    val cardmarket_id: Int,
    val cmc: Double,
    val collector_number: String,
    val color_identity: List<String>,
    val colors: List<String>,
    val digital: Boolean,
    val edhrec_rank: Int,
    val finishes: List<String>,
    val foil: Boolean,
    val frame: String,
    val full_art: Boolean,
    val games: List<String>,
    val highres_image: Boolean,
    val id: String,
    val illustration_id: String,
    val image_status: String,
    val image_uris: ImageUris,
    val keywords: List<String>,
    val lang: String,
    val layout: String,
    val legalities: Legalities,
    val mana_cost: String,
    val multiverse_ids: List<Int>,
    val name: String,
    val nonfoil: Boolean,
    val `object`: String,
    val oracle_id: String,
    val oversized: Boolean,
    val penny_rank: Int,
    val prices: Prices,
    val prints_search_uri: String,
    val promo: Boolean,
    val purchase_uris: PurchaseUris,
    val rarity: String,
    val related_uris: RelatedUris,
    val released_at: String,
    val reprint: Boolean,
    val reserved: Boolean,
    val rulings_uri: String,
    val scryfall_set_uri: String,
    val scryfall_uri: String,
    val security_stamp: String,
    val `set`: String,
    val set_id: String,
    val set_name: String,
    val set_search_uri: String,
    val set_type: String,
    val set_uri: String,
    val story_spotlight: Boolean,
    val tcgplayer_id: Int,
    val textless: Boolean,
    val type_line: String,
    val uri: String,
    val variation: Boolean
)