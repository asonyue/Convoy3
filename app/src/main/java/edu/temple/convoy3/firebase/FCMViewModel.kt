package edu.temple.convoy3.firebase

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class MessageData(val action: String, val username: String, val url: String)

class FCMViewModel : ViewModel() {
    private val _messageData = mutableStateOf<MessageData?>(null)
    val messageData: State<MessageData?> = _messageData

    fun setMessageData(messageData: MessageData?) {
        _messageData.value = messageData
    }
}
