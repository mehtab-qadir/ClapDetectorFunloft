package com.cwc.clapdetector.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.hardware.camera2.CameraManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import kotlin.math.abs


class ClapDetector(private val context: Context, private val listener: ClapListener) {
    private val sampleRate = 44100 // Default sensitivity value
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
    @SuppressLint("MissingPermission")
    private val audioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize)

    private val minEventInterval = 500 // Minimum time interval between events (milliseconds)
    private val clapDetectionDelay = 10

    private var isListening = false
    private var isVibrating = false
    private var lastEventTime = 0L

    // Initialize background noise level
    private var backgroundNoiseLevel = 0

    companion object {
        var mediaPlayer: MediaPlayer? = null
    }

    private var vibrator: Vibrator
    private val vibrationHandler = Handler(Looper.getMainLooper())

    private var isFlashing = false
    private val flashHandler = Handler(Looper.getMainLooper())

    init {
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun startListening() {
        if (isListening)
            return
        isListening = true
        audioRecord.startRecording()

        updateBackgroundNoiseLevel()

        val buffer = ShortArray(bufferSize)
        val eventDetectorHandler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                if (isListening) {
                    val bytesRead = audioRecord.read(buffer, 0, bufferSize)
                    if (bytesRead > 0) {
                        val amplitude = buffer.map { abs(it.toInt()) }.maxOrNull() ?: 0
                        val currentTime = System.currentTimeMillis()

                        val detectionThreshold = context.getsoundsenstivity() * 100.0

                        if (amplitude > detectionThreshold && currentTime - lastEventTime > minEventInterval) {
                            lastEventTime = currentTime
                            listener.onEventDetected()
                        }
                    }
                    eventDetectorHandler.postDelayed(this, 100) // Adjust delay
                }
            }
        }

        eventDetectorHandler.post(runnable)
    }

    // Helper function to update background noise level
    private fun updateBackgroundNoiseLevel() {
        val bufferSize = 1024 // Adjust buffer size as needed
        val buffer = ShortArray(bufferSize)
        val bytesRead = audioRecord.read(buffer, 0, bufferSize)
        if (bytesRead > 0) {
            backgroundNoiseLevel = buffer.map { abs(it.toInt()) }.average().toInt()
        }
    }

    fun stopListening() {
        isListening = false
        audioRecord.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        stopVibrating()
    }

    fun playSoundFromAssets(fileName: String, durationInSeconds: Int, isLooping: Boolean) {
        try {
            if (mediaPlayer == null || !mediaPlayer!!.isPlaying) {
                val assetFileDescriptor: AssetFileDescriptor = context.assets.openFd(fileName)
                mediaPlayer = MediaPlayer()
                mediaPlayer?.setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                mediaPlayer?.prepare()

                mediaPlayer?.setOnCompletionListener {
                    if (isLooping) {
                        mediaPlayer?.seekTo(0)
                        mediaPlayer?.start()
                    } else
                        if (durationInSeconds <= 15 || durationInSeconds <= 30 || durationInSeconds <= 60 || durationInSeconds <= 120) {
                            mediaPlayer?.seekTo(0)
                            mediaPlayer?.start()
                        } else {
                            mediaPlayer?.release()
                            mediaPlayer = null
                        }
                }

                mediaPlayer?.start()

                // Stop after the specified duration
                if (!isLooping) {
                    // Stop after the specified duration
                    Handler().postDelayed({
                        mediaPlayer?.stop()
                        mediaPlayer?.release()
                        mediaPlayer = null
                    }, durationInSeconds * 1000.toLong())
                    // Duration in milliseconds
                } // Convert seconds to milliseconds
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startFlashing(pattern:LongArray) {
        if (!isFlashing) {
            isFlashing = true
            flashSOSContinuously(pattern)
        }
    }

     fun stopFlashing(pattern:LongArray) {
        isFlashing = false
        flashHandler.removeCallbacksAndMessages(null)
         flashSOSContinuously(pattern)
    }

    @SuppressLint("ServiceCast")
    private fun flashSOSContinuously(pattern:LongArray) {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]

        var on = true
        var index = 0

        val runnable = object : Runnable {
            override fun run() {
                try {
                    if (isFlashing) {
                        if (on) {
                            cameraManager.setTorchMode(cameraId, true)
                            on = false
                        } else {
                            cameraManager.setTorchMode(cameraId, false)
                            on = true
                        }
                        index = (index + 1) % pattern.size
                        flashHandler.postDelayed(this, pattern[index])
                    } else {
                        turnOffFlash()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        flashHandler.post(runnable)
    }



    private fun turnOffFlash() {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]

        try {
            cameraManager.setTorchMode(cameraId, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


     fun startVibrating(pattern: LongArray) {
        if (!isVibrating) {
            isVibrating = true
            vibrate(pattern)
        }
    }

     fun stopVibrating() {
        isVibrating = false
        vibrationHandler.removeCallbacksAndMessages(null)
        vibrator.cancel()
    }

    private fun vibrate(pattern: LongArray) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
            vibrationHandler.postDelayed({ vibrate(pattern) }, calculatePatternDuration(pattern)) // Vibrate every 200ms
        } else {
            vibrator.vibrate(200)
            vibrationHandler.postDelayed({ vibrate(pattern) }, calculatePatternDuration(pattern)) // Vibrate every 200ms
        }
    }
    private fun calculatePatternDuration(pattern: LongArray): Long {
        return pattern.sum()
    }

    interface ClapListener {
        fun onEventDetected()
    }
}

