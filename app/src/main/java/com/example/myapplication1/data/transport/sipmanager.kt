package com.example.myapplication1.data.transport

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

object SipManager {
    private const val TAG = "SipManager"
    private var socket: DatagramSocket? = null
    private var localPort = 5060
    private var localIp = "10.0.2.15"

    fun initSipStack(localIp: String) {
        this.localIp = localIp
        try {
            if (socket == null || socket!!.isClosed) {
                // 不依赖任何第三方库，直接开启最底层的 UDP 通道
                socket = DatagramSocket(localPort)
                Log.d(TAG, "✅ SIP 引擎启动成功 (已掀桌子，纯净 UDP 模式)")
                startListening()
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ UDP 端口绑定失败: ${e.message}")
        }
    }

    private fun startListening() {
        Thread {
            val buffer = ByteArray(2048)
            while (socket != null && !socket!!.isClosed) {
                try {
                    val packet = DatagramPacket(buffer, buffer.size)
                    socket!!.receive(packet)
                    val response = String(packet.data, 0, packet.length)
                    Log.d(TAG, "📩 收到服务器响应:\n$response")

                    // 解析服务器返回的成功状态码
                    if (response.contains("SIP/2.0 200 OK")) {
                        Log.i(TAG, "✅✅✅ 注册成功！服务器已接受该账号！")
                    } else if (response.contains("401") || response.contains("407")) {
                        Log.w(TAG, "⚠️ 收到 401：服务器要求密码鉴权")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "接收错误: ${e.message}")
                }
            }
        }.start()
    }

    suspend fun register(user: String, server: String) {
        withContext(Dispatchers.IO) {
            try {
                val callId = "${System.currentTimeMillis()}@$localIp"
                val branch = "z9hG4bK${System.currentTimeMillis()}"
                val tag = "tag${System.currentTimeMillis()}"

                // 手写标准的 SIP REGISTER 协议报文，跟第三方库发出去的一模一样
                val sipMessage = """
                    REGISTER sip:$server:5060 SIP/2.0
                    Via: SIP/2.0/UDP $localIp:$localPort;branch=$branch
                    Max-Forwards: 70
                    To: <sip:$user@$server>
                    From: <sip:$user@$server>;tag=$tag
                    Call-ID: $callId
                    CSeq: 1 REGISTER
                    Contact: <sip:$user@$localIp:$localPort>
                    Content-Length: 0

                """.trimIndent().replace("\n", "\r\n") + "\r\n"

                val serverAddress = InetAddress.getByName(server)
                val data = sipMessage.toByteArray()
                val packet = DatagramPacket(data, data.size, serverAddress, 5060)

                // 一键发送，没有任何拦截
                socket?.send(packet)
                Log.d(TAG, "👉 注册请求已发出 (无惧系统拦截)")
            } catch (e: Exception) {
                Log.e(TAG, "❌ 发送失败: ${e.message}")
            }
        }
    }
}