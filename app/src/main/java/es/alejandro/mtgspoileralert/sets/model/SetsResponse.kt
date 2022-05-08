package es.alejandro.mtgspoileralert.sets.model

data class SetsResponse(
    val `data`: List<Set>,
    val has_more: Boolean,
    val `object`: String
)