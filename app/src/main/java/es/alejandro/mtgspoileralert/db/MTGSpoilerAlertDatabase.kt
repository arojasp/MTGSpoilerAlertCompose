package es.alejandro.mtgspoileralert.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import es.alejandro.mtgspoileralert.cards.model.Card
import es.alejandro.mtgspoileralert.detail.model.CardResponse
import es.alejandro.mtgspoileralert.sets.model.Set
import es.alejandro.mtgspoileralert.sets.model.SetsResponse

@Database(
    entities = [CardResponse::class, Set::class, Card::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MTGSpoilerAlertDatabase : RoomDatabase() {

    abstract fun provideDao(): MTGSpoilerAlertDao
}