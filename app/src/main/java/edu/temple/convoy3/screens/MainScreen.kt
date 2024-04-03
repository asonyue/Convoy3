package edu.temple.convoy3.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.temple.convoy3.components.Clickable_button
import edu.temple.convoy3.components.MyDialog
import edu.temple.convoy3.network.ApiManager
import edu.temple.convoy3.playback.AndroidAudioPlayer
import edu.temple.convoy3.playback.AudioPlayer
import edu.temple.convoy3.recorder.AndroidAudioRecorder
import edu.temple.convoy3.utility.SharedPreferencesManager
import java.io.File
import okhttp3.MultipartBody
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import edu.temple.convoy3.components.ConvoyDialog
import edu.temple.convoy3.components.RecordingDialog
import edu.temple.convoy3.firebase.FCMViewModel


@Composable

fun MainScreen() {

    val messageData by FCMViewModel().messageData

    messageData?.let { data ->
        Text(text = "Username: ${data.username}")
        // Update other UI components as needed
    }

    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var showDialogTyping by remember { mutableStateOf(false) }
    var showRecordDialog by remember { mutableStateOf(false) }

    var conveyIDDisplay by remember { mutableStateOf("") }
    var inConvoy by remember { mutableStateOf(false) }

    val recorder by lazy {
        AndroidAudioRecorder(context)
    }

    val player by lazy {
        AndroidAudioPlayer(context)
    }

    var audioFile: File? = null

    Surface {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            FloatingActionButton(
                                onClick = { showDialog = true },
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Text(text = "Convoy")
                            }
                            FloatingActionButton(
                                onClick = { showRecordDialog = true },
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Text(text = "Record")
                            }
                        }
                    }
                )
            }
        ) {
            val tmp = it
            Text("Convoy ID: $conveyIDDisplay ")
        }
    }

    if (showDialog) {
        ConvoyDialog(
            onDismissRequest = { showDialog = false },
            startConvoy = {
                val loginSession = SharedPreferencesManager.getSessionKey(context)
                val userName = SharedPreferencesManager.getUsername(context)
                if (loginSession != null && userName != null) {
                    ApiManager.startConvey(userName, loginSession) {
                        if (it.convoy_id != null) {
                            SharedPreferencesManager.saveConvoySessionKey(context, it.convoy_id)
                            conveyIDDisplay = it.convoy_id
                            inConvoy = true
                        }
                    }
                }

            },
            joinConvoy = {
                showDialogTyping = true

            },
            endConvoy = {
                val userName = SharedPreferencesManager.getUsername(context)
                val loginSession = SharedPreferencesManager.getSessionKey(context)
                val convoySession = SharedPreferencesManager.getConvoySessionKey(context)
                if (loginSession != null && convoySession != null && userName != null) {
                    ApiManager.endConvey(
                        username = userName,
                        sessionKey = loginSession,
                        convoySessionKey = convoySession
                    ) {
                        if (it.status == "SUCCESS") {
                            SharedPreferencesManager.saveConvoySessionKey(context, "")
                            conveyIDDisplay = ""; inConvoy = false
                        }

                    }
                }

            }) {
            //leave
            val userName = SharedPreferencesManager.getUsername(context)
            val loginSession = SharedPreferencesManager.getSessionKey(context)
            val convoySession = SharedPreferencesManager.getConvoySessionKey(context)
            if (loginSession != null && convoySession != null && userName != null) {
                ApiManager.leaveConvey(
                    username = userName,
                    sessionKey = loginSession,
                    convoySessionKey = convoySession
                ) {
                    if (it.status == "SUCCESS") {
                        SharedPreferencesManager.saveConvoySessionKey(context, "")
                        conveyIDDisplay = ""
                        inConvoy = false
                    }
                }
            }


        }
    }

    if (showDialogTyping) {
        MyDialog(onDismissRequest = { showDialogTyping = false }) { convoyValue ->
            val userName = SharedPreferencesManager.getUsername(context)
            val loginSession = SharedPreferencesManager.getSessionKey(context)
            if (loginSession != null && convoyValue != "" && userName != null) {
                ApiManager.joinConvey(
                    username = userName,
                    sessionKey = loginSession,
                    convoySessionKey = convoyValue
                ) {
                    if (it.convoy_id != null) {
                        SharedPreferencesManager.saveConvoySessionKey(context, it.convoy_id)
                        conveyIDDisplay = it.convoy_id
                        inConvoy = true
                    }
                }

            }
        }

    }

    if (showRecordDialog) {
        RecordingDialog(onDismissRequest = { showRecordDialog = false },
            startRecording = {
                File(context.cacheDir, "audio.mp3").also {
                recorder.start(it)
                audioFile = it
            }
            }) {
            // stop
            recorder.stop()
            val userName = SharedPreferencesManager.getUsername(context)
            val loginSession = SharedPreferencesManager.getSessionKey(context)
            val convoySession = SharedPreferencesManager.getConvoySessionKey(context)
            if (loginSession != null && convoySession != null && userName != null ) {
                audioFile?.let {
                        ApiManager.uploadFile(username = userName, sessionKey = loginSession, convoySessionKey = convoySession, file= it) { con ->
                        Log.d("con", con.toString())
                    }
                }
            }
        }
    }

}





