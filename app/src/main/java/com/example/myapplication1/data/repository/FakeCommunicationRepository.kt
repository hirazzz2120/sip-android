package com.example.myapplication1.data.repository

import com.example.myapplication1.domain.model.AdminAlert
import com.example.myapplication1.domain.model.AlertSeverity
import com.example.myapplication1.domain.model.AdminMetric
import com.example.myapplication1.domain.model.AuthorRole
import com.example.myapplication1.domain.model.BackendEndpoint
import com.example.myapplication1.domain.model.CallDirection
import com.example.myapplication1.domain.model.CallSession
import com.example.myapplication1.domain.model.CallState
import com.example.myapplication1.domain.model.ChatMessage
import com.example.myapplication1.domain.model.Contact
import com.example.myapplication1.domain.model.ContactStatus
import com.example.myapplication1.domain.model.ConversationSummary
import com.example.myapplication1.domain.model.DeliveryCheckpoint
import com.example.myapplication1.domain.model.MessageKind
import com.example.myapplication1.domain.model.ReadinessStatus
import com.example.myapplication1.domain.model.SystemProfile
import com.example.myapplication1.domain.model.WorkspaceUpdate

class FakeCommunicationRepository : CommunicationRepository {
    override fun loadProfile(): SystemProfile {
        return SystemProfile(
            currentUserName = "安卓终端01",
            sipAccount = "sip:android01@campus-lab.local",
            gateway = "ws://10.0.2.2:8080",
            peerClient = "PC 课程设计客户端",
            environmentLabel = "课程设计联调环境",
            backendBaseUrl = "http://10.0.2.2:8080/api",
            releaseStage = "前端已可交付后端联调"
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
                isGroup = true,
                memberCount = 6,
                routingKey = "team.project.room-402",
                integrationStatus = ReadinessStatus.READY
            ),
            ConversationSummary(
                id = "c2",
                title = "教师评审",
                preview = "需要补充 SIP 注册日志和演示视频",
                kind = MessageKind.TEXT,
                unreadCount = 1,
                timestamp = "08:55",
                isGroup = false,
                memberCount = 2,
                routingKey = "review.teacher.direct",
                integrationStatus = ReadinessStatus.IN_PROGRESS
            ),
            ConversationSummary(
                id = "c3",
                title = "PC 互通联调",
                preview = "已收到桌面端语音片段",
                kind = MessageKind.AUDIO,
                unreadCount = 0,
                timestamp = "昨天",
                isGroup = false,
                memberCount = 2,
                routingKey = "interop.desktop.bridge",
                integrationStatus = ReadinessStatus.BLOCKED
            ),
            ConversationSummary(
                id = "c4",
                title = "多媒体样式验收",
                preview = "待接真实图片、语音、视频 URL",
                kind = MessageKind.VIDEO,
                unreadCount = 0,
                timestamp = "周一",
                isGroup = true,
                memberCount = 4,
                routingKey = "qa.media.acceptance",
                integrationStatus = ReadinessStatus.TODO
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
                capability = "SIP、HTTP、WS",
                roleTags = listOf("认证", "信令", "网关"),
                responseWindow = "预计 5 分钟内响应"
            ),
            Contact(
                id = "u2",
                name = "赵同学媒体中继",
                department = "音视频组",
                status = ContactStatus.BUSY,
                capability = "语音、视频",
                roleTags = listOf("WebRTC", "录制", "会议"),
                responseWindow = "会议中，15 分钟后可联调"
            ),
            Contact(
                id = "u3",
                name = "PC 实践客户端",
                department = "互通联调",
                status = ContactStatus.OFFLINE,
                capability = "桌面桥接",
                roleTags = listOf("Win 桌面端", "协议对齐"),
                responseWindow = "需等待桌面端上线"
            ),
            Contact(
                id = "u4",
                name = "陈同学数据库",
                department = "后端组",
                status = ContactStatus.ONLINE,
                capability = "MySQL、审计",
                roleTags = listOf("消息存储", "统计报表"),
                responseWindow = "当前可确认字段定义"
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
                durationLabel = "目标建立时延 < 150ms",
                scenarioLabel = "拨号前检查",
                nextAction = "等后端提供注册鉴权接口与账号分配",
                networkScore = 0.78f
            ),
            CallSession(
                id = "call2",
                title = "402 会议室群组通话",
                participants = 4,
                direction = CallDirection.INCOMING,
                state = CallState.SCHEDULED,
                durationLabel = "支持摄像头与麦克风",
                scenarioLabel = "多人会议准备",
                nextAction = "等待多人房间成员列表和主持人控制接口",
                networkScore = 0.61f
            ),
            CallSession(
                id = "call3",
                title = "桌面端互通测试",
                participants = 2,
                direction = CallDirection.OUTGOING,
                state = CallState.LAST_COMPLETED,
                durationLabel = "12 分 24 秒",
                scenarioLabel = "已完成回放",
                nextAction = "补录接通日志和通话详单",
                networkScore = 0.84f
            ),
            CallSession(
                id = "call4",
                title = "教师验收来电",
                participants = 2,
                direction = CallDirection.INCOMING,
                state = CallState.RINGING,
                durationLabel = "来电页面需 3 秒内可操作",
                scenarioLabel = "来电中",
                nextAction = "后端返回呼叫者资料和会议上下文",
                networkScore = 0.66f
            ),
            CallSession(
                id = "call5",
                title = "值班室视频联调",
                participants = 2,
                direction = CallDirection.OUTGOING,
                state = CallState.LIVE,
                durationLabel = "上行码率目标 1.5 Mbps",
                scenarioLabel = "通话中",
                nextAction = "接真实静音、摄像头翻转与弱网提示状态流",
                networkScore = 0.73f
            )
        )
    }

    override fun loadMetrics(): List<AdminMetric> {
        return listOf(
            AdminMetric(
                title = "在线用户",
                value = "18",
                insight = "安卓 4，PC 12，管理节点 2",
                target = "目标 20+"
            ),
            AdminMetric(
                title = "今日消息量",
                value = "1,284",
                insight = "文本 73%，媒体 27%",
                target = "媒体消息占比 >= 25%"
            ),
            AdminMetric(
                title = "通话成功率",
                value = "96.4%",
                insight = "最近失败原因：NAT 超时",
                target = "目标 >= 97%"
            )
        )
    }

    override fun loadAlerts(): List<AdminAlert> {
        return listOf(
            AdminAlert(
                title = "待完成任务",
                detail = "需要接入真实 SIP 注册与 Invite 握手流程。",
                severity = AlertSeverity.CRITICAL
            ),
            AdminAlert(
                title = "互通缺口",
                detail = "PC 与 Android 的消息载荷结构仍需统一协议。",
                severity = AlertSeverity.WARNING
            ),
            AdminAlert(
                title = "部署说明",
                detail = "管理统计应来自服务端，而不是当前本地假数据。",
                severity = AlertSeverity.INFO
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
            ),
            "c4" to listOf(
                ChatMessage(
                    id = "m7",
                    author = "验收群组",
                    authorRole = AuthorRole.REMOTE,
                    kind = MessageKind.VIDEO,
                    body = "演示流程_定版.mp4",
                    timestamp = "周一"
                ),
                ChatMessage(
                    id = "m8",
                    author = "安卓终端01",
                    authorRole = AuthorRole.SELF,
                    kind = MessageKind.IMAGE,
                    body = "消息卡片样式草图_final.png",
                    timestamp = "周一"
                )
            )
        )
    }

    override fun loadCheckpoints(): List<DeliveryCheckpoint> {
        return listOf(
            DeliveryCheckpoint(
                title = "登录与鉴权",
                owner = "后端 + Android",
                detail = "前端表单、跳转和错误展示已就位，等待真实 token 与用户态接口。",
                status = ReadinessStatus.READY
            ),
            DeliveryCheckpoint(
                title = "消息收发",
                owner = "后端 + 传输组",
                detail = "会话页已支持文本、图片、语音、视频四种消息壳，等待真实上传和回执事件。",
                status = ReadinessStatus.READY
            ),
            DeliveryCheckpoint(
                title = "群聊与成员管理",
                owner = "后端",
                detail = "前端已补群聊成员、路由键和状态面板，缺真实成员列表与权限控制。",
                status = ReadinessStatus.IN_PROGRESS
            ),
            DeliveryCheckpoint(
                title = "SIP 通话状态流",
                owner = "音视频组",
                detail = "拨号、来电、通话中、会议准备四类页面已展开，等待实时信令与媒体状态。",
                status = ReadinessStatus.IN_PROGRESS
            ),
            DeliveryCheckpoint(
                title = "审计统计与告警",
                owner = "后端",
                detail = "管理页已定义指标、接口清单和严重度分层，等待真实报表数据。",
                status = ReadinessStatus.TODO
            ),
            DeliveryCheckpoint(
                title = "PC 协议统一",
                owner = "Android + PC",
                detail = "协议字段已展示，但仍缺 ACK、已读、媒体下载地址和错误码规范。",
                status = ReadinessStatus.BLOCKED
            )
        )
    }

    override fun loadBackendEndpoints(): List<BackendEndpoint> {
        return listOf(
            BackendEndpoint(
                id = "api1",
                name = "登录鉴权",
                method = "POST",
                path = "/auth/login",
                purpose = "返回 token、用户资料、SIP 账号和网关地址。",
                payloadHint = "{ username, password, deviceId }",
                status = ReadinessStatus.READY
            ),
            BackendEndpoint(
                id = "api2",
                name = "会话列表",
                method = "GET",
                path = "/conversations",
                purpose = "返回最近消息、未读数、群聊成员数和会话路由键。",
                payloadHint = "query: page, size, updatedAfter",
                status = ReadinessStatus.READY
            ),
            BackendEndpoint(
                id = "api3",
                name = "消息发送",
                method = "POST",
                path = "/messages/send",
                purpose = "统一发送文本与媒体消息，返回 messageId 和初始投递状态。",
                payloadHint = "{ conversationId, kind, content, attachmentUrl? }",
                status = ReadinessStatus.IN_PROGRESS
            ),
            BackendEndpoint(
                id = "api4",
                name = "群聊成员管理",
                method = "PATCH",
                path = "/groups/{groupId}/members",
                purpose = "增删成员并返回成员角色与展示顺序。",
                payloadHint = "{ addUserIds, removeUserIds, operatorId }",
                status = ReadinessStatus.TODO
            ),
            BackendEndpoint(
                id = "api5",
                name = "SIP 注册信息",
                method = "GET",
                path = "/calls/bootstrap",
                purpose = "下发 SIP 域、鉴权参数、TURN/STUN 和会控能力。",
                payloadHint = "query: userId, platform",
                status = ReadinessStatus.BLOCKED
            ),
            BackendEndpoint(
                id = "api6",
                name = "后台统计",
                method = "GET",
                path = "/admin/dashboard",
                purpose = "返回消息量、在线用户、失败率和告警列表。",
                payloadHint = "query: from, to, granularity",
                status = ReadinessStatus.TODO
            )
        )
    }

    override fun loadWorkspaceUpdates(): List<WorkspaceUpdate> {
        return listOf(
            WorkspaceUpdate(
                title = "前端工作台进入可联调状态",
                detail = "页面已补加载壳、空态、媒体消息样式和后端接口清单。",
                timeLabel = "今天 10:20",
                status = ReadinessStatus.READY
            ),
            WorkspaceUpdate(
                title = "PC 互通字段待锁定",
                detail = "建议统一 messageId、ackStatus、attachmentUrl 和 senderClient。",
                timeLabel = "今天 09:45",
                status = ReadinessStatus.BLOCKED
            ),
            WorkspaceUpdate(
                title = "SIP 联调等待后端账号",
                detail = "前端通话页面已拆成来电、拨号、通话中、会议准备四类展示。",
                timeLabel = "昨天",
                status = ReadinessStatus.IN_PROGRESS
            )
        )
    }
}
