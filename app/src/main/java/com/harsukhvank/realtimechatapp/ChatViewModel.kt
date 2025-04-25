package com.harsukhvank.realtimechatapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.ConcurrentLinkedQueue


class ChatViewModel(
    private val connectivityManager: ConnectivityManager,
    private val context: Context
) {
    var selectedChatId by mutableStateOf<Int?>(null)
    private val _chats = mutableStateListOf<ChatPreview>()
    val chats: List<ChatPreview> get() = _chats

    private val _messages = mutableStateMapOf<Int, SnapshotStateList<String>>()
    private val messageQueue = ConcurrentLinkedQueue<Pair<Int, String>>()
    private var isConnected = true
    private lateinit var webSocket: WebSocket

    init {
        selectedChatId = 1
        _chats.addAll(listOf(
            ChatPreview(1, "Bot 1", "", true),
            ChatPreview(2, "Bot 2", "", true)
        ))
        _messages[1] = mutableStateListOf()
        _messages[2] = mutableStateListOf()
        monitorNetwork()
        connectWebSocket()
    }

    fun getMessagesForSelectedChat(): List<String> =
        selectedChatId?.let { _messages[it] ?: emptyList() } ?: emptyList()

    private fun connectWebSocket() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("wss://s14528.blr1.piesocket.com/v3/1?api_key=2CdnemtN76B0wGY65uAozRlKnxnrO4aK0XuYHR66&notify_self=1")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                showToast("Connected to server")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                selectedChatId?.let { chatId ->
                    _messages[chatId]?.add("Bot: $text")
                    val chat = _chats.find { it.id == chatId }
                    chat?.let {
                        it.latestMessage = text
                        it.unread = true
                    }
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                showToast("WebSocket connection failed: ${t.message}")
                Log.e("WebSocket", "Error: ${t.message}", t)
            }
        })
    }

    fun sendMessage(message: String) {
        selectedChatId?.let { chatId ->
            _messages[chatId]?.add("You: $message")
            _chats.find { it.id == chatId }?.latestMessage = message
            _chats.find { it.id == chatId }?.unread = false

            if (isConnected) {
                webSocket.send(message)
            } else {
                messageQueue.add(Pair(chatId, message))
                showToast("You're offline. Message queued.")
            }
        }
    }

    private fun retryQueuedMessages() {
        var isNotEmpty = false
        while (messageQueue.isNotEmpty()) {
            val (chatId, message) = messageQueue.poll()!!
            _messages[chatId]?.add("Retrying: $message")
            webSocket.send(message)
            isNotEmpty = true
        }

        if(isNotEmpty){
            showToast("All queued messages sent successfully.")
        }
    }

    private fun monitorNetwork() {
        val request = android.net.NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(
            request,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    isConnected = true
                    retryQueuedMessages()
                }

                override fun onLost(network: Network) {
                    isConnected = false
                    showToast("You're offline.")
                }
            }
        )
    }

    private fun showToast(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}