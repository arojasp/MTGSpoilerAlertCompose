package es.alejandro.mtgspoileralert.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import es.alejandro.mtgspoileralert.cards.model.Card
import es.alejandro.mtgspoileralert.detail.model.CardResponse
import es.alejandro.mtgspoileralert.sets.model.Set
import es.alejandro.mtgspoileralert.sets.model.SetsResponse

@Dao
interface MTGSpoilerAlertDao {

    @Insert
    suspend fun saveCard(card: CardResponse)

    @Query("SELECT * FROM sets")
    suspend fun getAllSets(): List<Set>

    @Insert(onConflict = REPLACE)
    suspend fun saveSets(sets: List<Set>)

    @Query("SELECT * FROM cards WHERE `set` = :set")
    suspend fun getCardsForSet(set: String): List<Card>

    @Insert(onConflict = REPLACE)
    suspend fun saveCards(cards: List<Card>)
}