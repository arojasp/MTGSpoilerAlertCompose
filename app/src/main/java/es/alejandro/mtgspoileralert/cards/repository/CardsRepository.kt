package es.alejandro.mtgspoileralert.cards.repository

import android.util.Log
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
        response = try {
            var hold = service.getCardsForSet(code = codeSet, page = pageNumber)
            holdCardList.addAll(hold.data)

            while (hold.has_more){
                pageNumber++
                hold = service.getCardsForSet(code = codeSet, page = pageNumber)
                holdCardList.addAll(hold.data)
            }
            val properResponse = hold.copy(data = holdCardList)
            dao.saveCards(holdCardList)
            properResponse
        } catch (e: Exception) {
            Log.d("TAG", "${e.message}")
            val storedCards = dao.getCardsForSet(codeSet)
            CardsResponse(storedCards, false, "", "", storedCards.size)
        }

        return response
    }
}