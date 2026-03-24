package com.example.myapplication1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication1.domain.model.AdminAlert
import com.example.myapplication1.domain.model.AdminMetric
import com.example.myapplication1.domain.model.AlertSeverity
import com.example.myapplication1.domain.model.PcInteropContract
import com.example.myapplication1.ui.app.AppUiState
import com.example.myapplication1.ui.components.AccentPanel
import com.example.myapplication1.ui.components.BackendEndpointCard
import com.example.myapplication1.ui.components.ReadinessChip
import com.example.myapplication1.ui.components.SignalMeter
import com.example.myapplication1.ui.components.SummaryStatCard
import com.example.myapplication1.ui.theme.Aqua
import com.example.myapplication1.ui.theme.Coral
import com.example.myapplication1.ui.theme.SkyBlue
import com.example.myapplication1.ui.theme.Steel

@Composable
fun AdminScreen(state: AppUiState) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionHeader(
                title = "后台管理",
                subtitle = "这一页已经整理成后端可直接对照的交付面板，覆盖指标、接口、协议和告警四个面。"
            )
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(state.metrics, key = AdminMetric::title) { metric ->
                    SummaryStatCard(
                        title = metric.title,
                        value = metric.value,
                        caption = "${metric.insight} · ${metric.target}",
                        accent = when (metric.title) {
                            "在线用户" -> Aqua
                            "今日消息量" -> SkyBlue
                            else -> Coral
                        }
                    )
                }
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "运行观测",
                        style = MaterialTheme.typography.titleLarge
                    )
                    SignalMeter(label = "消息投递稳定度", progress = 0.81f, accent = SkyBlue)
                    SignalMeter(label = "SIP 注册准备度", progress = 0.56f, accent = Coral)
                    SignalMeter(label = "PC 协议完成度", progress = 0.67f, accent = Aqua)
                }
            }
        }
        item {
            AccentPanel(
                title = "服务端建设范围",
                subtitle = "这部分用于课程设计答辩时解释后台要承担的职责。",
                lines = listOf(
                    "登录、身份认证、SIP 启动信息分发",
                    "消息投递、已读回执、离线消息和媒体上传下载",
                    "通话详单、会议控制、告警和审计检索",
                    "PC / Android 客户端统一协议与错误码"
                ),
                accent = Coral
            )
        }
        item {
            HighlightPanel(
                title = "交付检查线",
                lines = state.checkpoints.map { checkpoint ->
                    "${checkpoint.title} · ${checkpoint.owner} · ${checkpoint.status}"
                }
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "后端接口清单",
                        style = MaterialTheme.typography.titleLarge
                    )
                    state.backendEndpoints.forEach { endpoint ->
                        BackendEndpointCard(endpoint = endpoint)
                    }
                }
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "PC 互通协议字段",
                        style = MaterialTheme.typography.titleLarge
                    )
                    PcInteropContract.messageFields.forEach { field ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = field.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = field.description,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "示例：${field.example}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
        items(state.alerts, key = AdminAlert::title) { alert ->
            AlertCard(alert = alert)
        }
    }
}

@Composable
private fun AlertCard(alert: AdminAlert) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = alert.title,
                style = MaterialTheme.typography.titleMedium
            )
            ReadinessChip(
                status = when (alert.severity) {
                    AlertSeverity.INFO -> com.example.myapplication1.domain.model.ReadinessStatus.READY
                    AlertSeverity.WARNING -> com.example.myapplication1.domain.model.ReadinessStatus.IN_PROGRESS
                    AlertSeverity.CRITICAL -> com.example.myapplication1.domain.model.ReadinessStatus.BLOCKED
                }
            )
            Text(
                text = alert.detail,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = when (alert.severity) {
                    AlertSeverity.INFO -> "影响级别：信息"
                    AlertSeverity.WARNING -> "影响级别：需尽快跟进"
                    AlertSeverity.CRITICAL -> "影响级别：阻塞联调"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = when (alert.severity) {
                    AlertSeverity.INFO -> Steel
                    AlertSeverity.WARNING -> SkyBlue
                    AlertSeverity.CRITICAL -> Coral
                }
            )
        }
    }
}
