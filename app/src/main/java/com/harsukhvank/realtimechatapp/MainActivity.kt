package com.harsukhvank.realtimechatapp

import ChatViewModel
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent


class MainActivity : ComponentActivity() {

    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val chatViewModel = ChatViewModel(connectivityManager, applicationContext)

        setContent {
            ChatApp(chatViewModel)
        }
    }
}
