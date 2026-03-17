package com.example.myapplication1.data.transport

import com.example.myapplication1.domain.model.MessageKind

data class InteropMessagePayload(
    val messageId: String,
    val conversationId: String,
    val senderId: String,
    val senderClient: String,
    val receiverLabel: String,
    val kind: MessageKind,
    val content: String,
    val timestampLabel: String
)

data class TransportAck(
    val messageId: String,
    val serverMessage: String,
    val deliveredAt: String
)

data class IncomingTransportMessage(
    val replyToMessageId: String,
    val conversationId: String,
    val senderLabel: String,
    val kind: MessageKind,
    val content: String,
    val timestampLabel: String
)

interface ChatTransport {
    suspend fun send(payload: InteropMessagePayload): TransportAck
    suspend fun awaitIncomingReply(payload: InteropMessagePayload): IncomingTransportMessage
}
