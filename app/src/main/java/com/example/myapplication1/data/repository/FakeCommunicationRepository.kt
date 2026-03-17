package com.example.myapplication1.data.repository

import com.example.myapplication1.domain.model.AdminAlert
import com.example.myapplication1.domain.model.AdminMetric
import com.example.myapplication1.domain.model.AuthorRole
import com.example.myapplication1.domain.model.CallDirection
import com.example.myapplication1.domain.model.CallSession
import com.example.myapplication1.domain.model.CallState
import com.example.myapplication1.domain.model.ChatMessage
import com.example.myapplication1.domain.model.Contact
import com.example.myapplication1.domain.model.ContactStatus
import com.example.myapplication1.domain.model.ConversationSummary
import com.example.myapplication1.domain.model.MessageKind
import com.example.myapplication1.domain.model.SystemProfile

class FakeCommunicationRepository : CommunicationRepository {
    override fun loadProfile(): SystemProfile {
        return SystemProfile(
            currentUserName = "安卓终端01",
            sipAccount = "sip:android01@campus-lab.local",
            gateway = "ws://10.0.2.2:8080",
            peerClient = "PC 课程设计客户端"
        )
    }

    override fun loadConversations(): List<ConversationSummary> {
        return listOf(
            ConversationSummary(
                id = "c1",
                title = "项目群组",
                preview = "已上传机房网络拓扑草图",
                kind = MessageKind.IMAGE,
                unreadCount = 3,
                timestamp = "09:32",
                isGroup = true
            ),
            ConversationSummary(
                id = "c2",
                title = "教师评审",
                preview = "需要补充 SIP 注册日志和演示视频",
                kind = MessageKind.TEXT,
                unreadCount = 1,
                timestamp = "08:55",
                isGroup = false
            ),
            ConversationSummary(
                id = "c3",
                title = "PC 互通联调",
                preview = "已收到桌面端语音片段",
                kind = MessageKind.AUDIO,
                unreadCount = 0,
                timestamp = "昨天",
                isGroup = false
            )
        )
    }

    override fun loadContacts(): List<Contact> {
        return listOf(
            Contact(
                id = "u1",
                name = "林同学 SIP 网关",
                department = "后端组",
                status = ContactStatus.ONLINE,
                capability = "SIP、HTTP、WS"
            ),
            Contact(
                id = "u2",
                name = "赵同学媒体中继",
                department = "音视频组",
                status = ContactStatus.BUSY,
                capability = "语音、视频"
            ),
            Contact(
                id = "u3",
                name = "PC 实践客户端",
                department = "互通联调",
                status = ContactStatus.OFFLINE,
                capability = "桌面桥接"
            )
        )
    }

    override fun loadCalls(): List<CallSession> {
        return listOf(
            CallSession(
                id = "call1",
                title = "单人 SIP 通话检查",
                participants = 2,
                direction = CallDirection.OUTGOING,
                state = CallState.READY,
                durationLabel = "目标建立时延 < 150ms"
            ),
            CallSession(
                id = "call2",
                title = "402 会议室群组通话",
                participants = 4,
                direction = CallDirection.INCOMING,
                state = CallState.SCHEDULED,
                durationLabel = "支持摄像头与麦克风"
            ),
            CallSession(
                id = "call3",
                title = "桌面端互通测试",
                participants = 2,
                direction = CallDirection.OUTGOING,
                state = CallState.LAST_COMPLETED,
                durationLabel = "12 分 24 秒"
            )
        )
    }

    override fun loadMetrics(): List<AdminMetric> {
        return listOf(
            AdminMetric(
                title = "在线用户",
                value = "18",
                insight = "安卓 4，PC 12，管理节点 2"
            ),
            AdminMetric(
                title = "今日消息量",
                value = "1,284",
                insight = "文本 73%，媒体 27%"
            ),
            AdminMetric(
                title = "通话成功率",
                value = "96.4%",
                insight = "最近失败原因：NAT 超时"
            )
        )
    }

    override fun loadAlerts(): List<AdminAlert> {
        return listOf(
            AdminAlert(
                title = "待完成任务",
                detail = "需要接入真实 SIP 注册与 Invite 握手流程。"
            ),
            AdminAlert(
                title = "互通缺口",
                detail = "PC 与 Android 的消息载荷结构仍需统一协议。"
            ),
            AdminAlert(
                title = "部署说明",
                detail = "管理统计应来自服务端，而不是当前本地假数据。"
            )
        )
    }

    override fun loadMessages(): Map<String, List<ChatMessage>> {
        return mapOf(
            "c1" to listOf(
                ChatMessage(
                    id = "m1",
                    author = "项目群组",
                    authorRole = AuthorRole.REMOTE,
                    kind = MessageKind.TEXT,
                    body = "Android 和 PC 客户端需要共享统一的消息载荷格式。",
                    timestamp = "09:20"
                ),
                ChatMessage(
                    id = "m2",
                    author = "安卓终端01",
                    authorRole = AuthorRole.SELF,
                    kind = MessageKind.TEXT,
                    body = "界面骨架已经完成，下一步接真实传输和本地持久化。",
                    timestamp = "09:24"
                ),
                ChatMessage(
                    id = "m3",
                    author = "项目群组",
                    authorRole = AuthorRole.REMOTE,
                    kind = MessageKind.IMAGE,
                    body = "机房拓扑_v2.png",
                    timestamp = "09:32"
                )
            ),
            "c2" to listOf(
                ChatMessage(
                    id = "m4",
                    author = "教师评审",
                    authorRole = AuthorRole.REMOTE,
                    kind = MessageKind.TEXT,
                    body = "请保留登录、消息投递和 SIP 邀请流程的日志。",
                    timestamp = "08:40"
                ),
                ChatMessage(
                    id = "m5",
                    author = "安卓终端01",
                    authorRole = AuthorRole.SELF,
                    kind = MessageKind.TEXT,
                    body = "收到，核心消息链路完成后我会接入后台统计。",
                    timestamp = "08:55"
                )
            ),
            "c3" to listOf(
                ChatMessage(
                    id = "m6",
                    author = "PC 课程设计客户端",
                    authorRole = AuthorRole.REMOTE,
                    kind = MessageKind.AUDIO,
                    body = "互通样例语音.wav",
                    timestamp = "昨天"
                )
            )
        )
    }
}
