package edu.temple.convoy3.recorder

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}