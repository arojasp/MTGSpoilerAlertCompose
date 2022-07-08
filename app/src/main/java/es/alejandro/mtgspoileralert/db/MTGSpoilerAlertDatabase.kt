package es.alejandro.mtgspoileralert.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import es.alejandro.mtgspoileralert.cards.model.Card
import es.alejandro.mtgspoileralert.detail.model.CardResponse
import es.alejandro.mtgspoileralert.sets.model.Set

@Database(
    entities = [CardResponse::class, Set::class, Card::class],
    exportSchema = true,
    version = 2,
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ]
)
@TypeConverters(Converters::class)
abstract class MTGSpoilerAlertDatabase : RoomDatabase() {

    abstract fun provideDao(): MTGSpoilerAlertDao
}
