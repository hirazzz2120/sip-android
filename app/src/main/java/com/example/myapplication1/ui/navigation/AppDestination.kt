package com.example.myapplication1.ui.navigation

enum class AppDestination(
    val route: String,
    val label: String,
    val marker: String
) {
    SESSIONS("sessions", "消息", "聊"),
    CONTACTS("contacts", "联系人", "友"),
    CALLS("calls", "通话", "话"),
    ADMIN("admin", "管理", "管")
}
