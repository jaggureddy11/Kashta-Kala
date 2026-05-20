package com.example.kashtakala.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolio_items")
data class PortfolioItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imagePath: String,      // absolute path in internal storage
    val caption: String = "",
    val dateAdded: Long = System.currentTimeMillis()
)