package com.cwc.clapdetector.UserInterface

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.cwc.clapdetector.R
import com.cwc.clapdetector.Utils.getflash
import com.cwc.clapdetector.Utils.getvibrate
import com.cwc.clapdetector.Utils.loadSOSPattern
import com.cwc.clapdetector.Utils.loadvibratePattern
import com.cwc.clapdetector.Utils.saveSOSPattern
import com.cwc.clapdetector.Utils.saveflash
import com.cwc.clapdetector.Utils.savevibrate
import com.cwc.clapdetector.Utils.savevibratePattern
import com.cwc.clapdetector.databinding.ActivitySettingsBinding


class Settings : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding:ActivitySettingsBinding

    //flashligh pattern
    private val sosPattern = longArrayOf(500, 500, 500, 500, 500, 500, 1500)
    private val defaultPattern = longArrayOf(1000)
    private val discoPattern = longArrayOf(100, 500)

    //vibrate pattern
    private val defaultVibration = longArrayOf(200)
    private val strongVibration = longArrayOf(100, 200)
    private val heartbeatVibration = longArrayOf(300, 100, 300, 100, 300, 500, 300, 100, 300, 100, 300)
    private val tickTockVibration = longArrayOf(300, 300, 500, 300)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            binding.defaultfl.setOnClickListener(this@Settings)
            binding.discofl.setOnClickListener(this@Settings)
            binding.sosfl.setOnClickListener(this@Settings)
            binding.defaultv.setOnClickListener(this@Settings)
            binding.strongv.setOnClickListener(this@Settings)
            binding.heartbeatv.setOnClickListener(this@Settings)
            binding.tickv.setOnClickListener(this@Settings)
            binding.back.setOnClickListener(this@Settings)
            binding.flashlight.isChecked = getflash()
            binding.vibrate.isChecked = getvibrate()

            if (getflash()) {
                binding.flashlight.trackTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this@Settings, R.color.appcolor))
            } else {
                binding.flashlight.trackTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this@Settings, R.color.grey1))
            }
            if (getvibrate()) {
                binding.vibrate.trackTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this@Settings, R.color.appcolor))
            } else {
                binding.vibrate.trackTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this@Settings, R.color.grey1))
            }

            if (loadSOSPattern().contentEquals(defaultPattern)) {
                binding.defaultfl.isChecked = true
                binding.discofl.isChecked = false
                binding.sosfl.isChecked = false
            } else if (loadSOSPattern().contentEquals(discoPattern)) {
                binding.defaultfl.isChecked = false
                binding.discofl.isChecked = true
                binding.sosfl.isChecked = false
            } else {
                binding.defaultfl.isChecked = false
                binding.discofl.isChecked = false
                binding.sosfl.isChecked = true
            }

            if (loadvibratePattern().contentEquals(defaultVibration)) {
                binding.defaultv.isChecked = true
                binding.strongv.isChecked = false
                binding.heartbeatv.isChecked = false
                binding.tickv.isChecked = false
            } else if (loadvibratePattern().contentEquals(strongVibration)) {
                binding.defaultv.isChecked = false
                binding.strongv.isChecked = true
                binding.heartbeatv.isChecked = false
                binding.tickv.isChecked = false
            } else if (loadvibratePattern().contentEquals(heartbeatVibration)) {
                binding.defaultv.isChecked = false
                binding.strongv.isChecked = false
                binding.heartbeatv.isChecked = true
                binding.tickv.isChecked = false
            } else {
                binding.defaultv.isChecked = false
                binding.strongv.isChecked = false
                binding.heartbeatv.isChecked = false
                binding.tickv.isChecked = true
            }

            binding.flashlight.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    saveflash(isChecked)
                    binding.flashlight.trackTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(this@Settings, R.color.appcolor))
                } else {
                    saveflash(isChecked)
                    binding.flashlight.trackTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(this@Settings, R.color.grey1))
                }
            }
            binding.vibrate.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    savevibrate(isChecked)
                    binding.vibrate.trackTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(this@Settings, R.color.appcolor))
                } else {
                    savevibrate(isChecked)
                    binding.vibrate.trackTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(this@Settings, R.color.grey1))
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back -> {
                onBackPressedDispatcher.onBackPressed()
            }
            R.id.defaultfl -> {
                binding.defaultfl.isChecked = true
                binding.discofl.isChecked = false
                binding.sosfl.isChecked = false
                saveSOSPattern(defaultPattern)
            }
            R.id.discofl -> {
                binding.defaultfl.isChecked = false
                binding.discofl.isChecked = true
                binding.sosfl.isChecked = false
                saveSOSPattern(discoPattern)
            }
            R.id.sosfl -> {
                binding.defaultfl.isChecked = false
                binding.discofl.isChecked = false
                binding.sosfl.isChecked = true
                saveSOSPattern(sosPattern)
            }
            R.id.defaultv -> {
                binding.defaultv.isChecked = true
                binding.strongv.isChecked = false
                binding.heartbeatv.isChecked = false
                binding.tickv.isChecked = false
                savevibratePattern(defaultVibration)
            }
            R.id.strongv -> {
                binding.defaultv.isChecked = false
                binding.strongv.isChecked = true
                binding.heartbeatv.isChecked = false
                binding.tickv.isChecked = false
                savevibratePattern(strongVibration)
            }
            R.id.heartbeatv -> {
                binding.defaultv.isChecked = false
                binding.strongv.isChecked = false
                binding.heartbeatv.isChecked = true
                binding.tickv.isChecked = false
                savevibratePattern(heartbeatVibration)
            }
            R.id.tickv -> {
                binding.defaultv.isChecked = false
                binding.strongv.isChecked = false
                binding.heartbeatv.isChecked = false
                binding.tickv.isChecked = true
                savevibratePattern(tickTockVibration)
            }
        }
        onBackPressedDispatcher.addCallback(this@Settings, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@Settings,MainActivity::class.java))
                finish()
            }
        })
    }

}