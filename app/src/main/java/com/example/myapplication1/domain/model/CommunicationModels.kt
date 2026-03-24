package com.example.myapplication1.domain.model

data class SystemProfile(
    val currentUserName: String,
    val sipAccount: String,
    val gateway: String,
    val peerClient: String,
    val environmentLabel: String,
    val backendBaseUrl: String,
    val releaseStage: String
)

data class ConversationSummary(
    val id: String,
    val title: String,
    val preview: String,
    val kind: MessageKind,
    val unreadCount: Int,
    val timestamp: String,
    val isGroup: Boolean,
    val memberCount: Int,
    val routingKey: String,
    val integrationStatus: ReadinessStatus
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
    val capability: String,
    val roleTags: List<String>,
    val responseWindow: String
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
    val durationLabel: String,
    val scenarioLabel: String,
    val nextAction: String,
    val networkScore: Float
)

enum class CallDirection {
    INCOMING,
    OUTGOING
}

enum class CallState {
    READY,
    RINGING,
    LIVE,
    SCHEDULED,
    LAST_COMPLETED
}

data class AdminMetric(
    val title: String,
    val value: String,
    val insight: String,
    val target: String
)

data class AdminAlert(
    val title: String,
    val detail: String,
    val severity: AlertSeverity
)

enum class AlertSeverity {
    INFO,
    WARNING,
    CRITICAL
}

enum class ReadinessStatus {
    READY,
    IN_PROGRESS,
    BLOCKED,
    TODO
}

data class DeliveryCheckpoint(
    val title: String,
    val owner: String,
    val detail: String,
    val status: ReadinessStatus
)

data class BackendEndpoint(
    val id: String,
    val name: String,
    val method: String,
    val path: String,
    val purpose: String,
    val payloadHint: String,
    val status: ReadinessStatus
)

data class WorkspaceUpdate(
    val title: String,
    val detail: String,
    val timeLabel: String,
    val status: ReadinessStatus
)
