package es.alejandro.mtgspoileralert.detail.service

import es.alejandro.mtgspoileralert.cards.model.CardsResponse
import es.alejandro.mtgspoileralert.detail.model.CardResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ICardDetailService {
    @GET("cards/{id}")
    suspend fun getCardDetail(
        @Path("id") cardId: String
    ): CardResponse
}