package com.example.myapplication1.data.transport

import com.example.myapplication1.domain.model.MessageKind
import kotlinx.coroutines.delay

class FakeChatTransport : ChatTransport {
    override suspend fun send(payload: InteropMessagePayload): TransportAck {
        delay(700)
        return TransportAck(
            messageId = payload.messageId,
            serverMessage = buildString {
                append("服务端已接收")
                append(payload.kind.toChineseLabel())
                append("消息，待真实接口替换。")
            },
            deliveredAt = "刚刚"
        )
    }

    override suspend fun awaitIncomingReply(payload: InteropMessagePayload): IncomingTransportMessage {
        delay(900)
        return IncomingTransportMessage(
            replyToMessageId = payload.messageId,
            conversationId = payload.conversationId,
            senderLabel = payload.receiverLabel,
            kind = MessageKind.TEXT,
            content = buildString {
                append("对端已收到你的")
                append(payload.kind.toChineseLabel())
                append("消息：")
                append(payload.content.take(18))
            },
            timestampLabel = "刚刚"
        )
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
