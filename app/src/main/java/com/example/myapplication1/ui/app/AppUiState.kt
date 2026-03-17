package com.example.myapplication1.ui.app

import com.example.myapplication1.domain.model.AdminAlert
import com.example.myapplication1.domain.model.AdminMetric
import com.example.myapplication1.domain.model.CallSession
import com.example.myapplication1.domain.model.ChatMessage
import com.example.myapplication1.domain.model.Contact
import com.example.myapplication1.domain.model.ConversationSummary
import com.example.myapplication1.domain.model.SystemProfile

data class AppUiState(
    val isAuthenticated: Boolean = false,
    val profile: SystemProfile? = null,
    val conversations: List<ConversationSummary> = emptyList(),
    val contacts: List<Contact> = emptyList(),
    val calls: List<CallSession> = emptyList(),
    val metrics: List<AdminMetric> = emptyList(),
    val alerts: List<AdminAlert> = emptyList(),
    val messages: Map<String, List<ChatMessage>> = emptyMap(),
    val messageDraft: String = "",
    val transportStatus: String = "本地模拟传输未启动",
    val selectedConversationId: String? = null
) {
    val selectedConversation: ConversationSummary?
        get() = conversations.firstOrNull { it.id == selectedConversationId }

    val selectedMessages: List<ChatMessage>
        get() = selectedConversationId?.let { messages[it] }.orEmpty()
}
