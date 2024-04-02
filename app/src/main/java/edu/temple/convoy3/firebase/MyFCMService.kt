package edu.temple.convoy3.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonParser

class MyFCMService: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e("FCM", "message received!")
        message.data["payload"]?.apply {
            val parsedJson = JsonParser().parse(this).asJsonObject
//            val action = parsedJson.get("action")
//            val username = parsedJson.get("username")
//            val messageFile = parsedJson.get("message_file")
            Log.d("action", parsedJson.toString())
        }
    }
}