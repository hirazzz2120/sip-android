package com.example.myapplication1.domain.model

data class SystemProfile(
    val currentUserName: String,
    val sipAccount: String,
    val gateway: String,
    val peerClient: String
)

data class ConversationSummary(
    val id: String,
    val title: String,
    val preview: String,
    val kind: MessageKind,
    val unreadCount: Int,
    val timestamp: String,
    val isGroup: Boolean
)

data class ChatMessage(
    val id: String,
    val author: String,
    val authorRole: AuthorRole,
    val kind: MessageKind,
    val body: String,
    val timestamp: String,
    val deliveryStatus: MessageDeliveryStatus = MessageDeliveryStatus.DELIVERED
)

enum class AuthorRole {
    SELF,
    REMOTE
}

enum class MessageKind {
    TEXT,
    IMAGE,
    AUDIO,
    VIDEO
}

enum class MessageDeliveryStatus {
    LOCAL,
    SENDING,
    SENT,
    DELIVERED,
    FAILED
}

data class Contact(
    val id: String,
    val name: String,
    val department: String,
    val status: ContactStatus,
    val capability: String
)

enum class ContactStatus {
    ONLINE,
    BUSY,
    OFFLINE
}

data class CallSession(
    val id: String,
    val title: String,
    val participants: Int,
    val direction: CallDirection,
    val state: CallState,
    val durationLabel: String
)

enum class CallDirection {
    INCOMING,
    OUTGOING
}

enum class CallState {
    READY,
    SCHEDULED,
    LAST_COMPLETED
}

data class AdminMetric(
    val title: String,
    val value: String,
    val insight: String
)

data class AdminAlert(
    val title: String,
    val detail: String
)
