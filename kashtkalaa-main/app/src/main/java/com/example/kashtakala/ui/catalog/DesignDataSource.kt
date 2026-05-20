package com.example.kashtakala.ui.catalog

import com.example.kashtakala.R
import com.example.kashtakala.data.model.FurnitureDesign

object DesignDataSource {

    fun getAllDesigns(): List<FurnitureDesign> = listOf(

        // SOFAS
        FurnitureDesign(
            id = 1, name = "Modern L-Sofa",
            category = "Sofa",
            imageRes = R.drawable.sofa_modern,
            description = "Contemporary L-shaped sofa with clean lines, perfect for modern living rooms.",
            suggestedWood = "Sheesham",
            estimatedWidth = 8f, estimatedHeight = 3f, estimatedDepth = 3.5f
        ),
        FurnitureDesign(
            id = 2, name = "Classic 3-Seater",
            category = "Sofa",
            imageRes = R.drawable.sofa_classic,
            description = "Traditional 3-seater sofa with carved wooden frame.",
            suggestedWood = "Teak",
            estimatedWidth = 7f, estimatedHeight = 3f, estimatedDepth = 3f
        ),

        // BEDS
        FurnitureDesign(
            id = 3, name = "King Platform Bed",
            category = "Bed",
            imageRes = R.drawable.bed_king,
            description = "Low-profile platform bed with storage drawers underneath.",
            suggestedWood = "Teak",
            estimatedWidth = 6.5f, estimatedHeight = 1.5f, estimatedDepth = 7f
        ),
        FurnitureDesign(
            id = 4, name = "Queen Poster Bed",
            category = "Bed",
            imageRes = R.drawable.bed_queen,
            description = "Four-poster queen bed with decorative headboard.",
            suggestedWood = "Sheesham",
            estimatedWidth = 5.5f, estimatedHeight = 6f, estimatedDepth = 6.5f
        ),

        // CABINETS
        FurnitureDesign(
            id = 5, name = "TV Cabinet",
            category = "Cabinet",
            imageRes = R.drawable.cabinet_tv,
            description = "Wall-mounted TV unit with floating shelves.",
            suggestedWood = "MDF",
            estimatedWidth = 6f, estimatedHeight = 2f, estimatedDepth = 1.5f
        ),
        FurnitureDesign(
            id = 6, name = "Kitchen Cabinet",
            category = "Cabinet",
            imageRes = R.drawable.cabinet_kitchen,
            description = "Modular kitchen cabinet with soft-close hinges.",
            suggestedWood = "Plywood",
            estimatedWidth = 4f, estimatedHeight = 7f, estimatedDepth = 2f
        ),

        // WARDROBES
        FurnitureDesign(
            id = 7, name = "3-Door Wardrobe",
            category = "Wardrobe",
            imageRes = R.drawable.wardrobe_three_door,
            description = "Spacious 3-door wardrobe with mirror and internal shelves.",
            suggestedWood = "Plywood",
            estimatedWidth = 6f, estimatedHeight = 7.5f, estimatedDepth = 2f
        ),
        FurnitureDesign(
            id = 8, name = "Sliding Wardrobe",
            category = "Wardrobe",
            imageRes = R.drawable.wardrobe_sliding,
            description = "Space-saving sliding door wardrobe with glass panels.",
            suggestedWood = "MDF",
            estimatedWidth = 8f, estimatedHeight = 8f, estimatedDepth = 2f
        ),

        // TABLES
        FurnitureDesign(
            id = 9, name = "Dining Table 6-Seater",
            category = "Table",
            imageRes = R.drawable.table_dining,
            description = "Solid wood dining table with turned legs.",
            suggestedWood = "Teak",
            estimatedWidth = 5f, estimatedHeight = 2.5f, estimatedDepth = 3f
        ),
        FurnitureDesign(
            id = 10, name = "Study Desk",
            category = "Table",
            imageRes = R.drawable.table_study,
            description = "L-shaped study desk with overhead shelves.",
            suggestedWood = "Pine",
            estimatedWidth = 4f, estimatedHeight = 2.5f, estimatedDepth = 2f
        ),
    )

    val categories = listOf("All", "Sofa", "Bed", "Cabinet", "Wardrobe", "Table")
}


