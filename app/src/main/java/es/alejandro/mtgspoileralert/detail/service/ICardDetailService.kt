package es.alejandro.mtgspoileralert.detail.service

import es.alejandro.mtgspoileralert.detail.model.CardResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ICardDetailService {
    @GET("cards/{id}")
    suspend fun getCardDetail(
        @Path("id") cardId: String
    ): CardResponse
}
