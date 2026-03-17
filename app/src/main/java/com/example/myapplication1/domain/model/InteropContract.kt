package com.example.myapplication1.domain.model

data class InteropProtocolField(
    val name: String,
    val description: String,
    val example: String
)

object PcInteropContract {
    val messageFields = listOf(
        InteropProtocolField(
            name = "conversationId",
            description = "会话唯一标识，Android 和 PC 端必须一致。",
            example = "c1"
        ),
        InteropProtocolField(
            name = "kind",
            description = "消息类型，推荐使用 TEXT / IMAGE / AUDIO / VIDEO。",
            example = "TEXT"
        ),
        InteropProtocolField(
            name = "content",
            description = "文本内容或媒体文件名 / 下载地址。",
            example = "已选择图片：机房拓扑图.png"
        ),
        InteropProtocolField(
            name = "timestamp",
            description = "消息生成时间，建议使用统一时间源。",
            example = "2026-03-17 10:30:00"
        ),
        InteropProtocolField(
            name = "senderId",
            description = "发送端标识，推荐直接用 SIP 账号或业务账号。",
            example = "sip:android01@campus-lab.local"
        )
    )
}
