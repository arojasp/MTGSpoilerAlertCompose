package es.alejandro.mtgspoileralert.cards.repository

import android.util.Log
import es.alejandro.mtgspoileralert.cards.model.Card
import es.alejandro.mtgspoileralert.cards.model.CardsResponse
import es.alejandro.mtgspoileralert.cards.service.ICardsService
import es.alejandro.mtgspoileralert.datastore.SetsDataStoreManager
import es.alejandro.mtgspoileralert.datastore.SettingsDataStoreManager
import es.alejandro.mtgspoileralert.db.MTGSpoilerAlertDao
import es.alejandro.mtgspoileralert.notification.NotificationService
import javax.inject.Inject

interface ICardsRepository {
    suspend fun getCardsForSet(codeSet: String): CardsResponse
}

class CardsRepository @Inject constructor(
    val service: ICardsService,
    val dao: MTGSpoilerAlertDao,
    val notificationService: NotificationService
) : ICardsRepository {
    override suspend fun getCardsForSet(set: String): CardsResponse {
        var pageNumber = 1
        val holdCardList = mutableListOf<Card>()
        val codeSet = "e:$set"
        var response: CardsResponse = try {
            var hold = service.getCardsForSet(code = codeSet, page = pageNumber)
            holdCardList.addAll(hold.data)

            while (hold.has_more) {
                pageNumber++
                hold = service.getCardsForSet(code = codeSet, page = pageNumber)
                holdCardList.addAll(hold.data)
            }
            val properResponse = hold.copy(data = holdCardList)

            val thereAreNewItems = dao.insertNewCards(set, properResponse.data)
            if (thereAreNewItems) {
                notificationService.showNewSetsNotification()
            }

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
