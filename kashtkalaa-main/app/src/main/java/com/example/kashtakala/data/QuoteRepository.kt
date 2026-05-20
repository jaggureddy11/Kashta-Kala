package com.example.kashtakala.data.repository

import com.example.kashtakala.data.db.QuoteDao
import com.example.kashtakala.data.model.SavedQuote
import kotlinx.coroutines.flow.Flow

class QuoteRepository(private val quoteDao: QuoteDao) {

    val allQuotes: Flow<List<SavedQuote>> = quoteDao.getAllQuotes()

    suspend fun insert(quote: SavedQuote) {
        quoteDao.insertQuote(quote)
    }

    suspend fun delete(quote: SavedQuote) {
        quoteDao.deleteQuote(quote)
    }

    suspend fun deleteById(id: Int) {
        quoteDao.deleteById(id)
    }
}