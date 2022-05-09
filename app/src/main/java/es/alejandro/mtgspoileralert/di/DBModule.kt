package es.alejandro.mtgspoileralert.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.alejandro.mtgspoileralert.db.MTGSpoilerAlertDao
import es.alejandro.mtgspoileralert.db.MTGSpoilerAlertDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext app: Context) = Room.databaseBuilder(
        app,
        MTGSpoilerAlertDatabase::class.java,
        "mtgsa_db"
    ).build()

    @Provides
    @Singleton
    fun provideDBDao(db: MTGSpoilerAlertDatabase): MTGSpoilerAlertDao {
        return db.provideDao()
    }
}