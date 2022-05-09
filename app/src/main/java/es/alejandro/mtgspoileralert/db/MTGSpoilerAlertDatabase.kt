package es.alejandro.mtgspoileralert.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import es.alejandro.mtgspoileralert.detail.model.CardResponse

@Database(
    entities = [CardResponse::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MTGSpoilerAlertDatabase : RoomDatabase() {

    abstract fun provideDao(): MTGSpoilerAlertDao
}