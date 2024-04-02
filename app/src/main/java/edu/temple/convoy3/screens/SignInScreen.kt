package edu.temple.convoy3.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.messaging.FirebaseMessaging
import edu.temple.convoy3.components.Clickable_button
import edu.temple.convoy3.components.PasswordTextField
import edu.temple.convoy3.components.TextCredential
import edu.temple.convoy3.navigation.AllScreen
import edu.temple.convoy3.network.ApiManager
import edu.temple.convoy3.utility.SharedPreferencesManager

fun sendFCM(context: Context) {

    var sessionKey = ""
    var fcmToken = ""
    var username = ""

    FirebaseMessaging.getInstance()
        .token.addOnSuccessListener {
            fcmToken = it.toString()
            sessionKey = SharedPreferencesManager.getSessionKey(context).toString()
            username = SharedPreferencesManager.getUsername(context).toString()
            ApiManager.sendFCM(sessionKey, fcmToken, username)
        }




}

@Composable

fun SignInScreen(navController: NavController) {

    val context = LocalContext.current

    var username by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    Surface {
        Column(
            modifier = Modifier
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextCredential(name = "Username", text = username) {
                username = it
            }
            PasswordTextField(password = password) {
                password = it
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Clickable_button(text = "Login") {
                ApiManager.signIn(username, password) {
                    if(it.status == "SUCCESS") {
                        SharedPreferencesManager.saveSessionKey(context, it.session_key.toString())
                        SharedPreferencesManager.saveUsername(context, username)
                        navController.navigate(route=AllScreen.MainScreen.name)
                        sendFCM(context)
                    }
                }
            }
            Clickable_button(text = "No Account") { navController.navigate(route=AllScreen.SignUpScreen.name) }
        }
    }
}