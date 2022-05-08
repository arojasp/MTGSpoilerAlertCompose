package es.alejandro.mtgspoileralert.cards.repository

import es.alejandro.mtgspoileralert.cards.model.CardsResponse
import es.alejandro.mtgspoileralert.cards.service.ICardsService
import javax.inject.Inject

interface ICardsRepository {
    suspend fun getCardsForSet(codeSet: String): CardsResponse
}
class CardsRepository @Inject constructor(
    val service: ICardsService
): ICardsRepository{
    override suspend fun getCardsForSet(codeSet: String): CardsResponse {
        return service.getCardsForSet(code = codeSet)
    }
}