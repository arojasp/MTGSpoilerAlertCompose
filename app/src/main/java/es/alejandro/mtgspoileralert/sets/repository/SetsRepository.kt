package es.alejandro.mtgspoileralert.sets.repository

import android.util.Log
import es.alejandro.mtgspoileralert.db.MTGSpoilerAlertDao
import es.alejandro.mtgspoileralert.sets.model.Set
import es.alejandro.mtgspoileralert.sets.model.SetType
import es.alejandro.mtgspoileralert.sets.model.SetsResponse
import es.alejandro.mtgspoileralert.sets.service.ISetsService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ISetsRepository {
    suspend fun getAllSets(): SetsResponse
}

class SetsRepository @Inject constructor(
    val service: ISetsService,
    val dao: MTGSpoilerAlertDao,
    val dispatcher: CoroutineDispatcher
): ISetsRepository {
    override suspend fun getAllSets(): SetsResponse {
        return withContext(dispatcher){
            val response = try{
                val hold = service.getAllSets()
                val newList = hold.data.filter {
                    (it.set_type == SetType.EXPANSION || it.set_type == SetType.CORE || it.set_type == SetType.COMMANDER) && it.card_count > 0
                }
                val properResponse = hold.copy(data = newList)
                dao.saveSets(properResponse.data)
                properResponse
            } catch (e: Exception) {
                SetsResponse(dao.getAllSets(), false, "")
            }
            response
        }
    }
}