//Column(
//modifier = Modifier.padding(10.dp)
//) {
//    Text("Convoy ID: $conveyIDDisplay ")
//    Clickable_button(text = "Create") {
//        val loginSession = SharedPreferencesManager.getSessionKey(context)
//        val userName = SharedPreferencesManager.getUsername(context)
//        if (loginSession != null && userName != null) {
//            ApiManager.startConvey(userName, loginSession) {
//                if(it.convoy_id != null) {
//                    SharedPreferencesManager.saveConvoySessionKey(context, it.convoy_id)
//                    conveyIDDisplay = it.convoy_id
//                }
//            }
//        }
//    }
//    Clickable_button(text = "End") {
//        val userName = SharedPreferencesManager.getUsername(context)
//        val loginSession = SharedPreferencesManager.getSessionKey(context)
//        val convoySession = SharedPreferencesManager.getConvoySessionKey(context)
//        if (loginSession != null && convoySession != null && userName != null) {
//            ApiManager.endConvey(username = userName, sessionKey = loginSession, convoySessionKey = convoySession) {
//                if(it.status == "SUCCESS") {
//                    SharedPreferencesManager.saveConvoySessionKey(context, "")
//                    conveyIDDisplay = ""
//                }
//            }
//        }
//    }
//    Clickable_button(text = "Join") {
//        showDialog = true
//    }
//    Clickable_button(text = "Leave") {
//        val userName = SharedPreferencesManager.getUsername(context)
//        val loginSession = SharedPreferencesManager.getSessionKey(context)
//        val convoySession = SharedPreferencesManager.getConvoySessionKey(context)
//        if (loginSession != null && convoySession != null && userName != null) {
//            ApiManager.leaveConvey(username = userName, sessionKey = loginSession, convoySessionKey = convoySession) {
//                if(it.status == "SUCCESS") {
//                    SharedPreferencesManager.saveConvoySessionKey(context, "")
//                    conveyIDDisplay = ""
//                }
//            }
//        }
//    }
//    Clickable_button(text = "Start Recording") {
//        File(context.cacheDir, "audio.mp3").also {
//            recorder.start(it)
//            audioFile = it
//        }
//    }
//    Clickable_button(text = "Stop Recording") {
//        recorder.stop()
//    }
//    Clickable_button(text = "Send Recording to Server") {
//        val userName = SharedPreferencesManager.getUsername(context)
//        val loginSession = SharedPreferencesManager.getSessionKey(context)
//        val convoySession = SharedPreferencesManager.getConvoySessionKey(context)
//        if (loginSession != null && convoySession != null && userName != null ) {
//            audioFile?.let {
//                ApiManager.uploadFile(username = userName, sessionKey = loginSession, convoySessionKey = convoySession, file= it) { con ->
//                    Log.d("con", con.toString())
//                }
//            }
//        }
//    }
//    Clickable_button(text = "Play Recording testing") {
//        player.playFile(audioFile!!)
//    }
//    Clickable_button(text = "Stop Playing Recording testing") {
//        player.stop()
//    }
//}
//
//}
//
//if (showDialog) {
//    MyDialog(onDismissRequest = { showDialog = false }) { convoyValue ->
//        val userName = SharedPreferencesManager.getUsername(context)
//        val loginSession = SharedPreferencesManager.getSessionKey(context)
//        if (loginSession != null && convoyValue != "" && userName != null) {
//            ApiManager.joinConvey(
//                username = userName,
//                sessionKey = loginSession,
//                convoySessionKey = convoyValue
//            ) {
//                if (it.convoy_id != null) {
//                    SharedPreferencesManager.saveConvoySessionKey(context, it.convoy_id)
//                    conveyIDDisplay = it.convoy_id
//                }
//            }
//
//        }
//    }
//}