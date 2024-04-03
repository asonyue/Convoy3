package edu.temple.convoy3.firebase

import android.R.attr.data
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonParser
import edu.temple.convoy3.playback.AndroidAudioPlayer
import edu.temple.convoy3.playback.AudioPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


class MyFCMService: FirebaseMessagingService() {



    private val fcmViewModel by lazy { FCMViewModel() }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e("FCM", "message received!")
        message.data["payload"]?.apply {

            val parsedJson = JsonParser().parse(this).asJsonObject
            val action = parsedJson.get("action")
            val username = parsedJson.get("username")
            val messageFile = parsedJson.get("message_file")
            Log.d("Default", parsedJson.toString())
            Log.d("Action", action.toString())
            Log.d("Username", username.toString())
            Log.d("MessageFile", messageFile.toString())

            val urlb = messageFile.toString()

            val scopea = CoroutineScope(Dispatchers.IO)
            scopea.launch {
                val tmp = AndroidAudioPlayer(this@MyFCMService).downloadAudio(this@MyFCMService, urlb)
                if (tmp != null) {
                    AndroidAudioPlayer(this@MyFCMService).playURI(tmp)
                }
            }
//
//            val scope = CoroutineScope(Dispatchers.IO)
//
//            val outputDir: File = this@MyFCMService.getCacheDir() // context being the Activity pointer
//            val outputFile = File.createTempFile("downloadedFile", ".mp4", outputDir)
//
//            // Launch a new coroutine in the scope
//            scope.launch {
//                var httpsurl = urla.replace("http://", "https://")
//                val url = URL(httpsurl)
//                val voiceData = url.readBytes()
//                val fileOutputStream = FileOutputStream(outputFile)
//                fileOutputStream.write(voiceData)
//                fileOutputStream.close()
//                AndroidAudioPlayer(this@MyFCMService).playFile(outputFile)
//            }

//            val urla = messageFile.toString()
//
//            val scope = CoroutineScope(Dispatchers.IO)
//
//            val outputDir: File = this@MyFCMService.cacheDir // context being the Service pointer
//            val outputFile = File.createTempFile("downloadedFile", ".mp4", outputDir)

// Launch a new coroutine in the scope





//            val file = File(url)
//            val urlFinal = file.toURI().toURL()

            //val outputDir: File = this@MyFCMService.getCacheDir() // context being the Activity pointer

            //val outputFile = File.createTempFile("downloadedFile", ".mp4", outputDir)

            //AndroidAudioPlayer(this@MyFCMService).downloadFile(URL(url), outputFile.canonicalPath)
            //AndroidAudioPlayer(this@MyFCMService).playFile(outputFile)








        }


    }


}