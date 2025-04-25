package com.harsukhvank.realtimechatapp

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


@Composable
fun ChatApp(viewModel: ChatViewModel) {
    var inputText by remember { mutableStateOf("") }
    val context = LocalContext.current

    Row(modifier = Modifier.fillMaxSize().padding(8.dp, 50.dp, 16.dp, 20.dp).imePadding()) {
        Column(modifier = Modifier.weight(0.3f).padding(8.dp)) {
            Text("Chats", style = MaterialTheme.typography.titleLarge)
            Divider()
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(viewModel.chats) { chat ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            viewModel.selectedChatId = chat.id
                            chat.unread = false
                        }.padding(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(chat.name, style = MaterialTheme.typography.bodyLarge)
                            Text(chat.latestMessage, style = MaterialTheme.typography.bodyLarge)
                        }
                        if (chat.unread) {
                            Text("●", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.weight(0.7f).padding(8.dp)) {
            Text("Messages", style = MaterialTheme.typography.titleLarge)
            Divider()
            if (viewModel.getMessagesForSelectedChat().isEmpty()) {
                Box(modifier = Modifier.weight(1f)) {
                    Text("No messages yet.", modifier = Modifier.padding(8.dp))
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(viewModel.getMessagesForSelectedChat()) { message ->
                        Text(text = message, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(4.dp))
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type a message") }
                )
                Button(onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.sendMessage(inputText)
                        inputText = ""
                    } else {
                        Toast.makeText(context, "Message can't be empty", Toast.LENGTH_LONG).show()
                    }
                }) {
                    Text("Send")
                }
            }
        }
    }
}


