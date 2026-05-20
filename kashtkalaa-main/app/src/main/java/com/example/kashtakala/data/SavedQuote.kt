package com.example.kashtakala.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_quotes")
data class SavedQuote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val customerName: String,
    val designName: String,
    val woodType: String,
    val lengthFt: Float,
    val widthFt: Float,
    val heightFt: Float,
    val areaSqFt: Float,
    val materialCost: Float,
    val labourCost: Float,
    val overheadPercent: Float,
    val totalCost: Float,
    val dateCreated: Long = System.currentTimeMillis()
)