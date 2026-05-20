package com.example.kashtakala.util

object WoodCalculator {

    // Wood types with cost per sq.ft. in rupees
    val woodTypes = listOf("Teak", "Sheesham", "Pine", "MDF", "Plywood")

    val woodCostPerSqFt = mapOf(
        "Teak"     to 180f,
        "Sheesham" to 120f,
        "Pine"     to 80f,
        "MDF"      to 45f,
        "Plywood"  to 55f
    )

    // Area in sq.ft. (length × width)
    fun calculateArea(lengthFt: Float, widthFt: Float): Float {
        return lengthFt * widthFt
    }

    // Volume in cu.ft. (length × width × height)
    fun calculateVolume(lengthFt: Float, widthFt: Float, heightFt: Float): Float {
        return lengthFt * widthFt * heightFt
    }

    // Material cost = area × cost per sq.ft.
    fun calculateMaterialCost(areaSqFt: Float, woodType: String): Float {
        val rate = woodCostPerSqFt[woodType] ?: 80f
        return areaSqFt * rate
    }

    // Final quote = material + labour + overhead%
    fun calculateTotalCost(
        materialCost: Float,
        labourCost: Float,
        overheadPercent: Float
    ): Float {
        val subtotal = materialCost + labourCost
        val overhead = subtotal * (overheadPercent / 100f)
        return subtotal + overhead
    }

    // Validate input dimensions
    fun validateInputs(length: String, width: String, height: String): String? {
        if (length.isBlank() || width.isBlank() || height.isBlank())
            return "Please fill all dimension fields"
        val l = length.toFloatOrNull() ?: return "Length must be a valid number"
        val w = width.toFloatOrNull() ?: return "Width must be a valid number"
        val h = height.toFloatOrNull() ?: return "Height must be a valid number"
        if (l <= 0 || w <= 0 || h <= 0) return "Dimensions must be greater than zero"
        return null // null means valid
    }
}