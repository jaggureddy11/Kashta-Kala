package com.example.kashtakala.data.repository

import com.example.kashtakala.data.db.FavouriteDao
import com.example.kashtakala.data.model.FavouriteDesign
import kotlinx.coroutines.flow.Flow

class FavouriteRepository(private val favouriteDao: FavouriteDao) {

    val allFavourites: Flow<List<FavouriteDesign>> = favouriteDao.getAllFavourites()

    suspend fun add(favourite: FavouriteDesign) {
        favouriteDao.addFavourite(favourite)
    }

    suspend fun remove(favourite: FavouriteDesign) {
        favouriteDao.removeFavourite(favourite)
    }

    suspend fun isFavourite(id: Int): Boolean {
        return favouriteDao.isFavourite(id)
    }
}