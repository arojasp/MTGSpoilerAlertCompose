package es.alejandro.mtgspoileralert.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import es.alejandro.mtgspoileralert.cards.model.Card
import es.alejandro.mtgspoileralert.detail.model.CardResponse

@Dao
interface MTGSpoilerAlertDao {

    @Query("SELECT * FROM card_details")
    suspend fun getListOfStrings(): List<CardResponse>

    @Insert
    suspend fun saveCard(card: CardResponse)
}