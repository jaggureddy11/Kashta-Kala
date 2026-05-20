package com.example.kashtakala.ui

import androidx.lifecycle.ViewModel
import com.example.kashtakala.data.model.FurnitureDesign
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedViewModel : ViewModel() {
    private val _selectedDesignForEstimation = MutableStateFlow<FurnitureDesign?>(null)
    val selectedDesignForEstimation = _selectedDesignForEstimation.asStateFlow()

    private val _selectedDesignForQuote = MutableStateFlow<FurnitureDesign?>(null)
    val selectedDesignForQuote = _selectedDesignForQuote.asStateFlow()

    fun selectDesignForEstimation(design: FurnitureDesign) {
        _selectedDesignForEstimation.value = design
    }

    fun clearEstimationSelection() {
        _selectedDesignForEstimation.value = null
    }

    fun selectDesignForQuote(design: FurnitureDesign) {
        _selectedDesignForQuote.value = design
    }

    fun clearQuoteSelection() {
        _selectedDesignForQuote.value = null
    }
}
