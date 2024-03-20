package edu.temple.convoy3.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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


@Composable

fun MainScreen(navController: NavController, ) {

    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var conveyIDDisplay by remember {mutableStateOf("")}

    val recorder by lazy {
        AndroidAudioRecorder(context)
    }

    val player by lazy {
        AndroidAudioPlayer(context)
    }

    var audioFile: File? = null

    Surface {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text("Convoy ID: $conveyIDDisplay ")
            Clickable_button(text = "Create") {
                val loginSession = SharedPreferencesManager.getSessionKey(context)
                val userName = SharedPreferencesManager.getUsername(context)
                if (loginSession != null && userName != null) {
                    ApiManager.startConvey(userName, loginSession) {
                        if(it.convoy_id != null) {
                            SharedPreferencesManager.saveConvoySessionKey(context, it.convoy_id)
                            conveyIDDisplay = it.convoy_id
                        }
                    }
                }
            }
            Clickable_button(text = "End") {
                val userName = SharedPreferencesManager.getUsername(context)
                val loginSession = SharedPreferencesManager.getSessionKey(context)
                val convoySession = SharedPreferencesManager.getConvoySessionKey(context)
                if (loginSession != null && convoySession != null && userName != null) {
                    ApiManager.endConvey(username = userName, sessionKey = loginSession, convoySessionKey = convoySession) {
                        if(it.status == "SUCCESS") {
                            SharedPreferencesManager.saveConvoySessionKey(context, "")
                            conveyIDDisplay = ""
                        }
                    }
                }
            }
            Clickable_button(text = "Join") {
                showDialog = true
            }
            Clickable_button(text = "Leave") {
                val userName = SharedPreferencesManager.getUsername(context)
                val loginSession = SharedPreferencesManager.getSessionKey(context)
                val convoySession = SharedPreferencesManager.getConvoySessionKey(context)
                if (loginSession != null && convoySession != null && userName != null) {
                    ApiManager.leaveConvey(username = userName, sessionKey = loginSession, convoySessionKey = convoySession) {
                        if(it.status == "SUCCESS") {
                            SharedPreferencesManager.saveConvoySessionKey(context, "")
                            conveyIDDisplay = ""
                        }
                    }
                }
            }
            Clickable_button(text = "Start Recording") {
                File(context.cacheDir, "audio.mp3").also {
                    recorder.start(it)
                    audioFile = it
                }
            }
            Clickable_button(text = "Stop Recording") {
                recorder.stop()
            }
            Clickable_button(text = "Send Recording to Server") {
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
            Clickable_button(text = "Play Recording testing") {
                player.playFile(audioFile!!)
            }
            Clickable_button(text = "Stop Playing Recording testing") {
                player.stop()
            }
        }

    }

    if (showDialog) {
        MyDialog(onDismissRequest = { showDialog = false }) { convoyValue ->
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
                    }
                }

            }
        }
    }
}