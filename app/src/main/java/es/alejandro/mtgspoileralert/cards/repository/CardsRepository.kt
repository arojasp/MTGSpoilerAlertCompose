package es.alejandro.mtgspoileralert.cards.repository

import es.alejandro.mtgspoileralert.cards.model.CardsResponse
import es.alejandro.mtgspoileralert.cards.service.ICardsService
import es.alejandro.mtgspoileralert.db.MTGSpoilerAlertDao
import javax.inject.Inject

interface ICardsRepository {
    suspend fun getCardsForSet(codeSet: String): CardsResponse
}
class CardsRepository @Inject constructor(
    val service: ICardsService,
    val dao: MTGSpoilerAlertDao
): ICardsRepository{
    override suspend fun getCardsForSet(codeSet: String): CardsResponse {
        val response = service.getCardsForSet(code = codeSet)
        return response
    }
}