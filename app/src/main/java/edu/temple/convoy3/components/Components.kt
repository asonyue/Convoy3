package edu.temple.convoy3.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog



@Composable
fun TextCredential(name: String, text: String, onTextChanged: (String) -> Unit ) {
    TextField(
        value = text,
        onValueChange = { onTextChanged(it) },
        label = { Text(name) }
    )
}

@Composable
fun PasswordTextField(password: String, onPassWordChanged: (String) -> Unit) {


    TextField(
        value = password,
        onValueChange = { onPassWordChanged(it) },
        label = { Text("Enter password") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

@Composable
fun Clickable_button(text: String, callback: () -> Unit) {
    TextButton(
        onClick = { callback() },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray)
    )
    {
        Text(
            color = Color.White,
            text = text
        )
    }
}

@Composable
fun MyDialog(onDismissRequest: () -> Unit, callback: (String) -> Unit) {

    var text by remember { mutableStateOf("")  }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(5.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Enter ConvoyID:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                TextField(value = text, onValueChange = {text = it})
                Spacer(modifier = Modifier.padding(10.dp))
                Button(onClick = { callback(text) }) {
                    Text("Submit")
                }
            }
        }
    }
}
