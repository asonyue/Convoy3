package edu.temple.convoy3

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.google.firebase.messaging.FirebaseMessaging
import edu.temple.convoy3.components.Clickable_button
import edu.temple.convoy3.components.MyDialog
import edu.temple.convoy3.navigation.AllNavigation
import edu.temple.convoy3.navigation.AllScreen
import edu.temple.convoy3.ui.theme.Convoy3Theme
import edu.temple.convoy3.utility.SharedPreferencesManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )
        FirebaseMessaging.getInstance()
            .token.addOnSuccessListener {
                Log.d("Token", it.toString())
                SharedPreferencesManager.saveFCMKey(this, it.toString())
            }
        setContent {
            MyApp {
                AllNavigation()
            }
        }
    }
}



@Composable
fun MyApp(content: @Composable () -> Unit) {
    Convoy3Theme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

    MyApp {


    }

}