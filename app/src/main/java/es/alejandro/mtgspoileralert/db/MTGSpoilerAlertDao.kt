package es.alejandro.mtgspoileralert.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import androidx.room.Transaction
import es.alejandro.mtgspoileralert.cards.model.Card
import es.alejandro.mtgspoileralert.detail.model.CardResponse
import es.alejandro.mtgspoileralert.sets.model.Set

@Dao
interface MTGSpoilerAlertDao {

    @Insert(onConflict = IGNORE)
    suspend fun saveCard(card: CardResponse)

    @Insert(onConflict = IGNORE)
    suspend fun saveSet(set: Set)

    @Query("SELECT * FROM sets")
    suspend fun getAllSets(): List<Set>

    @Insert(onConflict = IGNORE)
    suspend fun saveSets(sets: List<Set>)

    @Query("SELECT * FROM cards WHERE `set` = :set")
    suspend fun getCardsForSet(set: String): List<Card>

    @Insert(onConflict = IGNORE)
    suspend fun saveCards(cards: List<Card>)

    @Transaction
    suspend fun insertNewCards(setForCards: String, cards: List<Card>): Boolean {
        var thereAreNewItems = false
        val existingCardsForSet = getCardsForSet(setForCards)
        if (cards.size > existingCardsForSet.size)
            thereAreNewItems = true
        saveCards(cards)
        return thereAreNewItems
    }
}
