package edu.temple.convoy3.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.temple.convoy3.components.Clickable_button
import edu.temple.convoy3.components.PasswordTextField
import edu.temple.convoy3.components.TextCredential
import edu.temple.convoy3.navigation.AllScreen
import edu.temple.convoy3.network.ApiManager
import edu.temple.convoy3.utility.SharedPreferencesManager

@Composable

fun SignUpScreen(navController: NavController) {

    val context = LocalContext.current

    var firstname by rememberSaveable {
        mutableStateOf("")
    }

    var lastname by rememberSaveable {
        mutableStateOf("")
    }

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
            TextCredential(name = "Firstname", text = firstname) {
                firstname = it
            }
            TextCredential(name = "Lastname", text = lastname) {
                lastname = it
            }
            TextCredential(name = "Username", text = username) {
                username = it
            }
            PasswordTextField(password = password) {
                password = it
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Clickable_button(text = "SignUp") {
                ApiManager.signUp(firstname, lastname, username, password) {
                    if(it.status == "SUCCESS") {
                        SharedPreferencesManager.saveSessionKey(context, it.session_key.toString())
                        SharedPreferencesManager.saveUsername(context, username)
                        navController.navigate(route=AllScreen.MainScreen.name)
                        sendFCM(context)
                    }
                }
            }
            Clickable_button(text = "Have Account") { navController.navigate(route= AllScreen.SignInScreen.name) }
        }
    }
}