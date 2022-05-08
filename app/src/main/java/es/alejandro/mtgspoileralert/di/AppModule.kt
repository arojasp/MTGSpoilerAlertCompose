package es.alejandro.mtgspoileralert.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import es.alejandro.mtgspoileralert.sets.repository.ISetsRepository
import es.alejandro.mtgspoileralert.sets.repository.SetsRepository
import es.alejandro.mtgspoileralert.sets.service.ISetsService
import es.alejandro.mtgspoileralert.sets.usecase.GetSetsUseCase
import es.alejandro.mtgspoileralert.sets.usecase.IGetSetUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
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

    @Module
    @InstallIn(SingletonComponent::class)
    interface AppModuleInt {

        @Binds
        @Singleton
        fun provideSetsRepository(repo: SetsRepository): ISetsRepository

        @Binds
        @Singleton
        fun providesSetsUseCase(uc: GetSetsUseCase): IGetSetUseCase

    }

}