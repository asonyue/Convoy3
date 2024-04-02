package edu.temple.convoy3.network

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import edu.temple.convoy3.utility.SharedPreferencesManager
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import okhttp3.MultipartBody
import okhttp3.RequestBody


object ApiManager {

    private val apiService: ApiInterface = ServiceClient.buildService(ApiInterface::class.java)

    fun sendFCM(sessionKey: String, fcmToken: String, username: String) {
        apiService.updateFCM("UPDATE", username=username, sessionKey=sessionKey, fcmToken=fcmToken)
            .enqueue(object: Callback<fcmTokenParser> {
                override fun onResponse(
                    call: Call<fcmTokenParser>,
                    response: Response<fcmTokenParser>
                ) {
                    Log.d("sth", response.body().toString())
                }

                override fun onFailure(call: Call<fcmTokenParser>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }


    fun signUp(firstname: String, lastname: String, username: String, password: String, callback: (AccountAPIParser) -> Unit) {
        apiService.requestSignUp(firstname = firstname, lastname = lastname, username = username, password = password)
            .enqueue(object: Callback<AccountAPIParser>{
                override fun onResponse(
                    call: Call<AccountAPIParser>,
                    response: Response<AccountAPIParser>
                ) {
                    Log.d("message", response.body().toString())
                    if(response.body()?.status == "SUCCESS") {
                        callback(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<AccountAPIParser>, t: Throwable) {
                }
            })
    }

    fun signIn(username: String, password: String, callback: (AccountAPIParser) -> Unit) {
        apiService.requestSignIn(username = username, password = password)
            .enqueue(object: Callback<AccountAPIParser> {
                override fun onResponse(
                    call: Call<AccountAPIParser>,
                    response: Response<AccountAPIParser>
                ) {
                    if(response.body()?.status == "SUCCESS") {
                        callback(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<AccountAPIParser>, t: Throwable) {
                    
                }

            })

    }

    fun startConvey(username: String, sessionKey: String, callback: (ConveyAPIParser) -> Unit) {
        apiService.requestStartConvey(username = username, session_key = sessionKey)
            .enqueue(object: Callback<ConveyAPIParser>{
                override fun onResponse(
                    call: Call<ConveyAPIParser>,
                    response: Response<ConveyAPIParser>
                ) {
                    if(response.body()?.status == "SUCCESS") {
                        callback(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<ConveyAPIParser>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun endConvey(username: String, sessionKey: String, convoySessionKey:String, callback: (ConveyAPIParser) -> Unit) {
        apiService.requestEndConvey(username = username, convoyID = convoySessionKey, sessionKey = sessionKey)
            .enqueue(object: Callback<ConveyAPIParser>{
                override fun onResponse(
                    call: Call<ConveyAPIParser>,
                    response: Response<ConveyAPIParser>
                ) {
                    if(response.body()?.status == "SUCCESS") {
                        callback(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<ConveyAPIParser>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun joinConvey(username: String, sessionKey: String, convoySessionKey:String, callback: (ConveyAPIParser) -> Unit) {
        apiService.joinConvey(username = username, convoyID = convoySessionKey, sessionKey = sessionKey)
            .enqueue(object: Callback<ConveyAPIParser> {
                override fun onResponse(
                    call: Call<ConveyAPIParser>,
                    response: Response<ConveyAPIParser>
                ) {
                    if(response.body()?.status == "SUCCESS") {
                        callback(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<ConveyAPIParser>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun leaveConvey(username: String, sessionKey: String, convoySessionKey:String, callback: (ConveyAPIParser) -> Unit) {
        apiService.leaveConvey(username = username, convoyID = convoySessionKey, sessionKey = sessionKey)
            .enqueue(object: Callback<ConveyAPIParser> {
                override fun onResponse(
                    call: Call<ConveyAPIParser>,
                    response: Response<ConveyAPIParser>
                ) {
                    if(response.body()?.status == "SUCCESS") {
                        callback(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<ConveyAPIParser>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun uploadFile(username: String, sessionKey: String, convoySessionKey:String, file: File, callback: (ConveyAPIParser) -> Unit) {
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val fileUpload = MultipartBody.Part.createFormData("message_file", file.name, requestBody)
        apiService.sendAudioMessage(
            RequestBody.create(MultipartBody.FORM,"MESSAGE"),
            RequestBody.create(MultipartBody.FORM,username),
            RequestBody.create(MultipartBody.FORM,sessionKey),
            RequestBody.create(MultipartBody.FORM,convoySessionKey),
            fileUpload
        )
            .enqueue(object: Callback<ConveyAPIParser> {
                override fun onResponse(
                    call: Call<ConveyAPIParser>,
                    response: Response<ConveyAPIParser>
                ) {
                    callback(response.body()!!)
                }

                override fun onFailure(call: Call<ConveyAPIParser>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })


    }



}
