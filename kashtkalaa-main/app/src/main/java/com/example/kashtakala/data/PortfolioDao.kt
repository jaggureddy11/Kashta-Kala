package com.example.kashtakala.data.db

import androidx.room.*
import com.example.kashtakala.data.model.PortfolioItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: PortfolioItem)

    @Query("SELECT * FROM portfolio_items ORDER BY dateAdded DESC")
    fun getAllItems(): Flow<List<PortfolioItem>>

    @Delete
    suspend fun deleteItem(item: PortfolioItem)
}