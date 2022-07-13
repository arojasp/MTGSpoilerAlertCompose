package es.alejandro.mtgspoileralert.sets.repository

import es.alejandro.mtgspoileralert.datastore.SettingsDataStoreManager
import es.alejandro.mtgspoileralert.db.MTGSpoilerAlertDao
import es.alejandro.mtgspoileralert.sets.model.SetType
import es.alejandro.mtgspoileralert.sets.model.SetsResponse
import es.alejandro.mtgspoileralert.sets.service.ISetsService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ISetsRepository {
    suspend fun getAllSets(): SetsResponse
}

class SetsRepository @Inject constructor(
    val service: ISetsService,
    val dao: MTGSpoilerAlertDao,
    val dispatcher: CoroutineDispatcher,
    val settingsDataStoreManager: SettingsDataStoreManager
) : ISetsRepository {
    override suspend fun getAllSets(): SetsResponse {
        return withContext(dispatcher) {
            val response = try {
                val hold = service.getAllSets()
                val enabledSetTypes = settingsDataStoreManager.settings.first()
                val newList = hold.data.filter {
                    enabledSetTypes.setTypes.contains(it.set_type) && it.card_count > 0
                }
                val properResponse = hold.copy(data = newList)
                properResponse
            } catch (e: Exception) {
                SetsResponse(dao.getAllSets(), false, "")
            }
            response
        }
    }
}
