package com.example.myapplication1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication1.ui.components.StatusChip
import com.example.myapplication1.ui.components.SummaryStatCard
import com.example.myapplication1.ui.theme.Aqua
import com.example.myapplication1.ui.theme.Coral
import com.example.myapplication1.ui.theme.SkyBlue
import com.example.myapplication1.ui.theme.SlateBlue

@Composable
fun LoginScreen(
    onEnterWorkspace: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(SlateBlue, SkyBlue, Aqua)
                )
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            StatusChip(
                text = "课程设计 Android 客户端",
                accent = Coral
            )
            Text(
                text = "联聊",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "面向 SIP 音视频与即时通信的中文 UI 工作台，先用假数据把页面、节奏和展示层完成，再平滑替换成真实服务。",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(
                    listOf(
                        Triple("消息", "4 类", "文本、图片、语音、视频"),
                        Triple("通话", "SIP", "单人到多人会议"),
                        Triple("互通", "PC", "桌面端协议对齐")
                    )
                ) { item ->
                    SummaryStatCard(
                        title = item.first,
                        value = item.second,
                        caption = item.third,
                        accent = when (item.first) {
                            "消息" -> SkyBlue
                            "通话" -> Coral
                            else -> Aqua
                        }
                    )
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(30.dp),
            tonalElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "当前已完成",
                    style = MaterialTheme.typography.titleLarge
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("1. 中文界面骨架和主题风格")
                    Text("2. 本地消息发送、回执和状态展示")
                    Text("3. 联系人、通话、后台管理展示页")
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatusChip(text = "适合先做 UI", accent = Aqua)
                    StatusChip(text = "后续接真实后端", accent = Coral)
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onEnterWorkspace,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SlateBlue
                    )
                ) {
                    Text("进入演示工作台")
                }
            }
        }
    }
}
