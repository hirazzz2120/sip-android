package com.example.myapplication1.data.repository

import com.example.myapplication1.domain.model.AdminAlert
import com.example.myapplication1.domain.model.AdminMetric
import com.example.myapplication1.domain.model.CallSession
import com.example.myapplication1.domain.model.ChatMessage
import com.example.myapplication1.domain.model.Contact
import com.example.myapplication1.domain.model.ConversationSummary
import com.example.myapplication1.domain.model.SystemProfile

interface CommunicationRepository {
    fun loadProfile(): SystemProfile
    fun loadConversations(): List<ConversationSummary>
    fun loadContacts(): List<Contact>
    fun loadCalls(): List<CallSession>
    fun loadMetrics(): List<AdminMetric>
    fun loadAlerts(): List<AdminAlert>
    fun loadMessages(): Map<String, List<ChatMessage>>
}
