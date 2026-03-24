package com.example.myapplication1.data.repository

import com.example.myapplication1.domain.model.AdminAlert
import com.example.myapplication1.domain.model.AdminMetric
import com.example.myapplication1.domain.model.BackendEndpoint
import com.example.myapplication1.domain.model.CallSession
import com.example.myapplication1.domain.model.ChatMessage
import com.example.myapplication1.domain.model.Contact
import com.example.myapplication1.domain.model.ConversationSummary
import com.example.myapplication1.domain.model.DeliveryCheckpoint
import com.example.myapplication1.domain.model.SystemProfile
import com.example.myapplication1.domain.model.WorkspaceUpdate

interface CommunicationRepository {
    fun loadProfile(): SystemProfile
    fun loadConversations(): List<ConversationSummary>
    fun loadContacts(): List<Contact>
    fun loadCalls(): List<CallSession>
    fun loadMetrics(): List<AdminMetric>
    fun loadAlerts(): List<AdminAlert>
    fun loadMessages(): Map<String, List<ChatMessage>>
    fun loadCheckpoints(): List<DeliveryCheckpoint>
    fun loadBackendEndpoints(): List<BackendEndpoint>
    fun loadWorkspaceUpdates(): List<WorkspaceUpdate>
}
