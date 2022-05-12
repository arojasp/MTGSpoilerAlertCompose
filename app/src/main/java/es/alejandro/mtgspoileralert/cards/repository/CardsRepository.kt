package es.alejandro.mtgspoileralert.cards.repository

import es.alejandro.mtgspoileralert.cards.model.Card
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
        var pageNumber = 1
        val holdCardList = mutableListOf<Card>()
        var response: CardsResponse
        response = service.getCardsForSet(code = codeSet, page = pageNumber)
        holdCardList.addAll(response.data)

        while (response.has_more){
            pageNumber++
            response = service.getCardsForSet(code = codeSet, page = pageNumber)
            holdCardList.addAll(response.data)
        }
        return response
    }
}