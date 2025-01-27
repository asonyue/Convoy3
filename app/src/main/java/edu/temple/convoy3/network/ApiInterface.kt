package edu.temple.convoy3.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

data class AccountAPIParser(
    var status: String,
    var session_key: String?,
    var message: String?,
)

data class ConveyAPIParser(
    val status: String,
    val convoy_id: String?,
    val message: String?
)
data class fcmTokenParser(
    val status: String
)

interface ApiInterface {

    @FormUrlEncoded
    @POST("account.php")
    fun requestSignUp(
        @Field("action") action: String = "REGISTER",
        @Field("firstname") firstname: String,
        @Field("lastname") lastname: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<AccountAPIParser>

    @FormUrlEncoded
    @POST("account.php")
    fun requestSignIn(
        @Field("action") action: String = "LOGIN",
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<AccountAPIParser>

    @FormUrlEncoded
    @POST("convoy.php")
    fun requestStartConvey(
        @Field("action") action: String = "CREATE",
        @Field("username") username: String,
        @Field("session_key") session_key: String?
    ) : Call<ConveyAPIParser>

    @FormUrlEncoded
    @POST("convoy.php")
    fun requestEndConvey(
        @Field("action") action: String = "END",
        @Field("username") username: String,
        @Field("session_key") sessionKey: String,
        @Field("convoy_id") convoyID: String
    ) : Call<ConveyAPIParser>

    @FormUrlEncoded
    @POST("convoy.php")
    fun joinConvey(
        @Field("action") action: String = "JOIN",
        @Field("username") username: String,
        @Field("session_key") sessionKey: String,
        @Field("convoy_id") convoyID: String
    ) : Call<ConveyAPIParser>

    @FormUrlEncoded
    @POST("convoy.php")
    fun leaveConvey(
        @Field("action") action: String = "LEAVE",
        @Field("username") username: String,
        @Field("session_key") sessionKey: String,
        @Field("convoy_id") convoyID: String
    ) : Call<ConveyAPIParser>

    @Multipart
    @POST("convoy.php")
    fun sendAudioMessage(
        @Part("action") action: RequestBody,                //"MESSAGE"
        @Part("username") username: RequestBody,
        @Part("session_key") sessionKey: RequestBody,
        @Part("convoy_id") convoyId: RequestBody,
        @Part messageFile: MultipartBody.Part       //short recording from convoy member
    ): Call<ConveyAPIParser>

    @FormUrlEncoded
    @POST("account.php")
    fun updateFCM(
        @Field("action") action: String,
        @Field("username") username: String,
        @Field("session_key") sessionKey: String,
        @Field("fcm_token") fcmToken: String
    ): Call<fcmTokenParser>

}