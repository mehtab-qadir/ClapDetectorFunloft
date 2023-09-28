package com.cwc.clapdetector.UserInterface

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cwc.clapdetector.Adopter.LanguageAdapter
import com.cwc.clapdetector.Ads.NativeAdMob
import com.cwc.clapdetector.R
import com.cwc.clapdetector.Utils.Languages_Model
import com.cwc.clapdetector.Utils.getLanguages
import com.cwc.clapdetector.Utils.getcountrycode
import com.cwc.clapdetector.Utils.getlanguage
import com.cwc.clapdetector.Utils.savecountrycode
import com.cwc.clapdetector.Utils.switchLocale
import com.cwc.clapdetector.databinding.ActivityLanguageBinding
import com.example.spanishspeakandtranslate.data.local.shared_pref.TinyDB

class Language : AppCompatActivity(), View.OnClickListener{
    private lateinit var binding:ActivityLanguageBinding
    lateinit var languageadapter: LanguageAdapter
    var nativeAd:NativeAdMob?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        nativeAd = NativeAdMob()
        nativeAd!!.LoadNativeAd(this,R.string.native_id,R.layout.native_small,binding.shimerlayoutparentview.smalladView,binding.adviewnative,binding.nativeAd)
        binding.apply {
            val isDarkMode = resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            if (isDarkMode) {
                binding.languageconstraint.background =
                    ContextCompat.getDrawable(this@Language, R.drawable.languagebackground_night)
            } else {
                binding.languageconstraint.background =
                    ContextCompat.getDrawable(this@Language, R.drawable.languagebackground)
            }
            binding.continuee.setOnClickListener(this@Language)

            languageadapter = LanguageAdapter(this@Language, getLanguages())
            binding.recyclerview.layoutManager = LinearLayoutManager(this@Language)
            binding.recyclerview.adapter = languageadapter
            binding.recyclerview.setItemViewCacheSize(10)

        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.continuee -> {
                if(getlanguage().second == -1){
                    Toast.makeText(this, "Please Select Language", Toast.LENGTH_SHORT).show()
                }else{
                    TinyDB(this).putOutputCode("outputCodeKey",getcountrycode())
                    switchLocale(this,getcountrycode()!!)
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}