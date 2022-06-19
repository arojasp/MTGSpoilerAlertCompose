package es.alejandro.mtgspoileralert.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.alejandro.mtgspoileralert.cards.repository.CardsRepository
import es.alejandro.mtgspoileralert.cards.repository.ICardsRepository
import es.alejandro.mtgspoileralert.cards.service.ICardsService
import es.alejandro.mtgspoileralert.cards.usecase.GetCardsUseCase
import es.alejandro.mtgspoileralert.cards.usecase.IGetCardsUseCase
import es.alejandro.mtgspoileralert.detail.repository.CardDetailRepository
import es.alejandro.mtgspoileralert.detail.repository.ICardDetailRepository
import es.alejandro.mtgspoileralert.detail.service.ICardDetailService
import es.alejandro.mtgspoileralert.detail.usecase.GetCardDetailUseCase
import es.alejandro.mtgspoileralert.detail.usecase.IGetCardDetailUseCase
import es.alejandro.mtgspoileralert.sets.repository.ISetsRepository
import es.alejandro.mtgspoileralert.sets.repository.SetsRepository
import es.alejandro.mtgspoileralert.sets.service.ISetsService
import es.alejandro.mtgspoileralert.sets.usecase.GetSetsUseCase
import es.alejandro.mtgspoileralert.sets.usecase.IGetSetUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.scryfall.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesSetsService(retrofit: Retrofit): ISetsService {
        return retrofit.create(ISetsService::class.java)
    }

    @Provides
    @Singleton
    fun providesCardsService(retrofit: Retrofit): ICardsService {
        return retrofit.create(ICardsService::class.java)
    }

    @Provides
    @Singleton
    fun providesCardDetailService(retrofit: Retrofit): ICardDetailService {
        return retrofit.create(ICardDetailService::class.java)
    }

    @Provides
    @Singleton
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface AppModuleInt {

        @Binds
        @Singleton
        fun provideSetsRepository(repo: SetsRepository): ISetsRepository

        @Binds
        @Singleton
        fun provideCardsRepository(repo: CardsRepository): ICardsRepository

        @Binds
        @Singleton
        fun provideCardDetailRepository(repo: CardDetailRepository): ICardDetailRepository

        @Binds
        @Singleton
        fun providesSetsUseCase(uc: GetSetsUseCase): IGetSetUseCase

        @Binds
        @Singleton
        fun providesCardsUseCase(uc: GetCardsUseCase): IGetCardsUseCase

        @Binds
        @Singleton
        fun providesCardDetailUseCase(uc: GetCardDetailUseCase): IGetCardDetailUseCase
    }
}
