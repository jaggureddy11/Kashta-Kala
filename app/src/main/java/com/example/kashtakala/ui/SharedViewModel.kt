package com.example.kashtakala.ui

import androidx.lifecycle.ViewModel
import com.example.kashtakala.data.model.FurnitureDesign
import com.example.kashtakala.data.ChatMessage
import com.example.kashtakala.util.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedViewModel : ViewModel() {
    private val _selectedLanguage = MutableStateFlow(Language.ENGLISH)
    val selectedLanguage = _selectedLanguage.asStateFlow()

    fun setLanguage(lang: Language) {
        _selectedLanguage.value = lang
    }

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = "model",
                text = "Namaste! I am your Kashta-Kala AI carpentry assistant. Ask me anything about furniture designs, wood types, measurements, carpentry tips, or how to use the app!"
            )
        )
    )
    val chatMessages = _chatMessages.asStateFlow()

    fun addChatMessage(sender: String, text: String) {
        val current = _chatMessages.value.toMutableList()
        current.add(ChatMessage(sender, text))
        _chatMessages.value = current
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                sender = "model",
                text = "Namaste! I am your Kashta-Kala AI carpentry assistant. Ask me anything about furniture designs, wood types, measurements, carpentry tips, or how to use the app!"
            )
        )
    }

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
