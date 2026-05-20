package com.example.kashtakala.data.model

data class FurnitureDesign(
    val id: Int,
    val name: String,
    val category: String,       // "Sofa", "Bed", "Cabinet", "Wardrobe", "Table"
    val imageRes: Int,          // drawable resource ID
    val description: String,
    val suggestedWood: String,
    val estimatedWidth: Float,  // in feet
    val estimatedHeight: Float,
    val estimatedDepth: Float,
    var isFavourite: Boolean = false
)