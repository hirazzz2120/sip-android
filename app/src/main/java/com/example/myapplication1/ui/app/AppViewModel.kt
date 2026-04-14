package com.example.myapplication1.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication1.data.transport.SipManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState

    fun enterWorkspace() {
        // 切换 UI 状态
        _uiState.update { it.copy(isAuthenticated = true) }

        // 异步注册 SIP
        viewModelScope.launch {
            try {
                SipManager.initSipStack("10.0.2.15")
                SipManager.register("102", "10.0.2.2")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 🚨 终极修复：使用 vararg args: Any? 充当“黑洞”，接收 MainActivity 传来的任何参数
    fun sendMediaPlaceholder(vararg args: Any?) {
        android.util.Log.d("AppViewModel", "点击了媒体发送，接收到的参数数量: ${args.size}")
    }

    // 为了防范未然，把这几个函数也加上高容错
    fun selectConversation(vararg args: Any?) {}
    fun updateDraft(vararg args: Any?) {}
    fun sendTextMessage(vararg args: Any?) {}
}