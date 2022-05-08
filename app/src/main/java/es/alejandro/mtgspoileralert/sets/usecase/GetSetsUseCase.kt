package es.alejandro.mtgspoileralert.sets.usecase

import es.alejandro.mtgspoileralert.sets.model.SetsResponse
import es.alejandro.mtgspoileralert.sets.repository.ISetsRepository
import javax.inject.Inject

interface IGetSetUseCase {
    suspend operator fun invoke(): SetsResponse
}

class GetSetsUseCase @Inject constructor(
    val repository: ISetsRepository
): IGetSetUseCase {

    override suspend fun invoke(): SetsResponse {
        return repository.getAllSets()
    }
}