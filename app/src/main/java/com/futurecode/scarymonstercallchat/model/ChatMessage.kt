package com.futurecode.scarymonstercallchat.model

data class ChatMessage(
    val text: String,
    val isFromMonster: Boolean,
    val isTyping: Boolean = false // <-- ADD THIS LINE (Defaults to false)
)