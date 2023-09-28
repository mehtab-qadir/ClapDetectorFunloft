package com.cwc.clapdetector.Utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.widget.SeekBar
import com.warkiz.widget.IndicatorSeekBar

class VolumeChangeReceiver(private val seekBar: IndicatorSeekBar) : BroadcastReceiver() {

    private val audioManager: AudioManager =
        seekBar.context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.media.VOLUME_CHANGED_ACTION") {
            val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            seekBar.setProgress(currentVolume.toFloat())
        }
    }
}
