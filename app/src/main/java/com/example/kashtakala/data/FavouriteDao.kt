package com.example.kashtakala.data.db

import androidx.room.*
import com.example.kashtakala.data.model.FavouriteDesign
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavourite(favourite: FavouriteDesign)

    @Delete
    suspend fun removeFavourite(favourite: FavouriteDesign)

    @Query("SELECT * FROM favourite_designs")
    fun getAllFavourites(): Flow<List<FavouriteDesign>>

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_designs WHERE designId = :id)")
    suspend fun isFavourite(id: Int): Boolean
}