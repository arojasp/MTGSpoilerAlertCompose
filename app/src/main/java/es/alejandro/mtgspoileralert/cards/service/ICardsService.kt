package es.alejandro.mtgspoileralert.cards.service

import es.alejandro.mtgspoileralert.cards.model.CardsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ICardsService {
    @GET("cards/search")
    suspend fun getCardsForSet(
        @Query("order") set: String = "set",
        @Query("q") code: String,
        @Query("unique") unique: String = "prints",
        @Query("order") order: String = "spoiled",
        @Query("include_extras") include_extras: Boolean = false,
        @Query("page") page: Int
    ): CardsResponse
}
