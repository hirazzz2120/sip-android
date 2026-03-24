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
import com.example.myapplication1.ui.app.AppUiState
import com.example.myapplication1.ui.components.SignalMeter
import com.example.myapplication1.ui.components.StatusChip
import com.example.myapplication1.ui.components.SummaryStatCard
import com.example.myapplication1.ui.theme.Aqua
import com.example.myapplication1.ui.theme.Coral
import com.example.myapplication1.ui.theme.SkyBlue

@Composable
fun AdminScreen(state: AppUiState) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionHeader(
                title = "管理",
                subtitle = "查看系统状态、关键指标和提醒。"
            )
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(state.metrics, key = AdminMetric::title) { metric ->
                    SummaryStatCard(
                        title = metric.title,
                        value = metric.value,
                        caption = metric.insight,
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
                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "系统概览",
                        style = MaterialTheme.typography.titleLarge
                    )
                    SignalMeter(label = "消息稳定度", progress = 0.81f, accent = SkyBlue)
                    SignalMeter(label = "通话成功率", progress = 0.76f, accent = Coral)
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
            StatusChip(
                text = when (alert.severity.name) {
                    "CRITICAL" -> "重要"
                    "WARNING" -> "提醒"
                    else -> "信息"
                },
                accent = when (alert.severity.name) {
                    "CRITICAL" -> Coral
                    "WARNING" -> SkyBlue
                    else -> Aqua
                }
            )
            Text(
                text = alert.detail,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
