package com.cwc.clapdetector.UserInterface

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.AssetFileDescriptor
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.cwc.clapdetector.Ads.NativeAdMob
import com.cwc.clapdetector.R
import com.cwc.clapdetector.Utils.ClapDetector
import com.cwc.clapdetector.Utils.VolumeChangeReceiver
import com.cwc.clapdetector.Utils.getsoundactive
import com.cwc.clapdetector.Utils.getsoundtimeposition
import com.cwc.clapdetector.Utils.savesound
import com.cwc.clapdetector.Utils.savesoundactive
import com.cwc.clapdetector.Utils.savesoundposition
import com.cwc.clapdetector.Utils.savesoundtimeposition
import com.cwc.clapdetector.databinding.ActivitySetAudioBinding
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams

class SetAudio : AppCompatActivity(),View.OnClickListener {

    private lateinit var binding: ActivitySetAudioBinding
    private lateinit var volumeChangeReceiver: BroadcastReceiver
    var nativeAd:NativeAdMob?=null
    var soundtime:Int?=15
    var position:Int?=null
    var checked:Boolean = true
    var sound:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val isDarkMode = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        if (isDarkMode){
            binding.soundplay.background = ContextCompat.getDrawable(this, R.drawable.circlestroke_night)
        }
        else{
            binding.soundplay.background = ContextCompat.getDrawable(this, R.drawable.circlestroke)
        }
        nativeAd = NativeAdMob()
        nativeAd!!.LoadNativeAd(this,R.string.native_id,R.layout.native_medium,binding.shimerlayoutparentview.shimerlayout,binding.adviewnative,binding.nativeAd)
        soundtime = getsoundtimeposition()
        checked = getsoundactive()
        binding.switchMaterial.isChecked = checked
        binding.second15.setOnClickListener(this)
        binding.second30.setOnClickListener(this)
        binding.minutes1.setOnClickListener(this)
        binding.minutes2.setOnClickListener(this)
        binding.loop.setOnClickListener(this)
        binding.apply.setOnClickListener(this)
        binding.soundplay.setOnClickListener(this)

        val intent = intent.extras
        val name  = intent?.getString("name")
        val image = intent?.getInt("image")
        position = intent?.getInt("pos")
        sound = intent?.getString("sound")

