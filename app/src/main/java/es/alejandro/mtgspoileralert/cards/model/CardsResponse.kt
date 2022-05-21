package es.alejandro.mtgspoileralert.cards.model

data class CardsResponse(
    val `data`: List<Card>,
    val has_more: Boolean,
    val next_page: String?,
    val `object`: String,
    val total_cards: Int
)