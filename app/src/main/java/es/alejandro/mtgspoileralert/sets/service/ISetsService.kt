package es.alejandro.mtgspoileralert.sets.service

import es.alejandro.mtgspoileralert.sets.model.SetsResponse
import retrofit2.http.GET

interface ISetsService {

    @GET("sets")
    suspend fun getAllSets(): SetsResponse
}
