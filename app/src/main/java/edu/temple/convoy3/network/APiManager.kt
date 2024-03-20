package edu.temple.convoy3.network

import android.content.Context
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

    fun signUp(firstname: String, lastname: String, username: String, password: String, callback: (AccountAPIParser) -> Unit) {
        apiService.requestSignUp(firstname = firstname, lastname = lastname, username = username, password = password)
            .enqueue(object: Callback<AccountAPIParser>{
                override fun onResponse(
                    call: Call<AccountAPIParser>,
                    response: Response<AccountAPIParser>
                ) {
                    if(response.body()?.status == "SUCCESS") {
                        callback(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<AccountAPIParser>, t: Throwable) {
                    TODO("Not yet implemented")
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
                    TODO("Not yet implemented")
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
        val requestBody = RequestBody.create(MediaType.parse("audio/mp3"), file)
        val fileUpload = MultipartBody.Part.createFormData("message_file", file.name, requestBody)
        apiService.uploadFile(username=username, sessionKey=sessionKey, convoyID = convoySessionKey, messageFile = fileUpload)
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
