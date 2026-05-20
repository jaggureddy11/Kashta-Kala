package com.example.kashtakala.data.repository

import com.example.kashtakala.data.db.PortfolioDao
import com.example.kashtakala.data.model.PortfolioItem
import kotlinx.coroutines.flow.Flow

class PortfolioRepository(private val portfolioDao: PortfolioDao) {

    val allItems: Flow<List<PortfolioItem>> = portfolioDao.getAllItems()

    suspend fun insert(item: PortfolioItem) {
        portfolioDao.insertItem(item)
    }

    suspend fun delete(item: PortfolioItem) {
        portfolioDao.deleteItem(item)
    }
}