package edu.temple.convoy3.playback

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}