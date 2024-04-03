package edu.temple.convoy3.playback

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

class AndroidAudioPlayer(
    private val context: Context
): AudioPlayer {

    private var player: MediaPlayer? = null

    override fun playFile(file: File) {
        MediaPlayer.create(context, file.toUri()).apply {
            player = this
            start()
        }
    }

    fun playURI(uri: Uri) {
        MediaPlayer.create(context, uri).apply {
            player = this
            start()
        }
    }

    override fun playRecordingFromUrl(url: String) {
        MediaPlayer.create(context, Uri.parse(url)).apply{
            player = this
            start()
            setOnCompletionListener {
                stop()
                release()
            }
        }
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }

    override fun downloadFile(url: URL, fileName: String) {

    }

    suspend fun downloadAudio(context: Context, url: String): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connect()
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // If the response code is OK, proceed with downloading
                    val audioFile = File(context.filesDir, "audio_file.mp3")
                    val inputStream = BufferedInputStream(connection.inputStream)
                    val outputStream = FileOutputStream(audioFile)
                    inputStream.copyTo(outputStream)
                    return@withContext Uri.fromFile(audioFile)
                } else if (responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
                    // If the response code is 301, follow the redirect
                    val redirectUrl = connection.getHeaderField("Location")
                    Log.d("Redirect URL", redirectUrl)
                    return@withContext downloadAudio(
                        context,
                        redirectUrl
                    ) // Recursively call downloadAudio with the new URL
                } else {
                    Log.e("Download Error", "Server returned HTTP response code: $responseCode")
                }
            } catch (e: IOException) {
                Log.e("Download Error", "Error downloading audio file: ${e.message}")
            }
            return@withContext null
        }

    }

}