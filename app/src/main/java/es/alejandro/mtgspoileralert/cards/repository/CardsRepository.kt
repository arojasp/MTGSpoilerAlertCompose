package es.alejandro.mtgspoileralert.cards.repository

import android.util.Log
import es.alejandro.mtgspoileralert.cards.model.Card
import es.alejandro.mtgspoileralert.cards.model.CardsResponse
import es.alejandro.mtgspoileralert.cards.service.ICardsService
import es.alejandro.mtgspoileralert.datastore.SetsDataStoreManager
import es.alejandro.mtgspoileralert.datastore.SettingsDataStoreManager
import es.alejandro.mtgspoileralert.db.MTGSpoilerAlertDao
import es.alejandro.mtgspoileralert.notification.NotificationService
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface ICardsRepository {
    suspend fun getCardsForSet(codeSet: String): CardsResponse
}

class CardsRepository @Inject constructor(
    val service: ICardsService,
    val dao: MTGSpoilerAlertDao,
    val notificationService: NotificationService,
    val setsDataStoreManager: SetsDataStoreManager,
    val settingsDataStoreManager: SettingsDataStoreManager
) : ICardsRepository {
    override suspend fun getCardsForSet(set: String): CardsResponse {
        var pageNumber = 1
        val holdCardList = mutableListOf<Card>()
        val searchCodeSet = "e:$set"
        val response: CardsResponse = try {
            var hold = service.getCardsForSet(code = searchCodeSet, page = pageNumber)
            holdCardList.addAll(hold.data)

            while (hold.has_more) {
                pageNumber++
                hold = service.getCardsForSet(code = searchCodeSet, page = pageNumber)
                holdCardList.addAll(hold.data)
            }

            val languageSelected = settingsDataStoreManager.settings.first().language

            val properResponse = hold.copy(data = holdCardList
                .groupBy { it.oracle_id }
                .flatMap { entryMap ->
                    var filtered = entryMap.value.filter { it.lang == languageSelected }
                    if (filtered.isEmpty())
                        filtered = entryMap.value.filter { it.lang == "en" }
                    filtered
                })

            val thereAreNewItems = dao.insertNewCards(set, properResponse.data)
            if (thereAreNewItems && set == setsDataStoreManager.sets.first()) {
                notificationService.showNewSetsNotification()
            }

            dao.saveCards(holdCardList)
            properResponse
        } catch (e: Exception) {
            Log.e("TAG", "${e.message}")
            val storedCards = dao.getCardsForSet(searchCodeSet)
            CardsResponse(storedCards, false, "", "", storedCards.size)
        }

        return response
    }
}
