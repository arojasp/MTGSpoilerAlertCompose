package es.alejandro.mtgspoileralert.sets.repository

import es.alejandro.mtgspoileralert.sets.model.SetsResponse
import es.alejandro.mtgspoileralert.sets.service.ISetsService
import javax.inject.Inject

interface ISetsRepository {
    suspend fun getAllSets(): SetsResponse
}

class SetsRepository @Inject constructor(
    val service: ISetsService
): ISetsRepository {
    override suspend fun getAllSets(): SetsResponse {
        return service.getAllSets()
    }
}