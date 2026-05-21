package com.example.kashtakala.data

data class ChatMessage(
    val sender: String, // "user" or "model"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
