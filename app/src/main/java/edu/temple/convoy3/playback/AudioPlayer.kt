package edu.temple.convoy3.playback

import java.io.File
import java.net.URL

interface AudioPlayer {
    fun playFile(file: File)
    fun playRecordingFromUrl(url: String)
    fun stop()

    fun downloadFile(url: URL, fileName: String)
}