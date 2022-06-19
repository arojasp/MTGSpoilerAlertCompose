package es.alejandro.mtgspoileralert.detail.usecase

import es.alejandro.mtgspoileralert.detail.model.CardResponse
import es.alejandro.mtgspoileralert.detail.repository.ICardDetailRepository
import javax.inject.Inject

interface IGetCardDetailUseCase {
    suspend operator fun invoke(cardId: String): CardResponse
}

class GetCardDetailUseCase @Inject constructor(
    val repository: ICardDetailRepository
) : IGetCardDetailUseCase {
    override suspend fun invoke(cardId: String): CardResponse {
        return repository.getCardDetail(cardId)
    }
}
