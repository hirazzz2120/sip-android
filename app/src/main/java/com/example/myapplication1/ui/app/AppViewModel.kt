package com.example.myapplication1.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication1.data.repository.CommunicationRepository
import com.example.myapplication1.data.repository.FakeCommunicationRepository
import com.example.myapplication1.data.transport.ChatTransport
import com.example.myapplication1.data.transport.FakeChatTransport
import com.example.myapplication1.data.transport.IncomingTransportMessage
import com.example.myapplication1.data.transport.InteropMessagePayload
import com.example.myapplication1.data.transport.TransportAck
import com.example.myapplication1.domain.model.AuthorRole
import com.example.myapplication1.domain.model.ChatMessage
import com.example.myapplication1.domain.model.ConversationSummary
import com.example.myapplication1.domain.model.MessageDeliveryStatus
import com.example.myapplication1.domain.model.MessageKind
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    private val repository: CommunicationRepository = FakeCommunicationRepository(),
    private val transport: ChatTransport = FakeChatTransport()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun enterWorkspace() {
        val conversations = repository.loadConversations()
        _uiState.value = AppUiState(
            isAuthenticated = true,
            profile = repository.loadProfile(),
            conversations = conversations,
            contacts = repository.loadContacts(),
            calls = repository.loadCalls(),
            metrics = repository.loadMetrics(),
            alerts = repository.loadAlerts(),
            messages = repository.loadMessages(),
            transportStatus = "本地模拟传输已连接，可替换为真实 WebSocket/HTTP 服务。",
            selectedConversationId = conversations.firstOrNull()?.id
        )
    }

    fun selectConversation(conversationId: String) {
        _uiState.update { state ->
            state.copy(
                selectedConversationId = conversationId,
                messageDraft = "",
                transportStatus = "已切换到会话：${state.conversations.firstOrNull { it.id == conversationId }?.title ?: "未知会话"}"
            )
        }
    }

    fun updateDraft(draft: String) {
        _uiState.update { state ->
            state.copy(messageDraft = draft)
        }
    }

    fun sendTextMessage() {
        val state = _uiState.value
        val draft = state.messageDraft.trim()
        if (draft.isEmpty()) {
            return
        }
        startOutboundMessage(kind = MessageKind.TEXT, body = draft, clearDraft = true)
    }

    fun sendMediaPlaceholder(kind: MessageKind) {
        startOutboundMessage(
            kind = kind,
            body = when (kind) {
                MessageKind.TEXT -> "本地文本占位"
                MessageKind.IMAGE -> "已选择图片：机房拓扑图.png"
                MessageKind.AUDIO -> "已录制语音：语音消息.m4a"
                MessageKind.VIDEO -> "已选择视频：联调演示.mp4"
            },
            clearDraft = false
        )
    }

    private fun startOutboundMessage(
        kind: MessageKind,
        body: String,
        clearDraft: Boolean
    ) {
        val snapshot = _uiState.value
        val payload = buildPayload(snapshot, kind, body) ?: return
        _uiState.update { state ->
            appendPendingMessage(
                state = state,
                payload = payload,
                clearDraft = clearDraft
            )
        }

        viewModelScope.launch {
            runCatching {
                val ack = transport.send(payload)
                val reply = transport.awaitIncomingReply(payload)
                ack to reply
            }.onSuccess { (ack, reply) ->
                _uiState.update { state ->
                    applyAck(state, payload.conversationId, ack)
                }
                _uiState.update { state ->
                    applyIncomingReply(state, reply)
                }
            }.onFailure {
                _uiState.update { state ->
                    markFailed(
                        state = state,
                        conversationId = payload.conversationId,
                        messageId = payload.messageId,
                        reason = "发送失败：${it.message ?: "未知错误"}"
                    )
                }
            }
        }
    }

    private fun buildPayload(
        state: AppUiState,
        kind: MessageKind,
        body: String
    ): InteropMessagePayload? {
        val conversation = state.selectedConversation ?: return null
        return InteropMessagePayload(
            messageId = "msg-${System.currentTimeMillis()}",
            conversationId = conversation.id,
            senderId = state.profile?.sipAccount ?: "sip:android@local",
            senderClient = state.profile?.currentUserName ?: "安卓客户端",
            receiverLabel = conversation.title,
            kind = kind,
            content = body,
            timestampLabel = "刚刚"
        )
    }

    private fun appendPendingMessage(
        state: AppUiState,
        payload: InteropMessagePayload,
        clearDraft: Boolean
    ): AppUiState {
        val existingMessages = state.messages[payload.conversationId].orEmpty()
        val pendingMessage = ChatMessage(
            id = payload.messageId,
            author = payload.senderClient,
            authorRole = AuthorRole.SELF,
            kind = payload.kind,
            body = payload.content,
            timestamp = payload.timestampLabel,
            deliveryStatus = MessageDeliveryStatus.SENDING
        )
        return state.copy(
            conversations = state.conversations.updateConversationSummary(
                conversationId = payload.conversationId,
                preview = "我：${payload.content}",
                kind = payload.kind,
                timestamp = "刚刚",
                unreadCount = 0
            ),
            messages = state.messages + (payload.conversationId to (existingMessages + pendingMessage)),
            messageDraft = if (clearDraft) "" else state.messageDraft,
            transportStatus = "正在发送${payload.kind.toChineseLabel()}消息到 ${payload.receiverLabel}"
        )
    }

    private fun applyAck(
        state: AppUiState,
        conversationId: String,
        ack: TransportAck
    ): AppUiState {
        val updatedMessages = state.messages[conversationId].orEmpty().map { message ->
            if (message.id == ack.messageId) {
                message.copy(
                    timestamp = ack.deliveredAt,
                    deliveryStatus = MessageDeliveryStatus.SENT
                )
            } else {
                message
            }
        }
        return state.copy(
            messages = state.messages + (conversationId to updatedMessages),
            transportStatus = ack.serverMessage
        )
    }

    private fun applyIncomingReply(
        state: AppUiState,
        reply: IncomingTransportMessage
    ): AppUiState {
        val markedMessages = state.messages[reply.conversationId].orEmpty().map { message ->
            if (message.id == reply.replyToMessageId) {
                message.copy(deliveryStatus = MessageDeliveryStatus.DELIVERED)
            } else {
                message
            }
        }
        val incomingMessage = ChatMessage(
            id = "remote-${System.currentTimeMillis()}",
            author = reply.senderLabel,
            authorRole = AuthorRole.REMOTE,
            kind = reply.kind,
            body = reply.content,
            timestamp = reply.timestampLabel,
            deliveryStatus = MessageDeliveryStatus.DELIVERED
        )
        return state.copy(
            conversations = state.conversations.updateConversationSummary(
                conversationId = reply.conversationId,
                preview = reply.content,
                kind = reply.kind,
                timestamp = reply.timestampLabel,
                unreadCount = 0
            ),
            messages = state.messages + (reply.conversationId to (markedMessages + incomingMessage)),
            transportStatus = "收到 ${reply.senderLabel} 的回执消息"
        )
    }

    private fun markFailed(
        state: AppUiState,
        conversationId: String,
        messageId: String,
        reason: String
    ): AppUiState {
        val updatedMessages = state.messages[conversationId].orEmpty().map { message ->
            if (message.id == messageId) {
                message.copy(deliveryStatus = MessageDeliveryStatus.FAILED)
            } else {
                message
            }
        }
        return state.copy(
            messages = state.messages + (conversationId to updatedMessages),
            transportStatus = reason
        )
    }

    private fun List<ConversationSummary>.updateConversationSummary(
        conversationId: String,
        preview: String,
        kind: MessageKind,
        timestamp: String,
        unreadCount: Int
    ): List<ConversationSummary> {
        return map { conversation ->
            if (conversation.id == conversationId) {
                conversation.copy(
                    preview = preview,
                    kind = kind,
                    timestamp = timestamp,
                    unreadCount = unreadCount
                )
            } else {
                conversation
            }
        }
    }

    private fun MessageKind.toChineseLabel(): String {
        return when (this) {
            MessageKind.TEXT -> "文本"
            MessageKind.IMAGE -> "图片"
            MessageKind.AUDIO -> "语音"
            MessageKind.VIDEO -> "视频"
        }
    }
}
