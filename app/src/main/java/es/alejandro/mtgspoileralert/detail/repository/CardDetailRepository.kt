package es.alejandro.mtgspoileralert.detail.repository

import es.alejandro.mtgspoileralert.detail.model.CardResponse
import es.alejandro.mtgspoileralert.detail.service.ICardDetailService
import javax.inject.Inject

interface ICardDetailRepository {
    suspend fun getCardDetail(cardId: String): CardResponse
}

class CardDetailRepository @Inject constructor(
        val service: ICardDetailService
): ICardDetailRepository {
    override suspend fun getCardDetail(cardId: String): CardResponse {
        return service.getCardDetail(cardId)
    }
}