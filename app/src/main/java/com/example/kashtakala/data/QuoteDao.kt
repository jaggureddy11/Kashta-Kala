package com.example.kashtakala.data.db

import androidx.room.*
import com.example.kashtakala.data.model.SavedQuote
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: SavedQuote)

    @Query("SELECT * FROM saved_quotes ORDER BY dateCreated DESC")
    fun getAllQuotes(): Flow<List<SavedQuote>>

    @Delete
    suspend fun deleteQuote(quote: SavedQuote)

    @Query("DELETE FROM saved_quotes WHERE id = :quoteId")
    suspend fun deleteById(quoteId: Int)
}