        if(soundtime == 15){
            binding.second15.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.appcolor))
            binding.second15.strokeColor = ContextCompat.getColor(this, R.color.appcolor)
            binding.second15text.setTextColor(ContextCompat.getColor(this, R.color.white1))
            binding.second30.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.second30.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.second30text.setTextColor(ContextCompat.getColor(this, R.color.black1))
            binding.minutes1.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.minutes1.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.min1text.setTextColor(ContextCompat.getColor(this, R.color.black1))
            binding.minutes2.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.minutes2.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.min2text.setTextColor(ContextCompat.getColor(this, R.color.black1))
            binding.loop.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.loop.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.looptext.setTextColor(ContextCompat.getColor(this, R.color.black1))

        }
        else if(soundtime == 30){
            binding.second30.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.appcolor))
            binding.second30.strokeColor = ContextCompat.getColor(this, R.color.appcolor)
            binding.second30text.setTextColor(ContextCompat.getColor(this, R.color.white1))
            binding.second15.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.second15.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.second15text.setTextColor(ContextCompat.getColor(this, R.color.black1))
            binding.minutes1.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.minutes1.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.min1text.setTextColor(ContextCompat.getColor(this, R.color.black1))
            binding.minutes2.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.minutes2.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.min2text.setTextColor(ContextCompat.getColor(this, R.color.black1))
            binding.loop.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.loop.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.looptext.setTextColor(ContextCompat.getColor(this, R.color.black1))
        }
        else if(soundtime == 60){
            binding.minutes1.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.appcolor))
            binding.minutes1.strokeColor = ContextCompat.getColor(this, R.color.appcolor)
            binding.min1text.setTextColor(ContextCompat.getColor(this, R.color.white1))
            binding.second30.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.second30.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.second30text.setTextColor(ContextCompat.getColor(this, R.color.black1))
            binding.second15.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.second15.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.second15text.setTextColor(ContextCompat.getColor(this, R.color.black1))
            binding.minutes2.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.minutes2.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.min2text.setTextColor(ContextCompat.getColor(this, R.color.black1))
            binding.loop.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.loop.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.looptext.setTextColor(ContextCompat.getColor(this, R.color.black1))
        }
        else if(soundtime == 120){
            binding.minutes2.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.appcolor))
            binding.minutes2.strokeColor = ContextCompat.getColor(this, R.color.appcolor)
            binding.min2text.setTextColor(ContextCompat.getColor(this, R.color.white1))
            binding.minutes1.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.minutes1.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.min1text.setTextColor(ContextCompat.getColor(this, R.color.black1))
            binding.second30.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.second30.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.second30text.setTextColor(ContextCompat.getColor(this, R.color.black1))
            binding.second15.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.second15.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.second15text.setTextColor(ContextCompat.getColor(this, R.color.black1))
            binding.loop.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.loop.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.looptext.setTextColor(ContextCompat.getColor(this, R.color.black1))
        }
        else{
            binding.loop.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.appcolor))
            binding.loop.strokeColor = ContextCompat.getColor(this, R.color.appcolor)
            binding.looptext.setTextColor(ContextCompat.getColor(this, R.color.white1))
            binding.minutes1.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.minutes1.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.min1text.setTextColor(ContextCompat.getColor(this, R.color.black1))
            binding.second30.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.second30.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.second30text.setTextColor(ContextCompat.getColor(this, R.color.black1))
            binding.second15.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.second15.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.second15text.setTextColor(ContextCompat.getColor(this, R.color.black1))
            binding.minutes2.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
            binding.minutes2.strokeColor = ContextCompat.getColor(this, R.color.grey1)
            binding.min2text.setTextColor(ContextCompat.getColor(this, R.color.black1))
        }


        if(checked){
            binding.volume.alpha = 1f
            binding.durations.alpha = 1f
            binding.volume.isEnabled = true
            binding.second15.isClickable = true
            binding.second30.isClickable = true
            binding.minutes1.isClickable = true
            binding.minutes2.isClickable = true
            binding.loop.isClickable = true
            binding.switchMaterial.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.appcolor))
        }else{
            binding.volume.alpha = 0.5f
            binding.durations.alpha = 0.5f
            binding.volume.isEnabled = false
            binding.second15.isClickable = false
            binding.second30.isClickable = false
            binding.minutes1.isClickable = false
            binding.minutes2.isClickable = false
            binding.loop.isClickable = false
            binding.switchMaterial.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.grey1))
        }




        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        binding.volume.max = maxVolume.toFloat()

        // Set the SeekBar's progress to the current volume
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        binding.volume.setProgress(currentVolume.toFloat())
        binding.volume.setOnSeekChangeListener(object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {
                if (seekParams !=null){
                    if (seekParams.fromUser) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seekParams.progress, 0)
                    }
                }
            }
            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {

            }

        })

        volumeChangeReceiver = VolumeChangeReceiver(binding.volume)
        val intentFilter = IntentFilter("android.media.VOLUME_CHANGED_ACTION")
        registerReceiver(volumeChangeReceiver, intentFilter)

        binding.soundimage.setImageResource(image!!)
        binding.audioname.setText(name)

        binding.back.setOnClickListener {
            finish()
        }

        binding.switchMaterial.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checked = isChecked
                savesoundactive(checked)
                binding.volume.alpha = 1f
                binding.durations.alpha = 1f
                binding.volume.isEnabled = true
                binding.second15.isClickable = true
                binding.second30.isClickable = true
                binding.minutes1.isClickable = true
                binding.minutes2.isClickable = true
                binding.loop.isClickable = true
                binding.switchMaterial.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.appcolor))
            } else {
                checked = isChecked
                savesoundactive(checked)
                binding.volume.alpha = 0.5f
                binding.durations.alpha = 0.5f
                binding.volume.isEnabled = false
                binding.second15.isClickable = false
                binding.second30.isClickable = false
                binding.minutes1.isClickable = false
                binding.minutes2.isClickable = false
                binding.loop.isClickable = false
                binding.switchMaterial.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.grey1))
            }
        }
    }
    fun playSoundFromAssets(fileName: String) {
        try {
            if (ClapDetector.mediaPlayer == null || !ClapDetector.mediaPlayer!!.isPlaying) {
                val assetFileDescriptor: AssetFileDescriptor = assets.openFd(fileName)
                ClapDetector.mediaPlayer = MediaPlayer()
                ClapDetector.mediaPlayer?.setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                ClapDetector.mediaPlayer?.prepare()
                ClapDetector.mediaPlayer?.start()

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.second15 -> {
                binding.second15.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.appcolor))
                binding.second15.strokeColor = ContextCompat.getColor(this, R.color.appcolor)
                binding.second15text.setTextColor(ContextCompat.getColor(this, R.color.white1))
                binding.second30.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.second30.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.second30text.setTextColor(ContextCompat.getColor(this, R.color.black1))
                binding.minutes1.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.minutes1.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.min1text.setTextColor(ContextCompat.getColor(this, R.color.black1))
                binding.minutes2.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.minutes2.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.min2text.setTextColor(ContextCompat.getColor(this, R.color.black1))
                binding.loop.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.loop.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.looptext.setTextColor(ContextCompat.getColor(this, R.color.black1))
                soundtime = 15

            }
            R.id.second30 -> {
                binding.second30.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.appcolor))
                binding.second30.strokeColor = ContextCompat.getColor(this, R.color.appcolor)
                binding.second30text.setTextColor(ContextCompat.getColor(this, R.color.white1))
                binding.second15.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.second15.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.second15text.setTextColor(ContextCompat.getColor(this, R.color.black1))
                binding.minutes1.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.minutes1.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.min1text.setTextColor(ContextCompat.getColor(this, R.color.black1))
                binding.minutes2.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.minutes2.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.min2text.setTextColor(ContextCompat.getColor(this, R.color.black1))
                binding.loop.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.loop.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.looptext.setTextColor(ContextCompat.getColor(this, R.color.black1))
                soundtime = 30
            }
            R.id.minutes1 -> {
                binding.minutes1.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.appcolor))
                binding.minutes1.strokeColor = ContextCompat.getColor(this, R.color.appcolor)
                binding.min1text.setTextColor(ContextCompat.getColor(this, R.color.white1))
                binding.second30.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.second30.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.second30text.setTextColor(ContextCompat.getColor(this, R.color.black1))
                binding.second15.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.second15.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.second15text.setTextColor(ContextCompat.getColor(this, R.color.black1))
                binding.minutes2.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.minutes2.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.min2text.setTextColor(ContextCompat.getColor(this, R.color.black1))
                binding.loop.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.loop.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.looptext.setTextColor(ContextCompat.getColor(this, R.color.black1))
                soundtime = 60
            }
            R.id.minutes2 -> {
                binding.minutes2.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.appcolor))
                binding.minutes2.strokeColor = ContextCompat.getColor(this, R.color.appcolor)
                binding.min2text.setTextColor(ContextCompat.getColor(this, R.color.white1))
                binding.minutes1.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.minutes1.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.min1text.setTextColor(ContextCompat.getColor(this, R.color.black1))
                binding.second30.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.second30.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.second30text.setTextColor(ContextCompat.getColor(this, R.color.black1))
                binding.second15.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.second15.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.second15text.setTextColor(ContextCompat.getColor(this, R.color.black1))
                binding.loop.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.loop.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.looptext.setTextColor(ContextCompat.getColor(this, R.color.black1))
                soundtime = 120
            }
            R.id.loop -> {
                binding.loop.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.appcolor))
                binding.loop.strokeColor = ContextCompat.getColor(this, R.color.appcolor)
                binding.looptext.setTextColor(ContextCompat.getColor(this, R.color.white1))
                binding.minutes1.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.minutes1.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.min1text.setTextColor(ContextCompat.getColor(this, R.color.black1))
                binding.second30.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.second30.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.second30text.setTextColor(ContextCompat.getColor(this, R.color.black1))
                binding.second15.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.second15.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.second15text.setTextColor(ContextCompat.getColor(this, R.color.black1))
                binding.minutes2.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey1))
                binding.minutes2.strokeColor = ContextCompat.getColor(this, R.color.grey1)
                binding.min2text.setTextColor(ContextCompat.getColor(this, R.color.black1))
                soundtime = 0
            }
            R.id.apply -> {
                    savesoundtimeposition(soundtime!!)
                    savesoundposition(position!!)
                    savesound(sound!!)
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()

            }
            R.id.soundplay -> {
                    playSoundFromAssets(sound!!)
            }
        }
    }
}