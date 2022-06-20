package es.alejandro.mtgspoileralert.cards.usecase

import es.alejandro.mtgspoileralert.cards.model.CardsResponse
import es.alejandro.mtgspoileralert.cards.repository.ICardsRepository
import javax.inject.Inject

interface IGetCardsUseCase {
    suspend operator fun invoke(setCode: String): CardsResponse
}

class GetCardsUseCase @Inject constructor(
    val repository: ICardsRepository
) : IGetCardsUseCase {
    override suspend fun invoke(setCode: String): CardsResponse {
        return repository.getCardsForSet(setCode)
    }
}
