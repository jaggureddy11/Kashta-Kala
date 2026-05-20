package com.example.kashtakala.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_designs")
data class FavouriteDesign(
    @PrimaryKey
    val designId: Int           // matches FurnitureDesign.id
)
