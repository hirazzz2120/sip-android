package com.example.myapplication1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.myapplication1.ui.theme.Aqua
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
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.16f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Android SIP 即时通信客户端",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Text(
                text = "联聊",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "当前工程已经预留消息、联系人、音视频通话、后台统计和 PC 客户端互通模块。",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Surface(
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "当前阶段：已完成中文界面骨架、本地消息流和多页面导航，可继续接入真实后端与 SIP。",
                    style = MaterialTheme.typography.bodyMedium
                )
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
